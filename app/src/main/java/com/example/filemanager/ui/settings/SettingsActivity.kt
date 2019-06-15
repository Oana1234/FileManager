package com.example.filemanager.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.filemanager.R
import android.view.MenuItem
import android.os.Environment
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
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
    lateinit var sharedPreferences: SharedPreferences
    lateinit var defaultFolderPath: String

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == "path") {
            val preference: Preference = findPreference(key)
            if (preference is EditTextPreference) run {
                updateSummary(preference)
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        defaultFolderPath = sharedPreferences.getString("path", Environment.getExternalStorageDirectory().absolutePath)
    }

    fun updateSummary(editTextPreference: EditTextPreference) {
        editTextPreference.summary = defaultFolderPath
    }


}

