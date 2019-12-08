package com.laylamac.madesubmission2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class TabAdapter internal constructor(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitle = ArrayList<String>()

    private val mFragmentManager = fragmentManager

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment?, title: String) {
        if (fragment != null) {
            mFragmentList.add(fragment)
            mFragmentTitle.add(title)
        }
    }

    fun getFragment(bundle: Bundle, keyFragment: String): Fragment? {
        return mFragmentManager.getFragment(bundle, keyFragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle[position]
    }

}
