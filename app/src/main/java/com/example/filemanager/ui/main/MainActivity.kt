package com.example.filemanager.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseActivity
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.ui.main.fileslist.view.FilesListFragment
import com.example.filemanager.ui.main.fileslist.view.FilesRecyclerViewAdapter
import com.example.filemanager.utils.FileType
import com.example.filemanager.utils.launchFileIntent
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.view.Menu
import android.widget.Toast
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.example.filemanager.ui.settings.SettingsActivity
import com.example.filemanager.utils.PermissionManager
import androidx.appcompat.app.AlertDialog
import android.net.Uri
import android.os.Handler
import android.provider.Settings


class MainActivity : BaseActivity(), HasSupportFragmentInjector, FilesListFragment.OnItemClickListener{

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val backStackManager = BackStackManager()
    private lateinit var fileListRecyclerViewAdapter: FilesRecyclerViewAdapter

    lateinit var permissionManager: PermissionManager
    private val REQUEST_READ_EXTERNAL_STORAGE = 13
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 14

    lateinit var defaultFolderPath : String

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
    override fun getDefaultFragment(): BaseFragment = FilesListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionManager = PermissionManager(this)

        setSupportActionBar(mainToolbar)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val defaultFolderPath =
            sharedPreferences.getString("path", Environment.getExternalStorageDirectory().absolutePath)

        if (savedInstanceState == null) {
            val filesListFragment = FilesListFragment.build {
                if (defaultFolderPath != null) path = defaultFolderPath
            }


            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }
        //requestPermissions2()
    //    requestPermissions()
        initBackStack()

    }


    private fun initBackStack() {
        backStackManager.onStackChangeListener = {
        }

        backStackManager.addToStack(fileModel = FileModel(Environment.getExternalStorageDirectory().absolutePath, FileType.FOLDER, "/", 0.0))
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }

    private fun addFileFragment(fileModel: FileModel) {
        val filesListFragment = FilesListFragment.build {
            path = fileModel.path
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, filesListFragment)
        fragmentTransaction.addToBackStack(fileModel.path)
        fragmentTransaction.commit()
    }

    override fun onClick(fileModel: FileModel) {
        if (fileModel.fileType == FileType.FOLDER) {
            addFileFragment(fileModel)
        }
        else{
            launchFileIntent(fileModel)
        }
    }

    override fun onLongClick(fileModel: FileModel) {

    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            R.id.action_refresh -> Toast.makeText(this@MainActivity, "List automatically refreshed (AutoUpdatableAdapter)", Toast.LENGTH_LONG).show()
            R.id.action_settings ->{
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    private fun requestPermissions2(){
        permissionManager.checkPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            object : PermissionManager.PermissionAskListener {
                override fun onNeedPermission() {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }

                override fun onPermissionPreviouslyDenied() {
                    showCameraRational()
                }

                override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    dialogForSettings("Permission Denied", "Now you must allow camera access from settings.")
                }

                override fun onPermissionGranted() {
                }
            })
    }

    private fun showCameraRational() {
        AlertDialog.Builder(this).setTitle("Permission Denied")
            .setMessage("Without this permission this app is unable to show the files. Are you sure you want to deny this permission.")
            .setCancelable(false)
            .setNegativeButton("I'M SURE") { dialog, which -> dialog.dismiss() }
            .setPositiveButton("RETRY") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
                dialog.dismiss()
            }.show()

    }

    private fun dialogForSettings(title: String, msg: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(msg)
            .setCancelable(false)
            .setNegativeButton("NOT NOW") { dialog, which -> dialog.dismiss() }
            .setPositiveButton("SETTINGS") { dialog, which ->
                goToSettings()
                dialog.dismiss()
            }.show()
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.parse("package:$packageName")
        intent.data = uri
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE-> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted. Now you can call your method to open camera, fetch contact or whatever

                } else {
                    // Permission was denied.......
                    // You can again ask for permission from here
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
