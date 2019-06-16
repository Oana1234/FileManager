package com.example.filemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.ActionMode
import androidx.fragment.app.Fragment
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseActivity
import com.example.filemanager.base.view.BaseFragment
import com.example.filemanager.ui.main.fileslist.view.FilesListFragment
import com.example.filemanager.utils.FileType
import com.example.filemanager.utils.launchFileIntent
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.example.filemanager.ui.settings.SettingsActivity
import es.dmoral.toasty.Toasty
import android.graphics.Color
import kotlinx.android.synthetic.main.item_file_row.*
import com.example.filemanager.utils.deleteFile as FileUtilsDeleteFile



class MainActivity : BaseActivity(), HasSupportFragmentInjector, FilesListFragment.OnItemClickListener {

    val EXTRA_PATH = "com.example.filemanager.ui.main.path"

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val backStackManager = BackStackManager()
    private var mActionMode: ActionMode? = null
    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
    override fun getDefaultFragment(): BaseFragment = FilesListFragment()
     private var multiSelect = false
     private val selectedItems = ArrayList<String>()

    private val actionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            multiSelect = true
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.menu_cab, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.action_delete -> {
                    for (fileItem in selectedItems) {

                        FileUtilsDeleteFile(fileItem)
                        Toast.makeText(this@MainActivity, " Successfully Deleted", Toast.LENGTH_LONG).show()
                        updateContentOfCurrentFragment()

                    }
                    mode?.finish()
                    true
                }
                else -> false
            }
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            multiSelect = false
            selectedItems.clear()
              mActionMode = null

        }
    }


     fun selectItem(item: String?) {
         if (multiSelect) {
             if (selectedItems.contains(item)) {
                 selectedItems.remove(item)
                 file_row.setBackgroundColor(Color.WHITE)
             } else {
                 if (item != null) {
                     selectedItems.add(item)
                 }
                 file_row.setBackgroundColor(Color.LTGRAY)
             }
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        initBackStack()

    }

    private fun initBackStack() {
        backStackManager.onStackChangeListener = {
        }

        backStackManager.addToStack(
            fileModel = FileModel(
                Environment.getExternalStorageDirectory().absolutePath,
                FileType.FOLDER, "/", 0.0
            )
        )
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
        } else {
            launchFileIntent(fileModel)
        }
    }

    override fun onLongClick(fileModel: FileModel) {
        when(mActionMode){
            null -> {
                mActionMode = this.startActionMode(actionModeCallback)
                selectItem(fileModel.path)
            }

        }

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

        when (id) {
            R.id.action_refresh -> Toasty.info(
                this, getString(R.string.message_refresh_list),
                Toast.LENGTH_LONG, true
            ).show()
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateContentOfCurrentFragment() {
        val broadcastIntent = Intent()
        broadcastIntent.action = applicationContext.getString(R.string.file_change_broadcast)
        broadcastIntent.putExtra(EXTRA_PATH, backStackManager.top.path)
        sendBroadcast(broadcastIntent)
    }


}
