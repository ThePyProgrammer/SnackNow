package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.base.Result

@Preview
@Composable
fun ResultPane(result: Result) {
    Card(elevation = 10.dp, modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(10.dp)) {
        BoxWithConstraints {
            if (maxWidth < 400.dp) {
                Column(Modifier.padding(10.dp), Arrangement.spacedBy(5.dp)) {
                    Box {
                        Row(Modifier.padding(10.dp).fillMaxWidth(), Arrangement.spacedBy(5.dp)) {
                            Column {
                                Text(result.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("Location: " + result.location)
                            }
                        }

                        Column(modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(String.format("$%.2f",result.price), fontSize = 20.sp, color = MaterialTheme.colors.primary)
                        }
                    }

                }
            } else {
                Column(Modifier.padding(10.dp), Arrangement.spacedBy(5.dp)) {
                    Box {
                        Row(Modifier.padding(10.dp).fillMaxWidth(), Arrangement.spacedBy(5.dp)) {
                            Column {
                                Text(result.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("Location: " + result.location)
                            }
                        }

                        Column(modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(String.format("$%.2f",result.price), fontSize = 14.sp, color = MaterialTheme.colors.primary)
                        }
                    }
                    Image(result.getImage(), "Image of " + result.name, Modifier.fillMaxWidth())

                }
            }
        }



    }
}
