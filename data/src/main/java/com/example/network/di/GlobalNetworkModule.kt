package com.example.network.di

import com.example.network.persistence.DistrictIdentifiersDataStore
import com.example.network.persistence.DistrictIdentifiersDataStoreImpl
import com.example.network.utils.TimestampUtil
import dagger.Binds
import javax.inject.Singleton

interface GlobalNetworkModule {

    @Binds
    @Singleton
    fun bindTimeStampUtil(impl: TimestampUtil): TimestampUtil

    @Binds
    @Singleton
    fun bindDistrictIdentifiersDataStore(impl: DistrictIdentifiersDataStoreImpl): DistrictIdentifiersDataStore
}