package com.example.tekotest2.utils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class DialogFactory {
    companion object{
        @Synchronized
        fun showDialogFragment(activity: FragmentActivity?, dialogFragment: DialogFragment?) {
            if (activity == null || dialogFragment == null || activity.isDestroyed || activity.isFinishing || dialogFragment.isAdded) return
            val fm = activity.supportFragmentManager
            val transaction = fm.beginTransaction()
            val oldFragment = fm.findFragmentByTag(dialogFragment.javaClass.canonicalName)
            if (oldFragment != null) {
                transaction.remove(oldFragment)
            }
            transaction.add(dialogFragment, dialogFragment.javaClass.canonicalName)
            transaction.commitAllowingStateLoss()
            try {
                fm.executePendingTransactions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}