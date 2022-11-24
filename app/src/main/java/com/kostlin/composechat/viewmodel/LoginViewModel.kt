package com.kostlin.composechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostlin.composechat.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LogInEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUsername(username: String): Boolean {
        return username.length > Constants.MIN_USERNAME_LENGTH
    }

    fun loginUser(username: String, token: String? = null) {

        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if (isValidUsername(trimmedUsername) && token != null) {
                loginRegisteredUser(trimmedUsername, token)
            } else if (isValidUsername(trimmedUsername) && token == null) {
                loginGuestUser(trimmedUsername)
            } else {
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }
    }

    private fun loginRegisteredUser(username: String, token: String) {
        val user = User(id = username, name = username)

        client.connectUser(
            user = user,
            token = token
        ).enqueue { result ->

            if (result.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LogInEvent.Success)
                }
            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LogInEvent.ErrorLogIn(
                            result.error().message ?: "Unknown error"
                        ))
                }
            }
        }
    }

    private fun loginGuestUser(username: String) {
        client.connectGuestUser(
            userId = username,
            username = username
        ).enqueue { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LogInEvent.Success)
                }
            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LogInEvent.ErrorLogIn(
                            result.error().message ?: "Unknown error"
                        ))
                }
            }
        }
    }

    sealed class LogInEvent {
        object ErrorInputTooShort : LogInEvent()
        data class ErrorLogIn(val error: String) : LogInEvent()
        object Success : LogInEvent()

    }
}