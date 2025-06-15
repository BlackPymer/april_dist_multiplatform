package dev.yarobot.shirmaz.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ShirmazIcons.PhotoSearch: ImageVector
    get() {
        if (_PhotoSearch != null) {
            return _PhotoSearch!!
        }
        _PhotoSearch = ImageVector.Builder(
            name = "PhotoSearch",
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
                moveTo(15f, 8f)
                horizontalLineTo(15.01f)
                moveTo(11.5f, 21f)
                horizontalLineTo(6f)
                curveTo(5.204f, 21f, 4.441f, 20.684f, 3.879f, 20.121f)
                curveTo(3.316f, 19.559f, 3f, 18.796f, 3f, 18f)
                verticalLineTo(6f)
                curveTo(3f, 5.204f, 3.316f, 4.441f, 3.879f, 3.879f)
                curveTo(4.441f, 3.316f, 5.204f, 3f, 6f, 3f)
                horizontalLineTo(18f)
                curveTo(18.796f, 3f, 19.559f, 3.316f, 20.121f, 3.879f)
                curveTo(20.684f, 4.441f, 21f, 5.204f, 21f, 6f)
                verticalLineTo(11.5f)
                moveTo(20.2f, 20.2f)
                lineTo(22f, 22f)
                moveTo(3f, 16f)
                lineTo(8f, 11f)
                curveTo(8.928f, 10.107f, 10.072f, 10.107f, 11f, 11f)
                lineTo(13f, 13f)
                moveTo(15f, 18f)
                curveTo(15f, 18.796f, 15.316f, 19.559f, 15.879f, 20.121f)
                curveTo(16.441f, 20.684f, 17.204f, 21f, 18f, 21f)
                curveTo(18.796f, 21f, 19.559f, 20.684f, 20.121f, 20.121f)
                curveTo(20.684f, 19.559f, 21f, 18.796f, 21f, 18f)
                curveTo(21f, 17.204f, 20.684f, 16.441f, 20.121f, 15.879f)
                curveTo(19.559f, 15.316f, 18.796f, 15f, 18f, 15f)
                curveTo(17.204f, 15f, 16.441f, 15.316f, 15.879f, 15.879f)
                curveTo(15.316f, 16.441f, 15f, 17.204f, 15f, 18f)
                close()
            }
        }.build()

        return _PhotoSearch!!
    }

@Suppress("ObjectPropertyName")
private var _PhotoSearch: ImageVector? = null
