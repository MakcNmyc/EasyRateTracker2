package com.example.easyratetracker2.di

import com.example.easyratetracker2.api.services.CbrfService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun createCbrfService(): CbrfService
    fun createUntrackedPositionSourceFactory(): UntrackedPositionSource.Factory
//    fun createSelectableSourceRepository(): SelectableSourceRepository?
}