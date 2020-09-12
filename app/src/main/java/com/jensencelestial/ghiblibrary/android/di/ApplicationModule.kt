package com.jensencelestial.ghiblibrary.android.di

import android.content.Context
import androidx.room.Room
import com.jensencelestial.ghiblibrary.android.BuildConfig
import com.jensencelestial.ghiblibrary.android.data.db.AppDatabase
import com.jensencelestial.ghiblibrary.android.data.network.FilmService
import com.jensencelestial.ghiblibrary.android.data.network.LocationService
import com.jensencelestial.ghiblibrary.android.data.network.PersonService
import com.jensencelestial.ghiblibrary.android.data.network.SpeciesService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "ghibli.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFilmDao(appDatabase: AppDatabase) = appDatabase.filmDao()

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: AppDatabase) = appDatabase.locationDao()

    @Provides
    @Singleton
    fun providePeopleDao(appDatabase: AppDatabase) = appDatabase.peopleDao()

    @Provides
    @Singleton
    fun provideSpeciesDao(appDatabase: AppDatabase) = appDatabase.speciesDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideFilmsService(retrofit: Retrofit) = retrofit.create(FilmService::class.java)

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit) = retrofit.create(LocationService::class.java)

    @Provides
    @Singleton
    fun providePeopleService(retrofit: Retrofit) = retrofit.create(PersonService::class.java)

    @Provides
    @Singleton
    fun provideSpeciesService(retrofit: Retrofit) = retrofit.create(SpeciesService::class.java)
}