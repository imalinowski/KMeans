package com.example.kmeans.algo

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.example.kmeans.PointsManager
import com.example.kmeans.data.Point
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextFloat

const val MIN_K = 2

val K = mutableIntStateOf(MIN_K)

object KMeans {

    private val colors = listOf(Magenta, Cyan, Yellow, Green, Red)

    private val centers = mutableStateOf(
        emptyList<Point>()
    )

    private var clusters = mutableMapOf<Point, MutableSet<Point>>()

    fun setK(k: Int, width: Float, height: Float) {
        val newCenters = mutableListOf<Point>()
        for (i in 0..<max(MIN_K, min(k, colors.size))) {
            newCenters.add(
                Point(
                    x = nextFloat() * width,
                    y = nextFloat() * height,
                    color = colors[i]
                )
            )
        }
        Log.i("RASPBERRY", "$newCenters")
        centers.value = newCenters
    }

    fun getCenters() = centers.value

    fun clearCenters() {
        centers.value = emptyList()
    }

    suspend fun launchAlgo() {
        delay(1000)
        if (centers.value.isEmpty()) {
            return
        }
        calcClusters()
        val newCenters = calcNewCenters()

        if (centers.value != newCenters) {
            centers.value = newCenters
            launchAlgo()
        }
    }

    private fun calcClusters() {
        clusters = mutableMapOf()
        centers.value.forEach {
            clusters[it] = mutableSetOf()
        }

        PointsManager.getPoints().forEach { point ->
            val closestCenter = getClosestCenter(point)
            addPointToCluster(closestCenter, point)
        }
    }

    private fun calcNewCenters(): List<Point> {
        val newCenters = mutableListOf<Point>()
        clusters.forEach { (center, points) ->
            val newCenter = if (points.isEmpty()) {
                center
            } else {
                Point(
                    x = meanOf(points.map { it.x }),
                    y = meanOf(points.map { it.y }),
                    color = center.color
                )
            }
            newCenters.add(newCenter)
        }
        return newCenters
    }

    private fun getClosestCenter(point: Point): Point {
        return centers.value
            .map { center -> center to distance(center, point) }
            .minBy { (_, distance) -> distance }
            .first
    }

    private fun addPointToCluster(center: Point, point: Point) {
        val cluster = clusters[center] ?: mutableSetOf()
        cluster.add(point)
        clusters[center] = cluster
    }

    private fun distance(a: Point, b: Point): Float {
        val deltaX = abs(a.x - b.x)
        val deltaY = abs(a.y - b.y)
        return sqrt(deltaX.pow(2) + deltaY.pow(2))
    }

    private fun meanOf(values: List<Float>): Float {
        if (values.isEmpty()) return 0f
        return values.sum() / values.size
    }

    fun getPointColorViaCluster(point: Point): Color {
        clusters.forEach { (center, points) ->
            if (points.contains(point)) {
                return center.color
            }
        }
        return Magenta
    }
}

@Composable
fun DrawCenters(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        KMeans.getCenters().forEach { point ->
            drawRect(
                color = point.color,
                topLeft = Offset(point.x, point.y),
                size = Size(30f, 30f)
            )
        }
    }
}