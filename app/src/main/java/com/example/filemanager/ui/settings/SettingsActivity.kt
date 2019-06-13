package com.example.filemanager.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.filemanager.R
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.EditTextPreference
import kotlinx.android.synthetic.main.settings_dialog_edit_text.*
import android.R.attr.key

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

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        if(rootKey=="path"){
            Log.i("AAA", "damnmmmmmmmmmmm")
        }
    }

    override fun onDisplayPreferenceDialog(preference: androidx.preference.Preference?) {
        super.onDisplayPreferenceDialog(preference)
        if (preference is EditTextPreference) {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}

