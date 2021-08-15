package com.example.tekotest2.fragments.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.tekotest2.R

abstract class BaseDialogFragment<V : ViewDataBinding> : DialogFragment() {
    protected lateinit var binding: V
    protected open val idLayout : Int = 0
    private var isSetupViewCompleted: Boolean = false
    private var manager: WindowManager? = null
    private var metrics: DisplayMetrics? = null
    protected abstract fun setWidth(): Float
    protected abstract fun setHeight(): Float
    override fun onStart() {
        super.onStart()
        setupWindowDialog()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dialog?.window?.let {
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.noAnimTheme
        }

        binding = DataBindingUtil.inflate(inflater, idLayout, container, false)
        binding.lifecycleOwner = this
        metrics = DisplayMetrics()

        manager = activity?.windowManager
        return binding.root
    }
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()

    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        manager?.let {
            if (metrics != null) {
                it.defaultDisplay.getMetrics(metrics)
                val with: Float
                val height: Float
                if (setWidth() > 0 && setWidth() < 1 && setHeight() > 0 && setHeight() < 1) {
                    with = metrics!!.widthPixels * setWidth()
                    height = metrics!!.heightPixels * setHeight()
                    window?.setLayout(with.toInt(), height.toInt())
                } else if (setHeight() == 1f && setWidth() == 1f) {
                    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                } else if (setWidth() > 0 && setWidth() < 1) {
                    with = metrics!!.widthPixels * setWidth()
                    window?.setLayout(with.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                } else if (setHeight() > 0 && setHeight() < 1) {
                    height = metrics!!.heightPixels * setHeight()
                    window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height.toInt())
                } else {
                    window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                window?.setGravity(Gravity.CENTER)
            }
        }
    }

    protected open fun initLayout(){

    }

    private fun setupWindowDialog() {
        if (!isSetupViewCompleted) {
            isSetupViewCompleted = true
            val manager = requireActivity().windowManager
            val metrics = DisplayMetrics()
            if (manager != null) {
                manager.defaultDisplay.getMetrics(metrics)
                var with = WindowManager.LayoutParams.WRAP_CONTENT
                var height = WindowManager.LayoutParams.WRAP_CONTENT
                val statusBarHeight: Int =
                    getStatusBarHeight(requireActivity())

                    if (setWidth() > 0 && setWidth() < 1) {
                        with = (metrics.widthPixels * setWidth()).toInt()
                    }


                    if (setHeight() > 0 && setHeight() < 1) {
                        height = ((metrics.heightPixels - statusBarHeight) * setHeight()).toInt()
                    }

                dialog!!.window!!.setLayout(with, height)
            }
        }
    }
    open fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}