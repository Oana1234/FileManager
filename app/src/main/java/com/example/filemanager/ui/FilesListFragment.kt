package com.example.filemanager.ui

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
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment: BaseFragment() {

    companion object {

  //      internal val TAG = "FilesListFragment"

        internal val ARG_PATH: String = "com.example.filemanager.fileslist.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build(ARG_PATH)

//        fun newInstance(): FilesListFragment {
//            return FilesListFragment()
//        }

    }

    private lateinit var PATH: String
//    private lateinit var mCallback: AdapterView.OnItemClickListener
//    private lateinit var mFileChangeBroadcastReceiver: FileChangeBroadcastReceiver
    private val filesListRvAdapter: FilesRecyclerViewAdapter by lazy { FilesRecyclerViewAdapter() }
    private val filesList = mutableListOf<FileModel>()

    override fun setUp() {

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
        //   presenter.onAttach(this)
        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        PATH = filePath

        initViews()
        filesListRvAdapter.refreshList(filesList)

    }

    private fun initData() {

        filesList.apply {
            getFileModelsFromFiles(getFilesFromPath(PATH))
            if (this.isEmpty()) {
                emptyFolderLayout.visibility = View.VISIBLE
            } else {
                emptyFolderLayout.visibility = View.INVISIBLE
            }
        }

        // mFilesAdapter.updateData(files)

    }

    private fun initViews() {
        context?.let { ctx ->
            mainRecycleView.apply {
                layoutManager = LinearLayoutManager(ctx)
                adapter = filesListRvAdapter
            }
        }

        initData()
    }


//    class Builder {
//        var path: String = ""
//
//        fun build(): FilesListFragment {
//            val fragment = FilesListFragment()
//            val args = Bundle()
//            args.putString(ARG_PATH, path)
//            fragment.arguments = args;
//            return fragment
//        }
//    }


}