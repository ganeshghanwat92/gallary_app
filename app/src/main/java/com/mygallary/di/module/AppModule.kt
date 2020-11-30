package com.mygallary.di.module

import android.app.Application
import androidx.room.Room
import com.mygallary.BuildConfig
import com.mygallary.Constants
import com.mygallary.repository.Repository
import com.mygallary.repository.local.ImageDao
import com.mygallary.repository.local.ImageLocalRepository
import com.mygallary.repository.local.ImageRoomDatabase
import com.mygallary.repository.remote.ApiService
import com.mygallary.repository.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module()
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService) : RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun providesRepository(remoteDataSource: RemoteDataSource) : Repository {
        return Repository(remoteDataSource)
    }


    @Singleton // Tell Dagger to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideImageDatabase(app: Application) : ImageRoomDatabase = Room.databaseBuilder(
        app,
        ImageRoomDatabase::class.java,
        Constants.IMAGE_DB_NAME
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideImageDao(db: ImageRoomDatabase)  : ImageDao = db.imageDao() // The reason we can implement a Dao for the database

    @Provides
    @Singleton
    fun provideImageLocalRepository(imageDao: ImageDao) : ImageLocalRepository{
        return ImageLocalRepository(imageDao)
    }

}