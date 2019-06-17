package com.example.filemanager.ui.main.fileslist.presenter

import com.example.filemanager.base.presenter.BasePresenter
import com.example.filemanager.ui.main.fileslist.view.FilesListMVPView
import com.example.filemanager.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FilesListPresenter<V : FilesListMVPView>@Inject internal constructor( schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : BasePresenter<V>( schedulerProvider = schedulerProvider, compositeDisposable = compositeDisposable), FilesListMVPPresenter<V> {

//    lateinit var items: MutableList<FileModel>
//
//    override fun loadItems(path:String) {
//        items = getFileModelsFromFiles(getFilesFromPath(path))
//        getView()?.showItems(items)
//
//    }

}