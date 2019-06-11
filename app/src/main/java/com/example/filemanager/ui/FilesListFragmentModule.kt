package com.example.filemanager.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FilesListFragmentModule {

    @ContributesAndroidInjector
    abstract internal fun provideFilesListFragment(): FilesListFragment
}