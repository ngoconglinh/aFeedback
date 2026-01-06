package com.lu.feedback.model

import android.content.Context
import com.lu.feedback.R

data class Feedback (
    val text: String,
    val textNonTranslate: String,
    var isSelected: Boolean = false,
    val isOtherItem: Boolean = false
) {
    companion object {
        fun getOtherFb(context: Context): Feedback {
            return Feedback(context.getString(R.string.txt_other), "Other", isSelected = false, isOtherItem = true)
        }
    }
}