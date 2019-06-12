package com.example.filemanager.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.fileservice.FileChangeBroadcastReceiver
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
    override fun setUp() {

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
        //    Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            PATH = filePath
            initViews()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }

}