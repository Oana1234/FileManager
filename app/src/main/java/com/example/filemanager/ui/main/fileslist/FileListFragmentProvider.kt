package com.example.filemanager.ui.main.fileslist

import com.example.filemanager.ui.main.fileslist.view.FilesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FileListFragmentProvider {

    @ContributesAndroidInjector(modules = [FilesListModule::class])
    internal abstract fun provideFilesListFragment(): FilesListFragment
}