package com.example.filemanager.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.filemanager.R
import android.view.MenuItem
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.settings_dialog_edit_text.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "path") {
            context?.let {
                Toasty.info(
                    it, getString(R.string.path_refreshed_message),
                    Toast.LENGTH_LONG, true
                ).show()
            }
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var defaultFolderPath: String



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        defaultFolderPath = sharedPreferences.getString("path", Environment.getExternalStorageDirectory().absolutePath)

        val preference: Preference = findPreference("path")
        if (preference is EditTextPreference) run {
            updateSummary(preference)
        }

    }


    private fun updateSummary(editTextPreference: EditTextPreference) {
        editTextPreference.summary = defaultFolderPath
    }


}

