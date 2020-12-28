package top.alanpu.android.flappybird.extension

import android.content.res.Resources
import android.util.TypedValue

val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )

fun Double.toDegree() : Double {
    return Math.PI / 180 * this
}