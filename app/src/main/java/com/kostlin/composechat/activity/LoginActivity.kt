package com.kostlin.composechat

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kostlin.composechat.ui.theme.ComposeChatTheme
import com.kostlin.composechat.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChatTheme {
                LoginScreen()
            }
        }
    }

    @Composable
    fun LoginScreen() {

        var username by remember {
            mutableStateOf(TextFieldValue(""))
        }

        var showProgress: Boolean by remember {
            mutableStateOf(false)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 35.dp, end = 35.dp)
        ) {

            val (
                logo, usernameTextField, btnLoginAsUser, btnLoginAsGuest, progressBar
            ) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.chat_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 100.dp)
                    }
            )

            OutlinedTextField(
                value = username,
                onValueChange = { newValue -> username = newValue },
                label = { Text(text = "Enter Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(usernameTextField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(logo.bottom, margin = 32.dp)
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = {
                    viewModel.loginUser(username.text, getString(R.string.jwt_token))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(btnLoginAsUser) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(usernameTextField.bottom, margin = 16.dp)
                    }
            ) {
                Text(text = "Login as User")
            }

            Button(
                onClick = {
                    viewModel.loginUser(username.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(btnLoginAsGuest) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(btnLoginAsUser.bottom, margin = 16.dp)
                    }
            ) {
                Text(text = "Login as Guest")
            }

            if (showProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(btnLoginAsGuest.bottom, margin = 16.dp)
                    }

                )
            }

        }

    }
}