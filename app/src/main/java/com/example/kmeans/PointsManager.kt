package com.example.kmeans

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.kmeans.algo.K
import com.example.kmeans.algo.KMeans
import com.example.kmeans.data.Point
import kotlinx.coroutines.launch

object PointsManager {

    private val points = mutableStateOf(
        emptyList<Point>()
    )

    fun newPoint(x: Float, y: Float) {
        if (points.value.size == 1) {
            KMeans.setK(K.intValue, screenWidth, screenHeight)
        }
        points.value += listOf(Point(x, y))
    }

    fun getPoints() = points.value

    fun clearPoints() {
        points.value = emptyList()
        KMeans.clearCenters()
    }
}

fun Modifier.setPointerManger(): Modifier {
    return this.pointerInput("test") {
        detectTapGestures(
            onTap = {
                PointsManager.newPoint(it.x, it.y)
            }
        )
    }
}

@Composable
fun DrawPoints(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    Canvas(modifier) {
        for (point in PointsManager.getPoints()) {
            drawCircle(Color.Magenta, radius = 10f, center = Offset(point.x, point.y))
        }
        coroutineScope.launch {
            KMeans.launchAlgo()
        }
    }
}

@Composable
fun ClearPointsButton(modifier: Modifier = Modifier) {
    Button(onClick = { PointsManager.clearPoints() }) {
        Text(
            text = "Clear Points",
            modifier = modifier
        )
    }
}