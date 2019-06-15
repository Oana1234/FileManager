package com.example.filemanager.ui.main.fileslist.presenter

import com.example.filemanager.base.presenter.BasePresenter
import com.example.filemanager.ui.main.fileslist.view.FilesListMVPView
import com.example.filemanager.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import android.icu.lang.UCharacter.GraphemeClusterBreak.V



class FilesListPresenter<V : FilesListMVPView>@Inject internal constructor( schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : BasePresenter<V>( schedulerProvider = schedulerProvider, compositeDisposable = compositeDisposable), FilesListMVPPresenter<V> {

    override fun deleteFile() {
    }

    override fun resetToolbarToDefaultState() {
    }

    override fun unselectSessionList() {
    }

    override fun updateSession() {
    }


}