package com.lu.feedback

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri

object Utils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun View.enable(b: Boolean) {
        isClickable = b
        isFocusable = b
        isEnabled = b
        alpha = if (b) 1f else 0.4f
    }

    fun goToCHPlay(context: Context) {
        val appPackageName: String = context.packageName

        val intent = try {
            Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$appPackageName".toUri()
            )

        } catch (_: ActivityNotFoundException) {
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
            )
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}