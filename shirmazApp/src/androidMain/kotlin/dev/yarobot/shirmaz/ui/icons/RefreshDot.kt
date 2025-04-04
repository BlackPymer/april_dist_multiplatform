package dev.yarobot.shirmaz.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ShirmazIcons.RefreshDot: ImageVector
    get() {
        if (_RefreshDot != null) {
            return _RefreshDot!!
        }
        _RefreshDot = ImageVector.Builder(
            name = "RefreshDot",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 11f)
                curveTo(19.755f, 9.24f, 18.939f, 7.61f, 17.677f, 6.36f)
                curveTo(16.414f, 5.109f, 14.776f, 4.309f, 13.014f, 4.082f)
                curveTo(11.252f, 3.854f, 9.464f, 4.212f, 7.925f, 5.101f)
                curveTo(6.387f, 5.99f, 5.183f, 7.36f, 4.5f, 9f)
                moveTo(4f, 5f)
                verticalLineTo(9f)
                horizontalLineTo(8f)
                moveTo(4f, 13f)
                curveTo(4.245f, 14.76f, 5.061f, 16.39f, 6.323f, 17.64f)
                curveTo(7.586f, 18.891f, 9.224f, 19.691f, 10.986f, 19.918f)
                curveTo(12.748f, 20.146f, 14.536f, 19.788f, 16.075f, 18.899f)
                curveTo(17.613f, 18.01f, 18.817f, 16.64f, 19.5f, 15f)
                moveTo(20f, 19f)
                verticalLineTo(15f)
                horizontalLineTo(16f)
                moveTo(11f, 12f)
                curveTo(11f, 12.265f, 11.105f, 12.52f, 11.293f, 12.707f)
                curveTo(11.48f, 12.895f, 11.735f, 13f, 12f, 13f)
                curveTo(12.265f, 13f, 12.52f, 12.895f, 12.707f, 12.707f)
                curveTo(12.895f, 12.52f, 13f, 12.265f, 13f, 12f)
                curveTo(13f, 11.735f, 12.895f, 11.48f, 12.707f, 11.293f)
                curveTo(12.52f, 11.105f, 12.265f, 11f, 12f, 11f)
                curveTo(11.735f, 11f, 11.48f, 11.105f, 11.293f, 11.293f)
                curveTo(11.105f, 11.48f, 11f, 11.735f, 11f, 12f)
                close()
            }
        }.build()

        return _RefreshDot!!
    }

@Suppress("ObjectPropertyName")
private var _RefreshDot: ImageVector? = null
