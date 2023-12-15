package com.sunnyweather.android.ui.base

import androidx.fragment.app.Fragment

/**
 * fragment基类
 *
 * @since 2023/12/15
 */
open class BaseFragment: Fragment(){

    companion object {
        var mContainerViewId: Int = 0
    }

    private var currentFragment: Fragment? = null

    /**
     * 显示一个新的fragment
     */
    fun showFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.show(fragment)
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.add(mContainerViewId, fragment, fragment::class.java.name)
        }
        currentFragment = fragment
        transaction.commit()
    }

    /**
     * 根据tag查找fragment
     */
    fun findFragmentByTag(tag: String) = parentFragmentManager.findFragmentByTag(tag)
}