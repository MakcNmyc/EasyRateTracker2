package com.example.easyratetracker2.di

import com.example.easyratetracker2.api.services.CbrfService
import com.example.easyratetracker2.data.sources.executors.CbrfLatestCurrencyRate
import com.example.easyratetracker2.data.sources.factories.UntrackedSourceFactory
import dagger.Binds
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun createCbrfService(): CbrfService
    fun createCbrfLatestCurrencyRate(): CbrfLatestCurrencyRate
//    fun createUntrackedSourceFactory(): UntrackedSourceFactory
//    fun createUntrackedPositionSourceFactory(): UntrackedPositionSource.Factory
//    fun createSelectableSourceRepository(): SelectableSourceRepository?
}