package com.example.filemanager.ui.main.fileslist.view

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.ui.main.MainActivity
import com.example.filemanager.ui.main.fileslist.presenter.FilesListMVPPresenter
import com.example.filemanager.utils.FileUtils
import com.example.filemanager.utils.FileUtils.getFilesFromPath
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_files_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


class FilesListFragment : BaseFragment(), FilesListMVPView,  ListRefreshCallback {

    override fun onListRefresh() {
        loadFiles()
    }

    companion object {
        internal const val ARG_PATH: String = "com.example.filemanager.fileslist.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build(ARG_PATH)
    }

    @Inject
    internal lateinit var presenter: FilesListMVPPresenter<FilesListMVPView>
    private val filesListRvAdapter: FilesRecyclerViewAdapter by lazy { FilesRecyclerViewAdapter() }
    private var filesList = mutableListOf<FileModel>()
    private lateinit var mCallback: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(fileModel: FileModel)

        fun onLongClick(fileModel: FileModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.let {


            if (checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {

                askForMultiplePermissions()

            } else {
                loadFiles()
            }
        }

        try {
            mCallback = context as OnItemClickListener
        } catch (e: Exception) {
            throw Exception("$context should implement FilesListFragment.OnItemCLickListener")
        }
    }


    private fun loadFiles() {
        doAsync {
            arguments?.getString(ARG_PATH)?.let {
                filesList = FileUtils.getFileModelsFromFiles(getFilesFromPath(it))
            }

            uiThread {
                filesListRvAdapter.refreshList(filesList)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
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

    //called in onViewCreated
    override fun setUp() {

        initViews()

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
                    LinearLayoutManager(ctx)
                }
                adapter = filesListRvAdapter
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return context?.let {
            checkSelfPermission(
                it,
                permission
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun askForMultiplePermissions() {
        val requestCode = 13
        val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionList = arrayListOf<String>()

        if (!hasPermission(writeExternalStorage)) {
            permissionList.add(writeExternalStorage)
        }
        if (!hasPermission(readExternalStoragePermission)) {
            permissionList.add(readExternalStoragePermission)
        }
        if (permissionList.isNotEmpty()) {
            val permissions = permissionList.toTypedArray()
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        when (requestCode) {
            13-> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

                    // restart app
                    val mStartActivity = Intent(context, MainActivity::class.java)
                    val mPendingIntentId = 123456
                    val mPendingIntent = PendingIntent.getActivity(
                        context,
                        mPendingIntentId,
                        mStartActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                    val mgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() , mPendingIntent)
                    System.exit(0)

                } else {
                    // Permission was denied
                    context?.let {
                    }

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkEmptyFolderMessage() {
        if (filesList.isEmpty()) {
            context?.let {
                Toasty.info(
                    it, getString(R.string.message_empty_folder),
                    Toast.LENGTH_LONG, true).show()
            }
        }
    }

    fun showPermissionDenied(){
        context?.let {
            Toasty.warning(
                it, getString(R.string.permission_denied),
                Toast.LENGTH_LONG, true).show()
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

