package com.example.easyratetracker2.di

import com.example.easyratetracker2.api.CbrfApi
import com.example.easyratetracker2.api.services.CbrfService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [NetworkModule::class])
abstract class NetworkTestModule {
    @Singleton
    @Binds
    abstract fun createCbrfService(service: CbrfService.CbrfServiceImp): CbrfService

    companion object {
        @Singleton
        @Provides
        fun createMockWebServer(): MockWebServer {
            return MockWebServer()
        }

        @Singleton
        @Provides
        fun createCbrfApi(mockWebServer: MockWebServer, okHttpClient: OkHttpClient): CbrfApi {
            return CbrfApi.Builder.build(mockWebServer.url("/").toString(), okHttpClient)
        }

        @Provides
        fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }
    }
}