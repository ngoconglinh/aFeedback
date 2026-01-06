package com.lu.feedback.widget

import com.lu.feedback.model.Feedback

interface FeedbackViewListener {
    fun onEndFeedback()
    fun onOpenStoreForRate()
    fun onCloseClicked()
    fun onSendFeedback(list: List<String>, otherText: String)
}
