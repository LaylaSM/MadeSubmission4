package com.laylamac.madesubmission2.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.laylamac.madesubmission2.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadFragment(SettingFragment())
    }

    private fun loadFragment(settingFragment: androidx.fragment.app.Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container_setting, settingFragment).commit()
        return true

    }
}
