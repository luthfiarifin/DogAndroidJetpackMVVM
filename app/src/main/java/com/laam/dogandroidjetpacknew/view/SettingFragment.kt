package com.laam.dogandroidjetpacknew.view


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.laam.dogandroidjetpacknew.R

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
