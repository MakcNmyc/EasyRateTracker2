package com.example.easyratetracker2.di

import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.adapters.util.NetworkObserverImpl
import com.example.easyratetracker2.data.sources.factories.UntrackedSourceFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
abstract class NetworkVMScopeModule {

    @ViewModelScoped
    @Binds
    abstract fun createNetworkObserver(obs: NetworkObserverImpl): NetworkObserver
}