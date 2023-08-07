@file:Suppress("OPT_IN_IS_NOT_ENABLED", "DEPRECATION")

package com.ibsystem.temiassistant.presentation.screen.chat

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.ibsystem.temiassistant.MainActivity
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.domain.model.Message
import com.ibsystem.temiassistant.domain.model.MessageBody
import com.ibsystem.temiassistant.presentation.component.TopBarSection
import com.ibsystem.temiassistant.ui.theme.TextWhite
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

        val showDialog by viewModel.convertShowDialog.collectAsState()

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
            InputAmountOfMoneyDialog(
                viewModel = viewModel,
                showDialog = showDialog,
                onDismiss = { viewModel.converShowDialogSwitch() }
            )
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
        item { Text(text = "Session ID:" + viewModel.getSessionID(), color = Color.Gray) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }
        items(messages) { message ->
            MessageItem(
                messageText = message.message_body.message,
                isOut = message.isOut,
                time = message.time,
                imageUrl = message.imageUrl,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }
    }
}


@Composable
fun MessageSection(viewModel: ChatScreenViewModel) {
    val context = LocalContext.current
    val mRobot = (context as MainActivity).mRobot
    val message = remember { mutableStateOf("") }
    val suggestions = remember { mutableStateListOf(*viewModel.generateSuggestions(message.value, 3).toTypedArray()) }
    val conversationStage by viewModel.conversationStage.collectAsState()

    LaunchedEffect(conversationStage) {
        suggestions.clear()
        suggestions.addAll(viewModel.generateSuggestions(message.value, 3))
    }
    LaunchedEffect(message.value) {
        suggestions.clear()
        suggestions.addAll(viewModel.generateSuggestions(message.value, 3))
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .clickable {
                            if (viewModel.connectivityState.value == true) {
                                viewModel.messageToWit(MessageBody(suggestion))
                            } else {
                                Toast.makeText(context, "回線不調！", Toast.LENGTH_LONG).show()
                            }
                        }
                        .padding(start = 8.dp, end = 8.dp)
                )
            }
        }

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
                                if (viewModel.connectivityState.value == true) {
                                    viewModel.messageToWit(MessageBody(message.value))
                                } else {
                                    Toast.makeText(context, "回線不調！", Toast.LENGTH_LONG).show()
                                }
                                message.value = ""
                            }
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp),
                )
                IconButton(onClick = { mRobot.askQuestion("刮目せよ！吾輩の名はテミだ！") }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input"
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    messageText: String,
    isOut: Boolean,
    time: String,
    imageUrl: String? = null
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
                .let {
                    when (imageUrl) {
                        null -> it
                        else -> it.fillMaxWidth(0.7f)
                    }
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.End, // Set the horizontalArrangement parameter to Arrangement.End to arrange the children in reverse order
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = messageText,
                    color = TextWhite,
                    modifier = Modifier
                        .let {
                            when (imageUrl) {
                                null -> it
                                else -> it.weight(1f)
                            }
                        }
                )
                if (imageUrl != null) {
                    Log.i("Image", imageUrl)
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Image",
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Text(
            text = time,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun InputAmountOfMoneyDialog(
    viewModel: ChatScreenViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                InputAmountOfMoney(viewModel, onDismiss)
            }
        }
    }
}

@Composable
fun InputAmountOfMoney(
    viewModel: ChatScreenViewModel,
    onDismiss: () -> Unit
) {
    var inputAmount by rememberSaveable { mutableStateOf("") }
    var inputFrom by rememberSaveable { mutableStateOf("") }
    var inputTo by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Example list of currencies
    val currencies = listOf("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "HKD", "NZD")

    // State variables for the dropdown menus
    var showFromDropdown by remember { mutableStateOf(false) }
    var showToDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = inputFrom,
                    onValueChange = { inputFrom = it },
                    label = { Text("変換前の通貨") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            Modifier.clickable { showFromDropdown = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = showFromDropdown,
                    onDismissRequest = { showFromDropdown = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(onClick = {
                            inputFrom = currency
                            showFromDropdown = false
                        }) {
                            Text(currency)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = inputTo,
                    onValueChange = { inputTo = it },
                    label = { Text("変換後の通貨") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            Modifier.clickable { showToDropdown = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = showToDropdown,
                    onDismissRequest = { showToDropdown = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(onClick = {
                            inputTo = currency
                            showToDropdown = false
                        }) {
                            Text(currency)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputAmount,
            onValueChange = { newText ->
                inputAmount = newText.trimStart { it == '0' }
            },
            label = { Text("金額") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.convertCurrency(inputFrom, inputTo, inputAmount)
                        onDismiss()
                    }
                },
            ) {
                Text("確認")
            }
            Button(onClick = onDismiss) {
                Text("閉じる")
            }
        }
    }
}




