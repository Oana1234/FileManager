package com.example.filemanager.di

import com.example.filemanager.ui.FilesListFragmentModule
import com.example.filemanager.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [
        (FilesListFragmentModule::class)])
    abstract fun bindMainActivity(): MainActivity

}