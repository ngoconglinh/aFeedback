package com.lu.feedback

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMail(
    private val senderEmail: String,
    private val senderPass: String
) {

    suspend fun send(email: String, mSubject: String, message: String) = withContext(Dispatchers.IO) {
        val properties = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        val session = Session.getDefaultInstance(properties,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPass)
                }
            })

        try {
            val mm = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                addRecipient(Message.RecipientType.TO, InternetAddress(email))
                subject = mSubject
                setText(message)
            }
            Transport.send(mm)
        } catch (e: MessagingException) {
            Log.d("4444444444444442121", "send: ${e.message}")
            e.printStackTrace()
        }
    }
}
