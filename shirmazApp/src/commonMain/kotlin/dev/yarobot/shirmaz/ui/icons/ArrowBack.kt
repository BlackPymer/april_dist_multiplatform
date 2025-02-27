package dev.yarobot.shirmaz.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ShirmazIcons.ArrowBack: ImageVector
    get() {
        if (_ArrowBack != null) {
            return _ArrowBack!!
        }
        _ArrowBack = ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 26.dp,
            defaultHeight = 20.dp,
            viewportWidth = 26f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(6f, 9f)
                lineTo(5.293f, 8.293f)
                lineTo(4.586f, 9f)
                lineTo(5.293f, 9.707f)
                lineTo(6f, 9f)
                close()
                moveTo(21f, 10f)
                curveTo(21.552f, 10f, 22f, 9.552f, 22f, 9f)
                curveTo(22f, 8.448f, 21.552f, 8f, 21f, 8f)
                verticalLineTo(10f)
                close()
                moveTo(10.293f, 3.293f)
                lineTo(5.293f, 8.293f)
                lineTo(6.707f, 9.707f)
                lineTo(11.707f, 4.707f)
                lineTo(10.293f, 3.293f)
                close()
                moveTo(5.293f, 9.707f)
                lineTo(10.293f, 14.707f)
                lineTo(11.707f, 13.293f)
                lineTo(6.707f, 8.293f)
                lineTo(5.293f, 9.707f)
                close()
                moveTo(6f, 10f)
                horizontalLineTo(21f)
                verticalLineTo(8f)
                horizontalLineTo(6f)
                verticalLineTo(10f)
                close()
            }
        }.build()

        return _ArrowBack!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowBack: ImageVector? = null
