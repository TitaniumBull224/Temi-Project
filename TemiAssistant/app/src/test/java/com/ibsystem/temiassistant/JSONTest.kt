package com.ibsystem.temiassistant

import com.ibsystem.temiassistant.presentation.chat_ui.Message
import com.ibsystem.temiassistant.presentation.chat_ui.MessageBody
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert
import org.junit.Test

class JSONTest {
    @Test
    fun testConnectionClassToJSON() {
        val messageBody: MessageBody = MessageBody(message = "Hi")
        //val message: Message = Message(messageBody, true)

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<MessageBody> = moshi.adapter(MessageBody::class.java)
        val json = jsonAdapter.toJson(messageBody)

        val expectedJson = "{\"message\":\"Hi\",\"type\":\"message\"}"
        Assert.assertEquals(expectedJson, json)

    }
}