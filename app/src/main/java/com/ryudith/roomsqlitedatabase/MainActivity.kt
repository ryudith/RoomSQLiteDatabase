package com.ryudith.roomsqlitedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ryudith.roomsqlitedatabase.data.GameDb
import com.ryudith.roomsqlitedatabase.model.PlayerEntity
import com.ryudith.roomsqlitedatabase.repository.PlayerRepository
import com.ryudith.roomsqlitedatabase.ui.theme.RoomSQLiteDatabaseTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomSQLiteDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val db = GameDb(this)
                    val playerRepo = PlayerRepository(db)

                    val playerId = remember { mutableStateOf<Long?>(null) }
                    val playerName = remember { mutableStateOf("") }
                    val playerCreatedAt = remember { mutableStateOf(Date()) }

                    val players = playerRepo.browse().collectAsState(initial = listOf())
                    val editMode = remember { mutableStateOf(false) }
                    val resetInput = {
                        playerId.value = null
                        playerName.value = ""
                        playerCreatedAt.value = Date()

                        editMode.value = false
                    }

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = playerName.value,
                            onValueChange = { playerName.value = it },
                            label = { Text(text = "Player Name")},
                            shape = RoundedCornerShape(50)
                        )

                        Row {
                            if (editMode.value)
                            {
                                Button(onClick = {
                                    coroutineScope.launch {
                                        playerRepo.update(PlayerEntity(playerId.value, playerName.value, playerCreatedAt.value))
                                        resetInput()
                                    }
                                }) {
                                    Text(text = "Update Name")
                                }

                                Button(onClick = {
                                    coroutineScope.launch {
                                        playerRepo.delete(PlayerEntity(playerId.value, playerName.value, playerCreatedAt.value))
                                        resetInput()
                                    }
                                }) {
                                    Text(text = "Delete Player")
                                }

                                Button(onClick = {
                                    coroutineScope.launch {
                                        resetInput()
                                    }
                                }) {
                                    Text(text = "Cancel Edit")
                                }
                            }
                            else
                            {
                                Button(onClick = {
                                    coroutineScope.launch {
                                        playerRepo.create(PlayerEntity(playerId.value, playerName.value, playerCreatedAt.value))
                                        resetInput()
                                    }
                                }) {
                                    Text(text = "Save Player")
                                }
                            }
                        }

                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(players.value) {
                                Text(text = "${it.id} - ${it.createdAt.toGMTString()}")

                                Text(text = it.name, modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        val currentData = playerRepo.read(it.id!!)

                                        playerId.value = currentData!!.id
                                        playerName.value = currentData.name
                                        playerCreatedAt.value = currentData.createdAt

                                        editMode.value = true
                                    }
                                })

                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}













