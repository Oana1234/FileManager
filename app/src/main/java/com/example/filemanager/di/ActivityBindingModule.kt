package com.example.filemanager.di

import com.example.filemanager.ui.settings.SettingsActivity
import com.example.filemanager.ui.main.MainActivity
import com.example.filemanager.ui.main.fileslist.FilesListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [
        (FilesListModule::class)])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindDefaultFolderActivity(): SettingsActivity

}