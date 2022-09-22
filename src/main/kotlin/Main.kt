// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import model.base.Result
import ui.components.ResultPane
import ui.lib.Combobox
import ui.lib.NumberPicker

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("") }
    val priority = mutableStateOf("High")
    val queries = mutableStateOf(10)
    val results = listOf(
        Result("Cool Ranch Doritos Chips", 5.50, "7 Eleven, Simei Street 1"),
        Result("Cool Ranch Doritos Chips", 5.50, "7 Eleven, Simei Street 3"),
        Result("Doritos - Cool Ranch Flavour", 3.50, "FairPrice, EastPoint Mall, Simei Street 3"),
        Result("Cool Ranch Doritos Chips", 5.50, "7 Eleven, Simei Street 4"),
    )

    MaterialTheme {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {

            Box(Modifier.width(450.dp).fillMaxHeight()) {
                Column(Modifier.width(450.dp)) {
                    Row(Modifier.padding(10.dp).align(Alignment.Start).fillMaxWidth(), Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("I want to buy: ")
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Item") }
                        )
                    }
                    Row(Modifier.padding(10.dp).align(Alignment.Start).fillMaxWidth(), Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("How much do I care about distance?")
                        Combobox(
                            "", priority,
                            listOf("High", "Medium", "Low"),
                            modifier = Modifier.height(53.dp),
                            onValueChanged = {}
                        )
                    }
                    Row(Modifier.padding(10.dp).align(Alignment.Start).fillMaxWidth(), Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Number of Queries?")
                        NumberPicker(
                            queries,
                            range = 0..50,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Row(Modifier.padding(10.dp).align(Alignment.End)) {
                        Button(
                            onClick = {
                                // TODO
                            }
                        ) { Text("SEARCH") }
                    }
                }

            }

            Box(Modifier.fillMaxSize()) {
                val state = rememberLazyListState()

                LazyColumn(modifier = Modifier.width(500.dp).padding(end = 12.dp).fillMaxWidth(), state) {
                    items(results) { ResultPane(it) }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = state
                    )
                )
            }
        }
    }
}

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center),
        isMinimized = false,
        width = 800.dp,
        height = 600.dp
    )
    var windowTitle by remember { mutableStateOf("SnackNow") }
    Window(
        title = windowTitle,
        resizable = true,
        state = state,
        icon = null,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
