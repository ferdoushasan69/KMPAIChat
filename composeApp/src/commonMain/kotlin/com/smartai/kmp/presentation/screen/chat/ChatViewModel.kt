package com.smartai.kmp.presentation.screen.chat

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartai.kmp.domain.model.Role
import com.smartai.kmp.domain.usecase.DeleteGroupWithMessageUseCase
import com.smartai.kmp.domain.usecase.DeleteMessageUseCase
import com.smartai.kmp.domain.usecase.GetAllMessageByGroupByIdUseCase
import com.smartai.kmp.domain.usecase.GetContentUseCase
import com.smartai.kmp.domain.usecase.InsertMessageUseCase
import com.smartai.kmp.domain.usecase.UpdatePendingUseCase
import com.smartai.kmp.utils.AppCoroutineDispatcher
import com.smartai.kmp.utils.generateRandomKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val getContentUseCase: GetContentUseCase,
    private val getAllMessageByGroupByIdUseCase: GetAllMessageByGroupByIdUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val updatePendingUseCase: UpdatePendingUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val deleteGroupWithMessageUseCase: DeleteGroupWithMessageUseCase
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    var message by mutableStateOf("")
        private set

    var imageUri = mutableStateListOf<ByteArray>()
        private set

    var isDeleteShowingDialog by mutableStateOf(false)
        private set

    var groupId by mutableStateOf("")
        private set

    var failedMessage by mutableStateOf("")
        private set

    var lazyListState = LazyListState()

    fun getMessageList(isClicked: Boolean = false) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            if (isClicked) {
                _chatUiState.update {
                    _chatUiState.value.copy(
                        isLoading = true
                    )
                }
            }

            delay(500)
            _chatUiState.update {
                _chatUiState.value.copy(
                    message = getAllMessageByGroupByIdUseCase(groupId).reversed()
                )
            }
            _chatUiState.update {
                _chatUiState.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun deleteAllMessage(deleteGroup: () -> Unit) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            deleteMessageUseCase(groupId)
            deleteGroup()
        }
    }

    fun deleteGroupWithMessage() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            deleteGroupWithMessageUseCase(groupId)
            getMessageList(true)
        }
    }

    fun generateContentWithText(groupId: String, content: String, apiKey: String) {
        val images = imageUri.toList()
        imageUri.clear()

        viewModelScope.launch(appCoroutineDispatcher.io) {
            val messageId = generateRandomKey()
            failedMessage = messageId
            _chatUiState.update { _chatUiState.value.copy(isApiLoading = true) }

            addToMessage(
                messageId = messageId,
                groupId = groupId,
                text = content,
                images = images,
                participant = Role.YOU,
                isPending = true
            )

            try {
                val gemini = getContentUseCase(content = content, apiKey = apiKey, images = images)
                val generatedContent = gemini.candidates[0].content.parts[0].text
                val botId = generateRandomKey()
                handleContent(messageId, isPending = false)
                failedMessage = ""
                addToMessage(
                    messageId = botId,
                    groupId = groupId,
                    text = generatedContent,
                    images = emptyList(),
                    participant = Role.GEMINI,
                    isPending = false
                )
                _chatUiState.update {
                    _chatUiState.value.copy(
                        isApiLoading = false
                    )
                }
            } catch (e: Exception) {
                val erroMessage = if (e.message != null) {
                    if (e.message.toString().contains("Illegal input : Failed")) {
                        "Failed to generate content. Please try again."
                    } else {
                        e.message.toString()
                    }
                } else {
                    "Failed to generate content. Please try again."
                }
                handleError(messageId = messageId, errorMessage = erroMessage)
            }
        }
    }

    private fun handleError(messageId: String, errorMessage: String) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            updatePendingUseCase(messageId, isPending = false)
            val errorId = generateRandomKey()

            addToMessage(
                messageId = errorId,
                groupId = groupId,
                text = errorMessage,
                images = emptyList(),
                participant = Role.ERROR,
                isPending = false
            )
            failedMessage = ""
            _chatUiState.update {
                _chatUiState.value.copy(
                    isApiLoading = false
                )
            }

        }

    }

    private fun handleContent(messageId: String, isPending: Boolean) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            updatePendingUseCase(messageId = messageId, isPending)
            getMessageList()
        }
    }

    private fun addToMessage(
        messageId: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    ) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            insertMessageUseCase(
                messageId = messageId,
                groupId = groupId,
                text = text,
                images = images,
                participant = participant,
                isPending = isPending
            )
        }
    }

}