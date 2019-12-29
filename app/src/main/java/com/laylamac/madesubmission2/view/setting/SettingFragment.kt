package com.laylamac.madesubmission2.view.setting


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.utils.AlarmReceiver
import com.laylamac.madesubmission2.utils.SharedPrefManager

class SettingFragment : PreferenceFragmentCompat() {

    private val mAlarmReceiver = AlarmReceiver()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val sharedPref = SharedPrefManager(context as Context).getInstance(context as Context)
        val spcDailyReminder = findPreference<SwitchPreferenceCompat>("daily_reminder")
        val spcReleaseReminder = findPreference<SwitchPreferenceCompat>("release_reminder")
        val prefLanguage = findPreference<Preference>("preference_language")

        spcDailyReminder?.isChecked = sharedPref.checkDailyReminder() == true
        spcReleaseReminder?.isChecked = sharedPref.checkReleaseReminder() == true

        spcDailyReminder?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, _ ->
                val spcDaily = preference as SwitchPreferenceCompat

                if (spcDaily.isChecked) {
                    sharedPref.setDailyRemind(false)
                    mAlarmReceiver.cancelAlarm(context as Context, AlarmReceiver().TYPE_DAILY)
                } else {
                    sharedPref.setDailyRemind(true)
                    mAlarmReceiver.setRepeatAlarm(
                        context as Context,
                        AlarmReceiver().TYPE_DAILY,
                        "07:00", getString(R.string.daily_notif_message)
                    )
                }
                true
            }

        spcReleaseReminder?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, _ ->
                val spcRelease = preference as SwitchPreferenceCompat
                if (spcRelease.isChecked) {
                    sharedPref.setReleaseRemind(false)
                    mAlarmReceiver.cancelAlarm(context as Context, AlarmReceiver().TYPE_RELEASE)
                } else {
                    sharedPref.setReleaseRemind(true)
                    mAlarmReceiver.setRepeatAlarm(
                        context as Context,
                        AlarmReceiver().TYPE_RELEASE,
                        "08:00",
                        getString(R.string.release_notif_message)
                    )
                }
                true
            }

        prefLanguage?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }

    }


}
