package com.example.turfly

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                val response = chat.sendMessage(question)
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel("Error: ${e.message}", "model"))
            }
        }
    }

    fun sendImage(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                // Convert Uri to Bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

                messageList.add(MessageModel("Image uploaded", "user", imageUri))
                messageList.add(MessageModel("Processing image...", "model"))

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)  // ✅ Pass Bitmap instead of Uri
                    }
                )

                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel("Error processing image: ${e.message}", "model"))
            }
        }
    }
}
