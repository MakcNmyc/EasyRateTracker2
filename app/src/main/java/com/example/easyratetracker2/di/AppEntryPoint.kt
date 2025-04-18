package com.example.easyratetracker2.di

import com.example.easyratetracker2.adapters.SourceSelectionAdapter
import com.example.easyratetracker2.adapters.TrackedRatesAdapter
import com.example.easyratetracker2.adapters.UntrackedRatesAdapter
import com.example.easyratetracker2.api.services.CbrfService
import com.example.easyratetracker2.data.repositories.SelectableSourceRepository
import com.example.easyratetracker2.data.sources.executors.CbrfLatestUntrackedElement
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun createCbrfService(): CbrfService
    fun createCbrfLatestUntrackedElementExecutor(): CbrfLatestUntrackedElement
    fun createSelectableSourceRepository(): SelectableSourceRepository

    fun createTrackedRatesAdapter(): TrackedRatesAdapter
    fun createSourceSelectionAdapter(): SourceSelectionAdapter
    fun createUntrackedRatesAdapter(): UntrackedRatesAdapter
}