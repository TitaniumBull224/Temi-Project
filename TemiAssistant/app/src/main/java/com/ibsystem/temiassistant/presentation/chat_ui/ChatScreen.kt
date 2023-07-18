@file:Suppress("OPT_IN_IS_NOT_ENABLED", "DEPRECATION")

package com.ibsystem.temiassistant.presentation.chat_ui

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.ibsystem.temiassistant.MainActivity
import com.ibsystem.temiassistant.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatScreenViewModel) {
    val windowInsets = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.systemBars,
        applyBottom = false
    )
    ProvideWindowInsets {
        val keyboardController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo

        val insets = LocalWindowInsets.current
        val imeBottom = with(LocalDensity.current) { insets.ime.bottom.toDp() }

        viewModel.changeConnectivityState(activeNetwork != null)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(windowInsets),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                viewModel.connectivityState.value?.let {
                    TopBarSection(
                        username = "Chatbot",
                        profile = painterResource(id = R.drawable.ic_final_icon),
                        isOnline = it,
                        onBack = { navController.navigateUp() }
                    )
                }
                ChatSection(Modifier.weight(1f), viewModel)
                MessageSection(viewModel)
            }
            Spacer(modifier = Modifier.height(imeBottom))
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

@Composable
fun ChatSection(
    modifier: Modifier = Modifier,
    viewModel: ChatScreenViewModel
) {
    val messages: List<Message> by viewModel.messageList.observeAsState(emptyList())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Automatically scroll to the bottom of the chat whenever a new message is added
    LaunchedEffect(messages) {
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        items(messages) { message ->
            MessageItem(
                message.message_body.message,
                message.isOut,
                message.time
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun MessageSection(viewModel: ChatScreenViewModel) {
    val context = LocalContext.current
    val mRobot = (context as MainActivity).mRobot
    val message = remember { mutableStateOf("") }

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
            IconButton(onClick = {
                viewModel.clearMessage()
                viewModel.newSessionID()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Messages"
                )
            }
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
                            if(viewModel.connectivityState.value == true) {
                                viewModel.messageToWit(MessageBody(message.value))
                            } else {
                                Toast.makeText(context, "Please check your internet", Toast.LENGTH_LONG).show()
                            }
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
    isOut: Boolean,
    time: String
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

