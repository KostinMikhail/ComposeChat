package com.kostlin.composechat.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme


class ChannelListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatTheme {
                ChannelsScreen(
                    filters = Filters.`in`(
                        fieldName = "type",
                        values = listOf("gaming", "messaging", "commerce", "team", "livestream")
                    ),
                    title = "Chats",
                    isShowingSearch = true,
                    onItemClick = { channel ->
                        startActivity(MessagesActivity.getIntent(this, channelId = channel.cid))
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }
}