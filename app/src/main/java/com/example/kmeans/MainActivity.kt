package com.example.kmeans

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmeans.algo.DrawCenters
import com.example.kmeans.algo.K
import com.example.kmeans.algo.KMeans
import com.example.kmeans.ui.theme.KMeansTheme

var screenHeight = 1000f
var screenWidth = 1000f

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initKMeans()
            KMeansTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .setPointerManger(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawPoints()
                    DrawCenters()

                    TopContent()
                    BottomContent()
                }
            }
        }
    }
}

@Composable
fun initKMeans() {
    val configuration = LocalConfiguration.current
    screenHeight = configuration.screenHeightDp.dp.value
    screenWidth = configuration.screenWidthDp.dp.value
}

@Composable
fun TopContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopText("K Means")
        KInputFieldValue()
    }
}

@Composable
fun BottomContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        ClearPointsButton()
    }
}

@Composable
fun TopText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Tap $name!",
        modifier = modifier
    )
}

@Composable
fun KInputFieldValue(modifier: Modifier = Modifier) {

    val text = remember { mutableStateOf("${K.intValue}") }

    TextField(
        modifier = modifier,
        label = { Text("clusters K : ") },
        value = text.value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { k ->
            text.value = k
            try {
                K.intValue = k.toInt()
                KMeans.setK(k.toInt(), screenWidth, screenHeight)
            } catch (_: Throwable) { }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KMeansTheme {
        TopText("World!")
    }
}