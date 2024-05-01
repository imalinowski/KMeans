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
    Canvas(modifier) {
        PointsManager.getPoints().forEach { point ->
            drawCircle(
                color = KMeans.getPointColorViaCluster(point),
                radius = 10f,
                center = Offset(point.x, point.y)
            )
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