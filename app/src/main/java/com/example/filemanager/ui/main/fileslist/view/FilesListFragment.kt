package com.example.filemanager.ui.main.fileslist.view

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.utils.getFileModelsFromFiles
import com.example.filemanager.utils.getFilesFromPath
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.fragment_files_list.*


class FilesListFragment : BaseFragment() {

    companion object {
        internal val ARG_PATH: String = "com.example.filemanager.fileslist.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build(ARG_PATH)
    }

    private lateinit var PATH: String
    private val filesListRvAdapter: FilesRecyclerViewAdapter by lazy { FilesRecyclerViewAdapter() }
    private var filesList = mutableListOf<FileModel>()
    private lateinit var mCallback: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(fileModel: FileModel)

        fun onLongClick(fileModel: FileModel)
    }


    override fun setUp() {

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            return
        } else {
            PATH = filePath
            initViews()
            filesList = getFileModelsFromFiles(getFilesFromPath(PATH))
            filesListRvAdapter.refreshList(filesList)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation(resources.configuration)
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
            throw Exception("${context} should implement FilesListFragment.OnItemCLickListener")
        }
    }

    @WithPermissions(
        permissions = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE]
    )
    private fun initViews() {

        context?.let { ctx ->

            mainRecycleView.apply {
                layoutManager = LinearLayoutManager(ctx)
                adapter = filesListRvAdapter
            }


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

        setOrientation(newConfig)
    }

    private fun setOrientation(newConfig: Configuration) {


        context?.let { ctx ->
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                mainRecycleView.apply {
                    layoutManager = GridLayoutManager(ctx, 2)
                    adapter = filesListRvAdapter
                }
            } else {
                mainRecycleView.apply {
                    layoutManager = LinearLayoutManager(ctx)
                    adapter = filesListRvAdapter
                }
            }
        }

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
