package com.example.filemanager.base.presenter

import com.example.filemanager.base.view.MVPView

interface MVPPresenter < V : MVPView> {

    fun onAttach(view: V?)

    fun onDetach()

    fun getView(): V?

}