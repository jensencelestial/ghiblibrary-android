package com.jensencelestial.ghiblibrary.android.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.jensencelestial.ghiblibrary.android.BuildConfig
import com.jensencelestial.ghiblibrary.android.R
import com.mikepenz.aboutlibraries.LibsBuilder

class AboutFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about_preferences, rootKey)

        findPreference<Preference>(getString(R.string.version_about_preference_key))?.apply {
            summary = BuildConfig.VERSION_NAME
        }

        findPreference<Preference>(getString(R.string.github_about_preference_key))?.apply {
            setOnPreferenceClickListener {
                try {
                    openUrl(getString(R.string.github_link))

                    true
                } catch (e: Exception) {
                    false
                }
            }
        }

        findPreference<Preference>(getString(R.string.icons_about_preference_key))?.apply {
            setOnPreferenceClickListener {
                try {
                    openUrl(getString(R.string.good_ware_flaticon_link))

                    true
                } catch (e: Exception) {
                    false
                }
            }
        }

        findPreference<Preference>(getString(R.string.open_source_licenses_about_preference_key))?.apply {
            setOnPreferenceClickListener {
                try {
                    goToOpenSourceLicensesPage()

                    true
                } catch (e: Exception) {
                    false
                }
            }
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun goToOpenSourceLicensesPage() {
        context?.let {
            LibsBuilder().apply {
                activityTitle = getString(R.string.open_source_licenses_pascal)
                aboutShowIcon = false
                aboutShowVersion = false
            }.start(it)
        }
    }
}