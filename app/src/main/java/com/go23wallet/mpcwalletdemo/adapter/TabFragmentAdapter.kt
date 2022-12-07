package com.go23wallet.mpcwalletdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragments = mutableListOf<Fragment>()
    private var titles = mutableListOf<String>()

    fun setList(fragments: MutableList<Fragment>, titles: MutableList<String>) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}