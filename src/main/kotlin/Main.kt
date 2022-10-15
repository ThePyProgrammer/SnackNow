// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.getLocationFromPostalCode
import model.base.Location
import model.base.Result
import ui.components.ResultPane
import ui.lib.Combobox
import ui.lib.NumberPicker
import util.initialize
import util.search


@Composable
@Preview
fun app() {
    var userLocation: Location? = null
    var text by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Enter a postal code!") }
    val priority = mutableStateOf("High")
    val queries = mutableStateOf(10)
    val results = mutableStateListOf(
        Result("Air, 1 atm", 0.00, "Wherever you are right now"),
        Result("Cool Ranch Doritos Chip (1 chip only)", 5.50, "7 Eleven, Simei Street 1"),
        Result("Cool Ranch Doritos Chips", 5.49, "7 Eleven, Simei Street -300000000"),
        Result("Doritos - Cool Ranch Flavour", 3.50, "FairPrice, EastPoint Mall, 3 Simei Street 6, Singapore 528833"),
        Result("Warm Ranch Doritos Chips", 5.51, "7 Eleven, Simei Street 1 + √3i"),
    )

    initialize()

    MaterialTheme {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {

            Box(Modifier.width(450.dp).fillMaxHeight()) {
                Column(Modifier.width(450.dp)) {
                    Row(Modifier.padding(10.dp).align(Alignment.Start).fillMaxWidth(), Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("My postal code: ")
                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = {
                                postalCode = it
                                userLocation = getLocationFromPostalCode(postalCode)
                                address = if (userLocation == null) "Enter a valid postal code!" else userLocation!!.name
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            label = { Text("Postal Code") }
                        )
                    }
                    Row(Modifier.padding(10.dp).align(Alignment.Start).fillMaxWidth(), Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(address)
                    }
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
                                if (userLocation != null && priority.value in arrayOf("High", "Medium", "Low")) {
                                    results.clear()
                                    results.addAll(search(text, userLocation!!, queries.value, priority.value))
                                }
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
    val windowTitle by remember { mutableStateOf("SnackNow") }
    Window(
        title = windowTitle,
        resizable = true,
        state = state,
        icon = painterResource("snack_now_icon.png"), //rememberVectorPainter(windowIcon.value),
        onCloseRequest = ::exitApplication
    ) {
        app()
    }
}
