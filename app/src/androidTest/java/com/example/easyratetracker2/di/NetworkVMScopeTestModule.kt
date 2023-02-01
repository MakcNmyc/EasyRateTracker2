package com.example.easyratetracker2.di

import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [NetworkVMScopeModule::class])
@Module
abstract class NetworkVMScopeTestModule