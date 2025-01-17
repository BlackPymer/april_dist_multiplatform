package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.camera.model.ModelView
import dev.yarobot.shirmaz.core.compose.base.LocalPermissionsController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shirmaz.feature.camera.generated.resources.Res
import shirmaz.feature.camera.generated.resources.camera_not_granted
import shirmaz.feature.camera.generated.resources.camera_request
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.painter.Painter
import dev.yarobot.shirmaz.core.compose.ui.ShirmazTheme
import shirmaz.feature.camera.generated.resources.clothes
import shirmaz.feature.camera.generated.resources.gallery
import shirmaz.feature.camera.generated.resources.gallery_cd
import shirmaz.feature.camera.generated.resources.shirt1_name
import shirmaz.feature.camera.generated.resources.shirt2_name
import shirmaz.feature.camera.generated.resources.shirt3_name
import shirmaz.feature.camera.generated.resources.unclothes


@Composable
fun CameraScreen() {
    val shirts: Array<Shirt> = arrayOf(
        Shirt(
            index = 1,
            name = stringResource(Res.string.shirt1_name),
            painter = painterResource(Res.drawable.clothes)
        ),
        Shirt(
            index = 2,
            name = stringResource(Res.string.shirt2_name),
            painter = painterResource(Res.drawable.clothes)
        ),
        Shirt(
            index = 3,
            name = stringResource(Res.string.shirt3_name),
            painter = painterResource(Res.drawable.clothes)
        )
    )
    val viewModel = viewModel { CameraViewModel(shirts) }
    val state by viewModel.state.collectAsState()
    ScreenContent(
        onIntent = { viewModel.onIntent(it) },
        state = remember(state) { state },
    )
}

@Composable
private fun ScreenContent(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState,
) {
    val permissionsController = LocalPermissionsController.current
    LaunchedEffect(permissionsController) {
        onIntent(CameraIntent.RequestCamera(permissionsController))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.cameraProvideState) {
            is CameraProvideState.Granted -> {
                CameraView {
                    state.currentModel?.let {
                        ModelView(remember(state) { state.currentModel })
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        if (!state.isUnclothes) {
                            Carousel(onIntent = onIntent, state = state)
                            Spacer(
                                modifier = Modifier
                                    .height(ShirmazTheme.dimension.caruselPaddingFromToolbar)
                            )
                        }
                        ToolBar(onIntent = onIntent, state = state)
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(Res.string.camera_not_granted))
                    TextButton(
                        onClick = { onIntent(CameraIntent.RequestCamera(permissionsController)) }
                    ) {
                        Text(text = stringResource(Res.string.camera_request))
                    }
                }
            }
        }
    }
}

@Composable
private fun Carousel(onIntent: (CameraIntent) -> Unit, state: CameraScreenState) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),

        horizontalArrangement = Arrangement.spacedBy(ShirmazTheme.dimension.itemSpacing)
    ) {
        state.shirts.forEach { shirt ->
            Column {
                IconButton(
                    modifier = if (state.chosenShirt != shirt.index) {
                        Modifier
                            .size(ShirmazTheme.dimension.shirtButton)
                            .clip(RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius))
                            .background(ShirmazTheme.colors.shirtBackground)
                    } else {
                        Modifier
                            .size(ShirmazTheme.dimension.shirtButton)
                            .clip(RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius))
                            .background(ShirmazTheme.colors.shirtBackground)
                            .border(
                                ShirmazTheme.dimension.borderThikness,
                                Color.White,
                                RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius)
                            )
                    },
                    onClick = { onIntent(CameraIntent.ChooseShirt(shirt.index)) }
                ) {
                    Image(
                        painter = shirt.painter,
                        contentDescription = shirt.name
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),

                    color = ShirmazTheme.colors.text,
                    text = shirt.name

                )
            }
        }
    }

}


@Composable
private fun ToolBar(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShirmazTheme.dimension.toolBarHeight)
            .background(ShirmazTheme.colors.toolBar),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GalleryButton(onIntent = onIntent)
        TakePictureButton(onIntent = onIntent)
        if (state.isUnclothes) {
            ClothesButton(onIntent = onIntent)
        } else {
            UnclothesButton(onIntent = onIntent)
        }
    }
}


@Composable
private fun GalleryButton(modifier: Modifier = Modifier, onIntent: (CameraIntent) -> Unit) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.galleryButton),
        onClick = { onIntent(CameraIntent.OpenGallery) }

    ) {
        val painter: Painter = painterResource(Res.drawable.gallery)
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = stringResource(Res.string.gallery_cd)
        )
    }
}

@Composable
private fun TakePictureButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.takePictureButton),
        onClick = { onIntent(CameraIntent.TakePicture) }
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(ShirmazTheme.dimension.takePictureButton)
                .background(Color.White)
        )
    }
}

@Composable
private fun UnclothesButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.unclothesButton),
        onClick = { onIntent(CameraIntent.Unclothes) }
    ) {
        val painter: Painter = painterResource(Res.drawable.unclothes)
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = stringResource(Res.string.gallery_cd)
        )
    }
}

@Composable
private fun ClothesButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.clothesButton),
        onClick = { onIntent(CameraIntent.Unclothes) }
    ) {
        val painter: Painter = painterResource(Res.drawable.clothes)
        Image(
            modifier = modifier.size(ShirmazTheme.dimension.clothesButton),
            painter = painter,
            contentDescription = stringResource(Res.string.gallery_cd)

        )
    }
}