package com.example.easyratetracker2.di

import com.example.easyratetracker2.adapters.TrackedRatesAdapter
import com.example.easyratetracker2.adapters.TrackedRatesTestAdapter
import com.example.easyratetracker2.adapters.UntrackedRatesAdapter
import com.example.easyratetracker2.adapters.UntrackedRatesTestAdapter
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.adapters.util.NetworkObserverImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class TestModule {

//    @Singleton
//    @Binds
//    abstract fun createTrackedRatesViewModel(vm: TrackedRatesViewModelTest): TrackedRatesViewModel

    @Singleton
    @Binds
    abstract fun createNetworkObserver(obs: NetworkObserverImpl): NetworkObserver

    @Binds
    abstract fun createTrackedRatesAdapter(adapter: TrackedRatesTestAdapter): TrackedRatesAdapter

    @Binds
    abstract fun createUntrackedRatesAdapter(adapter: UntrackedRatesTestAdapter): UntrackedRatesAdapter


}