package com.example.filemanager.ui.main.fileslist

import com.example.filemanager.ui.main.fileslist.view.FilesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FilesListModule {

    @ContributesAndroidInjector
    abstract internal fun provideFilesListFragment(): FilesListFragment
}