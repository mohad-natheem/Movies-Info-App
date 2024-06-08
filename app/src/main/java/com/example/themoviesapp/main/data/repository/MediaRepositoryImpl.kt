package com.example.themoviesapp.main.data.repository

import com.example.themoviesapp.main.data.local.media.MediaDatabase
import com.example.themoviesapp.main.data.mappers.toMedia
import com.example.themoviesapp.main.data.mappers.toMediaEntity
import com.example.themoviesapp.main.data.remote.api.MediaApi
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.main.domain.repository.MediaRepository
import com.example.themoviesapp.util.Constants.TRENDING
import com.example.themoviesapp.util.Constants.UNAVAILABLE
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaApi: MediaApi,
    private val mediaDatabase: MediaDatabase
) : MediaRepository {

    private val mediaDao = mediaDatabase.mediaDao

    override suspend fun updateItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.updateMediaItem(mediaEntity)

    }

    override suspend fun insertItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.insetMediaItem(
            mediaEntity
        )
    }

    override suspend fun getItem(type: String, category: String, id: Int): Media {
        return mediaDao.getMediaById(
            id = id
        ).toMedia(
            type,category
        )
    }

    override suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))

            val localMediaList = mediaDao.getMediaListByTypeAndCategory(
                mediaType = type,
                category = category
            )

            val shouldLoadFromCache = localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh

            if(shouldLoadFromCache){
                emit(
                    Resource.Success(
                        data = localMediaList.map {
                            it.toMedia(type,category)
                        }
                    )
                )
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            var searchPage = page
            if(isRefresh){
                mediaDao.deleteMediaByTypeAndCategory(
                    mediaType = type,
                    category = category
                )
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApi.getMoviesAndTvSeriesList(
                    type = type,
                    category = category,
                    page = searchPage,
                    apiKey = apiKey
                ).results
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow

            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteMediaEntities = remoteMediaList.map {
                it.toMediaEntity(
                    type = type,
                    category = category)
            }
            mediaDao.insertMediaList(mediaEntities = remoteMediaEntities)

            val mediaList = mediaDao.getMediaListByTypeAndCategory(
                mediaType = type,
                category = category
            )
            emit(Resource.Success(
                data = mediaList.map {
                    it.toMedia(
                        type = type,
                        category = category
                    )
                }
            ))
            emit(Resource.Loading(isLoading = false))

        }
    }

    override suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))

            val localMediaList = mediaDao.getTrendingMediaList(
                category = TRENDING
            )

            val shouldLoadFromCache = localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh

            if(shouldLoadFromCache){
                emit(
                    Resource.Success(
                        data = localMediaList.map {
                            it.toMedia(
                                type = it.mediaType ?: UNAVAILABLE,
                                category = TRENDING
                            )
                        }
                    )
                )
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            var searchPage = page
            if(isRefresh){
                mediaDao.deleteTrendingMediaList(
                    category = TRENDING
                )
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApi.getTrendingList(
                    type = type,
                    time = time,
                    page = searchPage,
                    apiKey = apiKey
                ).results
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow

            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteMediaEntities = remoteMediaList.map {
                it.toMediaEntity(
                    type = it.media_type?: UNAVAILABLE,
                    category = TRENDING)
            }
            mediaDao.insertMediaList(mediaEntities = remoteMediaEntities)

            val mediaList = mediaDao.getTrendingMediaList(
                category = TRENDING
            )
            emit(Resource.Success(
                data = mediaList.map {
                    it.toMedia(
                        type = it.mediaType?: UNAVAILABLE,
                        category = TRENDING
                    )
                }
            ))
            emit(Resource.Loading(isLoading = false))
        }
    }
}