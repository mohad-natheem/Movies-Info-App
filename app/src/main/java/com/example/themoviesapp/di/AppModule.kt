package com.example.themoviesapp.di

import android.app.Application
import androidx.room.Room
import com.example.themoviesapp.main.data.local.genres.GenreDatabase
import com.example.themoviesapp.main.data.local.media.MediaDatabase
import com.example.themoviesapp.main.data.remote.api.GenreApi
import com.example.themoviesapp.main.data.remote.api.MediaApi
import com.example.themoviesapp.main.data.remote.api.MediaApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideMediaDatabase(
        app: Application
    ): MediaDatabase {
        return Room.databaseBuilder(
            app,
            MediaDatabase::class.java,
            "mediadb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGenreDatabase(
        app: Application
    ): GenreDatabase {
        return Room.databaseBuilder(
            app,
            GenreDatabase::class.java,
            "genredb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaApi(): MediaApi {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MediaApi::class.java)

    }

    @Provides
    @Singleton
    fun provideGenresApi(): GenreApi {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GenreApi::class.java)
    }

}