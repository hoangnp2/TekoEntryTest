package com.example.tekotest2.utils

import android.graphics.Rect
import android.view.View

class KeyboardUtils {
    companion object{
        fun addKeyboardVisibilityListener(
            rootLayout: View,
            onKeyboardVisibiltyListener: OnKeyboardVisibiltyListener
        ) {
            if (rootLayout.viewTreeObserver == null) return
            rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                rootLayout.getWindowVisibleDisplayFrame(r)
                val screenHeight = rootLayout.rootView.height

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                val keypadHeight = screenHeight - r.bottom
                val isVisible =
                    keypadHeight > screenHeight * 0.15 // 0.15 ratio is perhaps enough to determine keypad height.
                if (isVisible) {
                } else {
                }
                onKeyboardVisibiltyListener.onVisibilityChange(isVisible)
            }
        }
    }

    interface OnKeyboardVisibiltyListener {
        fun onVisibilityChange(isVisible: Boolean)
    }
}