package com.example.themoviesapp.media_details.data.repository

import com.example.themoviesapp.main.data.local.media.MediaDatabase
import com.example.themoviesapp.main.data.mappers.toMedia
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.media_details.data.remote.api.ExtraDetailsApi
import com.example.themoviesapp.media_details.data.remote.dto.details.DetailsDto
import com.example.themoviesapp.media_details.domain.repository.DetailsRepository
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoyImpl @Inject constructor(
    val extraDetailsApi: ExtraDetailsApi,
    val mediaDb: MediaDatabase
) : DetailsRepository {
    override suspend fun getDetails(
        type: String,
        id: Int,
        apikey: String,
        isRefresh: Boolean
    ): Flow<Resource<Media>> {
        return flow {
            emit(Resource.Loading(true))

            val mediaDao = mediaDb.mediaDao

            val mediaEntity = mediaDao.getMediaById(id)

            val doDetailsExistAlready = !(mediaEntity.runtime == null ||
                    mediaEntity.status == null || mediaEntity.tagline == null)

            if (!isRefresh && doDetailsExistAlready) {
                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType,
                            category = mediaEntity.category
                        )
                    )
                )
                emit(
                    Resource.Loading(isLoading = false)
                )
                return@flow
            }

            val remoteDetails = fetchDetailsFromRemote(
                type = type,
                id = id,
                apiKey = apikey
            )

            if(remoteDetails == null){
                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType,
                            category = mediaEntity.category
                        )
                    )
                )
                emit(
                    Resource.Loading(
                        isLoading = false
                    )
                )
                return@flow
            }

            remoteDetails?.let {details ->
                mediaEntity.runtime = details.runtime
                mediaEntity.status = details.status
                mediaEntity.tagline = details.tagline
            }

            emit(
                Resource.Success(
                    data = mediaEntity.toMedia(
                        type = mediaEntity.mediaType,
                        category = mediaEntity.category
                    )
                )
            )
            emit(Resource.Loading(isLoading = false))
        }
    }

    private suspend fun fetchDetailsFromRemote(
    type: String,
    id: Int,
    apiKey: String
    ): DetailsDto?
    {
        val remoteDetails = try {
            extraDetailsApi.getDetails(
                type = type,
                id = id,
                apiKey = apiKey
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
        return remoteDetails
    }
}