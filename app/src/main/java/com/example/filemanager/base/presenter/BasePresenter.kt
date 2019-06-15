package com.example.filemanager.base.presenter

import com.example.filemanager.base.view.MVPView
import com.example.filemanager.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter <V : MVPView> internal constructor(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable
) : MVPPresenter<V> {

    private var view: V? = null

    private val isViewAttached: Boolean get() = view != null

    override fun onAttach(view: V?) {
        this.view = view
    }

    override fun getView(): V? = view

    override fun onDetach() {
        compositeDisposable.dispose()
        view = null

    }
}