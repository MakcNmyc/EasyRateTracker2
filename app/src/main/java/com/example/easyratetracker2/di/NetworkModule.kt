package com.example.easyratetracker2.di

import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.api.services.CbrfService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModule {

    @Singleton
    @Binds
    abstract fun createCbrfService(service: CbrfService.CbrfServiceImp): CbrfService

    companion object {
        @Singleton
        @Provides
        fun createCbrfApi(okHttpBuilder: OkHttpClient.Builder): CbrfApi {
            return CbrfApi.Builder.build(okHttpBuilder)
        }

        @Provides
        fun createOkHttpClientBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }
    }

}