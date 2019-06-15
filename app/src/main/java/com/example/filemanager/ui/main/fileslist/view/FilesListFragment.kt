package com.example.filemanager.ui.main.fileslist.view

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.ui.main.fileslist.presenter.FilesListMVPPresenter
import com.example.filemanager.utils.getFileModelsFromFiles
import com.example.filemanager.utils.getFilesFromPath
import kotlinx.android.synthetic.main.fragment_files_list.*
import javax.inject.Inject
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import com.example.filemanager.utils.PermissionManager


class FilesListFragment : BaseFragment(), FilesListMVPView {

    override fun openSettingsActivity() {
    }

    companion object {
        internal val ARG_PATH: String = "com.example.filemanager.fileslist.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build(ARG_PATH)
    }

    @Inject
    internal lateinit var presenter: FilesListMVPPresenter<FilesListMVPView>
    private lateinit var PATH: String
    private val filesListRvAdapter: FilesRecyclerViewAdapter by lazy { FilesRecyclerViewAdapter() }
    private var filesList = mutableListOf<FileModel>()
    private lateinit var mCallback: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(fileModel: FileModel)

        fun onLongClick(fileModel: FileModel)
    }

    override fun setUp() {

        initViews()

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            return
        } else {
            PATH = filePath
            filesList = getFileModelsFromFiles(getFilesFromPath(PATH))
            filesListRvAdapter.refreshList(filesList)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mCallback = context as OnItemClickListener
        } catch (e: Exception) {
            throw Exception("$context should implement FilesListFragment.OnItemCLickListener")
        }
    }



    private fun initViews() {

        context?.let { ctx ->

            setOrientation(resources.configuration.orientation)

            filesListRvAdapter.onItemClickListener = { file, itemView: View, _: Int ->
                mCallback.onClick(file)

            }
            filesListRvAdapter.onItemLongClickListener = { file, _ ->
                mCallback.onLongClick(file)
                true
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig.orientation)
    }


    private fun setOrientation(orientation: Int) {
        context?.let { ctx ->
            mainRecycleView?.apply {
                layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(ctx, 2)
                } else {
                    GridLayoutManager(ctx, 1)
                }
                adapter = filesListRvAdapter
            }
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    class Builder {
        var path: String = ""

        fun build(s: String): FilesListFragment {
            val fragment = FilesListFragment()
            val args = Bundle()
            args.putString(s, path)
            fragment.arguments = args
            return fragment
        }
    }


}
