package com.meh.news.data.di

import com.meh.news.data.repository.HeadlinesRepository
import com.meh.news.domain.repository.IHeadlinesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideHeadlinesRepository(headlinesRepository: HeadlinesRepository): IHeadlinesRepository

}
