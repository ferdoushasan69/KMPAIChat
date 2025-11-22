package com.smartai.kmp.data.source.repository

import app.cash.sqldelight.async.coroutines.awaitAsList
import com.smartai.kmp.data.db.SharedDatabase
import com.smartai.kmp.data.mapper.toGemini
import com.smartai.kmp.data.source.remote.ApiService
import com.smartai.kmp.database.GroupChat
import com.smartai.kmp.database.Message
import com.smartai.kmp.domain.model.ChatMessage
import com.smartai.kmp.domain.model.Gemini
import com.smartai.kmp.domain.model.Group
import com.smartai.kmp.domain.model.Role
import com.smartai.kmp.domain.repository.GeminiRepository

class GeminiRepositoryImpl(
    private val apiService: ApiService,
    private val database: SharedDatabase
) : GeminiRepository {

    override suspend fun generateContent(
        content: String,
        apiKey: String
    ): Gemini = apiService.generateContent(content = content, apiKey = apiKey).toGemini()


    override suspend fun generateContentWithImage(
        content: String,
        apiKey: String,
        images: List<ByteArray>
    ): Gemini =
        apiService.generateContentWithImage(content = content, apiKey = apiKey, images = images)
            .toGemini()

    override suspend fun insertGroup(
        groupId: String,
        title: String,
        date: String,
        icon: String
    ) {
        database { db ->
            db.appDatabaseQueries.insertGroup(
                GroupChat = GroupChat(
                    groupId = groupId,
                    title = title,
                    date = date,
                    image = icon
                )
            )
        }
    }

    override suspend fun insertMessage(
        messageId: String,
        groupId: String,
        text: String,
        image: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    ) {
        database { db ->
            db.appDatabaseQueries.insertMessage(
                Message = Message(
                    messageId = messageId,
                    chatId = groupId,
                    conent = text,
                    images = image,
                    participant = participant,
                    isPending = if (isPending) 1L else 0L
                )
            )
        }
    }

    override suspend fun updatePendingStatus(messageId: String, isPending: Boolean) {
        database { db ->
            db.appDatabaseQueries.updateMessageByMessageId(
                isPending = if (isPending) 1L else 0L,
                messageId = messageId
            )
        }
    }

    override suspend fun getGroupList(): List<Group> {
        val groupList = arrayListOf<Group>()
        database { db ->
            groupList.addAll(db.appDatabaseQueries.getAllGroup().awaitAsList().map {
                Group(
                    groupId = it.groupId,
                    groupName = it.title,
                    date = it.date,
                    icon = it.image
                )
            })
        }
        return groupList
    }

    override suspend fun getMessageListByGroupId(groupId: String): List<ChatMessage> {
        val groupList = arrayListOf<ChatMessage>()
        database { db ->
            groupList.addAll(
                db.appDatabaseQueries.getChatByGroupId(groupId).awaitAsList().map {
                    ChatMessage(
                        messageId = it.messageId,
                        groupId = it.chatId,
                        text = it.conent,
                        images = it.images,
                        participant = it.participant,
                        isPending = it.isPending == 1L
                    )
                }
            )
        }
        return groupList
    }

    override suspend fun deleteAllMessage(groupId: String) {
        database { db ->
            db.appDatabaseQueries.deleteAllMessage(chatId = groupId)
        }
    }

    override suspend fun deleteGroupWithMessage(groupId: String) {
        database { db ->
            db.appDatabaseQueries.deleteAllMessage(groupId)
            db.appDatabaseQueries.deleteGroupe(groupId)
        }
    }
}