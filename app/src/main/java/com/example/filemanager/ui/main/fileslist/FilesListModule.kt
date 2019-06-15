package com.example.filemanager.ui.main.fileslist

import com.example.filemanager.ui.main.fileslist.presenter.FilesListMVPPresenter
import com.example.filemanager.ui.main.fileslist.presenter.FilesListPresenter
import com.example.filemanager.ui.main.fileslist.view.FilesListMVPView
import dagger.Module
import dagger.Provides

@Module
class FilesListModule {

    @Provides
    internal fun provideFilesListPresenter(filesListPresenter: FilesListPresenter<FilesListMVPView>)
            : FilesListMVPPresenter<FilesListMVPView> = filesListPresenter
}