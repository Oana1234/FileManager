package com.example.filemanager.base.view

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.filemanager.R
import com.example.filemanager.ui.main.fileslist.view.FilesListFragment
import com.tedpark.tedpermission.rx2.TedRx2Permission
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment(), MVPView{


    private var parentActivity : BaseActivity? = null
    val fragmentTag: String = this.javaClass.simpleName


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is BaseActivity){
            val activity = context as BaseActivity?
            this.parentActivity = activity
            activity?.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun getBaseActivity() = parentActivity

    private fun performDependencyInjection() = AndroidSupportInjection.inject(this)

    interface CallBack {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    abstract fun setUp()

}