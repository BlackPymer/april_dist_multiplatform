package dev.yarobot.shirmaz.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ShirmazIcons.Cloth: ImageVector
    get() {
        if (_Cloth != null) {
            return _Cloth!!
        }
        _Cloth = ImageVector.Builder(
            name = "Cloth",
            defaultWidth = 26.dp,
            defaultHeight = 26.dp,
            viewportWidth = 26f,
            viewportHeight = 26f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.669f, 4.999f)
                curveTo(15.669f, 4.292f, 15.388f, 3.614f, 14.888f, 3.114f)
                curveTo(14.388f, 2.614f, 13.71f, 2.333f, 13.003f, 2.333f)
                curveTo(12.296f, 2.333f, 11.617f, 2.614f, 11.118f, 3.114f)
                curveTo(10.618f, 3.614f, 10.337f, 4.292f, 10.337f, 4.999f)
                moveTo(19.009f, 13.672f)
                lineTo(23.623f, 16.234f)
                curveTo(24.039f, 16.465f, 24.385f, 16.803f, 24.626f, 17.213f)
                curveTo(24.868f, 17.623f, 24.995f, 18.09f, 24.995f, 18.565f)
                verticalLineTo(19.663f)
                moveTo(22.329f, 22.329f)
                horizontalLineTo(3.666f)
                curveTo(2.959f, 22.329f, 2.281f, 22.048f, 1.781f, 21.548f)
                curveTo(1.281f, 21.048f, 1f, 20.37f, 1f, 19.663f)
                verticalLineTo(18.565f)
                curveTo(1f, 18.09f, 1.127f, 17.623f, 1.368f, 17.213f)
                curveTo(1.609f, 16.803f, 1.956f, 16.465f, 2.372f, 16.234f)
                lineTo(11.267f, 11.292f)
                moveTo(1.005f, 1f)
                lineTo(25f, 24.995f)
            }
        }.build()

        return _Cloth!!
    }

@Suppress("ObjectPropertyName")
private var _Cloth: ImageVector? = null
