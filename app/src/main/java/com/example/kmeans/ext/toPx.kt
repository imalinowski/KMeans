package com.example.kmeans.ext

import android.content.Context
import android.util.TypedValue

fun Context.toPx(dp: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
)