@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.ibsystem.temiassistant.presentation.chat_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.MainActivity
import com.ibsystem.temiassistant.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.reflect.KProperty

data class Chat(
    val message: String,
    val time: String,
    val isOutgoing: Boolean
)

val message = mutableStateOf("")

val chats = mutableStateListOf(
//    Chat("Hi", "10:00 pm", true),
//    Chat("Hello", "10:00 pm", false),
//    Chat("What's up", "10:02 pm", false),
//    Chat("I am fine", "10:02 pm", true),
//    Chat("How are you doing", "10:06 pm", true),
//    Chat("I am good", "10:11 pm", false),
    Chat("刮目せよ！", "10:00 pm", false),
    Chat("Hi", "10:00 pm", true),
)

const val username = "Chatbot"
val profile = R.drawable.ic_final_icon
const val isOnline = true
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        keyboardController?.hide()
                        tryAwaitRelease()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopBarSection(
                username = username,
                profile = painterResource(id = profile),
                isOnline = isOnline,
                onBack = { navController.navigateUp() }
            )
            ChatSection(Modifier.weight(1f))
            MessageSection()
        }
    }

}

@Composable
fun TopBarSection(
    username: String,
    profile: Painter,
    isOnline: Boolean,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        backgroundColor = Color(0xFFFAFAFA),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = profile,
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = username, fontWeight = FontWeight.SemiBold)
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatSection(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        items(chats) { chat ->
            MessageItem(
                chat.message,
                chat.time,
                chat.isOutgoing
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MessageSection() {
    val context = LocalContext.current
    val mRobot = (context as MainActivity).mRobot

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(6.dp)
        ) {
            OutlinedTextField(
                placeholder = {
                    Text("Message..")
                },
                value = message.value,
                onValueChange = {
                    message.value = it
                },
                shape = RoundedCornerShape(25.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable {
                            val currentTime = Calendar.getInstance().time
                            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
                            val formattedTime = formatter.format(currentTime)
                            chats.add(Chat(message.value, formattedTime, true))
                            message.value = ""
                        }
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
            )
            IconButton(onClick = { mRobot.askQuestion("刮目せよ！") }) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Input"
                )
            }
        }
    }
}

@Composable
fun MessageItem(
    messageText: String,
    time: String,
    isOut: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOut) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isOut) MaterialTheme.colors.primary else Color(0xFF616161),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Text(
                text = messageText,
                color = Color.White
            )
        }

        Text(
            text = time,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

