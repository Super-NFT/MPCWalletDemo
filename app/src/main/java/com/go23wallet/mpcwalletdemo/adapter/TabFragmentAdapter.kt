package com.go23wallet.mpcwalletdemo.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewbinding.ViewBinding
import com.go23wallet.mpcwalletdemo.base.BaseFragment


class TabFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragments = mutableListOf<BaseFragment<out ViewBinding>>()
    private var titles = mutableListOf<String>()

    fun setList(
        fragments: MutableList<BaseFragment<out ViewBinding>>,
        titles: MutableList<String>
    ) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): BaseFragment<out ViewBinding> {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        return if (fragment == fragments[position]) {
            fragment
        } else {
            destroyItem(container, position, fragment)
            super.instantiateItem(container, position)
        }
    }
}