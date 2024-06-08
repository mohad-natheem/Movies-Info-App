package com.example.themoviesapp.media_details.data.repository

import com.example.themoviesapp.main.data.local.media.MediaDatabase
import com.example.themoviesapp.main.data.local.media.MediaEntity
import com.example.themoviesapp.main.data.mappers.toMedia
import com.example.themoviesapp.main.data.remote.dto.MediaListDto
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.media_details.data.remote.api.ExtraDetailsApi
import com.example.themoviesapp.media_details.domain.models.Cast
import com.example.themoviesapp.media_details.domain.repository.ExtraDetailsRepository
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class ExtraDetailsRepositoryImpl(
    private val extraDetailsApi: ExtraDetailsApi,
    val mediaDb: MediaDatabase
): ExtraDetailsRepository {
    override suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val mediaDao = mediaDb.mediaDao

            val mediaEntity = mediaDao.getMediaById(id = id)

            val doesSimilarMediaListExist =
                (mediaEntity.similarMediaList != null && mediaEntity.similarMediaList != "-1,-2")

            if(!isRefresh && doesSimilarMediaListExist){
                try{
                    val similarMediaListIds = mediaEntity.similarMediaList.split(",")!!.map { it.toInt() }

                    var similarMediaEntityList = ArrayList<MediaEntity>()
                    for(i in similarMediaListIds){
                        similarMediaEntityList.add(mediaDao.getMediaById(i))
                    }
                    emit(
                        Resource.Success(
                            data = similarMediaEntityList.map {
                                it.toMedia(
                                    type = it.mediaType,
                                    category = it.category
                                )
                            }
                        )
                    )
                }
                catch (e: Exception){
                    emit(Resource.Error("Something went wrong"))
                }
            }
            emit(Resource.Loading(isLoading = false))
            return@flow
        }
    }

    override suspend fun getCastList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Cast>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideosList(
        isRefresh: Boolean,
        type: String,
        apiKey: String,
        id: Int
    ): Flow<Resource<List<String>>> {
        return flow{
            val mediaDao = mediaDb.mediaDao

            val mediaEntity = mediaDao.getMediaById(id)

            val doVideosExist = mediaEntity.videos != null

            if(!isRefresh && doVideosExist){
                try{
                    val videoIds = mediaEntity.videos.split(",")!!.map { it }

                    emit(
                        Resource.Success(
                            data = videoIds
                        )
                    )
                }catch (e: Exception){
                    emit(Resource.Error(message ="Something went wrong"))
                }

                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteVideoIds = fetchVideoIdsFromRemote(
                type = mediaEntity.mediaType,
                apiKey = apiKey,
                id = id
            )

            if(remoteVideoIds == null){
                emit(Resource.Success(
                    data = emptyList()
                ))

                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            remoteVideoIds?.let {
                mediaEntity.videos =try {
                    remoteVideoIds.joinToString { "," }
                }catch (e: Exception){
                    "-1,-2"
                }
            }
            mediaDao.updateMediaItem(mediaEntity)

            emit(Resource.Success(
                data = remoteVideoIds
            ))
            emit(Resource.Loading(isLoading = false))
        }
    }

    private suspend fun fetchVideoIdsFromRemote(
        type: String,
        apiKey: String,
        id: Int
    ): List<String>? {

        val videoIds = try {
            extraDetailsApi.getVideos(
                type = type,
                apiKey = apiKey,
                id = id
            )

        }catch (e: HttpException){
            e.printStackTrace()
            null
        }catch (e: IOException){
            e.printStackTrace()
            null
        }

        val listToReturn = videoIds?.results?.filter {
            it.site == "YouTube" && it.type == "Featurette" || it.type == "Teaser"
        }

        return listToReturn?.map {
            it.key
        }

    }

}