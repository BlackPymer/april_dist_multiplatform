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
import dev.yarobot.shirmaz.ui.LocalPermissionsController
import dev.yarobot.shirmaz.camera.model.ModelView
import dev.yarobot.shirmaz.core.compose.base.LocalPermissionsController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.camera_not_granted
import shirmaz.shirmazapp.generated.resources.camera_request
import shirmaz.feature.camera.generated.resources.Res
import shirmaz.feature.camera.generated.resources.camera_not_granted
import shirmaz.feature.camera.generated.resources.camera_request
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.graphics.painter.Painter
import dev.yarobot.shirmaz.core.compose.ui.ShirmazTheme
import org.jetbrains.compose.resources.DrawableResource
import shirmaz.feature.camera.generated.resources.arrow
import shirmaz.feature.camera.generated.resources.back_cd
import shirmaz.feature.camera.generated.resources.carousel_cd
import shirmaz.feature.camera.generated.resources.clothes
import shirmaz.feature.camera.generated.resources.gallery
import shirmaz.feature.camera.generated.resources.gallery_cd
import shirmaz.feature.camera.generated.resources.save_cd
import shirmaz.feature.camera.generated.resources.unclothes


@Composable
fun CameraScreen() {
    val viewModel = viewModel { CameraViewModel() }
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
    LaunchedEffect(permissionsController){
        onIntent(CameraIntent.RequestCamera(permissionsController))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.cameraProvideState) {
            is CameraProvideState.Granted -> {
                CameraView(
                    onImageCaptured = {
                        onIntent(CameraIntent.OnImageCaptured(it))
                    }
                ) {
                    state.currentModel?.let {
                        ModelView(remember(state) { state.currentModel })
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        if (state.isCarouselVisible) {
                            Carousel(onIntent = onIntent, state = state)
                        }
                        if (state.saving) {
                            SavingPanel(onIntent = onIntent, state = state)
                        } else {
                            ToolBar(onIntent = onIntent, state = state)
                        }
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
            .horizontalScroll(scrollState)
            .padding(ShirmazTheme.dimension.carouselPaddingFromLeftScreenBorder),

        horizontalArrangement = Arrangement.spacedBy(ShirmazTheme.dimension.itemSpacing)
    ) {
        state.shirts.forEach { shirt ->
            Column(
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .height(ShirmazTheme.dimension.shirtButtonHeight)
                    .width(ShirmazTheme.dimension.shirtButtonWidth)
                    .clip(RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius))
                    .background(ShirmazTheme.colors.shirtBackground)
                    .clickable { onIntent(CameraIntent.ChooseShirt(shirt)) }
                    .then(
                        if (state.currentShirt == shirt) {
                            Modifier.border(
                                ShirmazTheme.dimension.borderThikness,
                                Color.White,
                                RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius)
                            )
                        } else {
                            Modifier
                        }
                    ),
                verticalArrangement = Arrangement.Center

            ) {
                Image(
                    modifier = Modifier
                        .size(ShirmazTheme.dimension.shirtPicture)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(shirt.painterRes),
                    contentDescription = stringResource(shirt.nameRes)
                )
                if (shirt.modelName != null) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        color = ShirmazTheme.colors.text,
                        text = stringResource(shirt.nameRes),
                        fontSize = ShirmazTheme.dimension.carouselButtonFontSize

                    )
                }
            }
        }
    }
    Spacer(
        modifier = Modifier
            .then (
                if(!state.saving) {
                    Modifier.height(ShirmazTheme.dimension.carouselPaddingFromToolbar)
                }else{
                    Modifier.height(ShirmazTheme.dimension.carouselPaddingFromSavingPanel)
                }
            )
    )

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
        if (state.isCarouselVisible) {
            CarouselButton(onIntent = onIntent, image = Res.drawable.unclothes)
        } else {
            CarouselButton(onIntent = onIntent, image = Res.drawable.clothes)
        }
    }
}

@Composable
private fun GalleryButton(modifier: Modifier = Modifier, onIntent: (CameraIntent) -> Unit) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.galleryButton),
        onClick = { onIntent(CameraIntent.OpenGallery) }

    ) {
        Image(
            modifier = modifier,
            painter = painterResource(Res.drawable.gallery),
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
        modifier = modifier.size(ShirmazTheme.dimension.takePictureButtonBorderCircle),
        onClick = { onIntent(CameraIntent.TakePicture) }
    ) {
        Box(
            modifier = Modifier
                .size(ShirmazTheme.dimension.takePictureButtonBorderCircle)
                .clip(CircleShape)
                .border(
                    width = ShirmazTheme.dimension.takePictureButtonBorderWidth,
                    color = ShirmazTheme.colors.takePictureButton,
                    shape = CircleShape
                )
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(ShirmazTheme.dimension.takePictureButtonCircle)
                    .clip(CircleShape)
                    .background(ShirmazTheme.colors.takePictureButton)
            )
        }
    }
}

@Composable
private fun CarouselButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit,
    image: DrawableResource
) {
    IconButton(
        modifier = modifier.size(ShirmazTheme.dimension.carouselButton),
        onClick = { onIntent(CameraIntent.ChangeCorouselVisability) }
    ) {
        val painter: Painter = painterResource(image)
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = stringResource(Res.string.carousel_cd)
        )
    }
}


@Composable
private fun SavingPanel(onIntent: (CameraIntent) -> Unit, state: CameraScreenState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShirmazTheme.dimension.toolBarHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onIntent = onIntent)
        SaveButton(onIntent = onIntent)
    }
}

@Composable
private fun RowScope.BackButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    Button(
        modifier = modifier
            .align(Alignment.CenterVertically)
            .height(ShirmazTheme.dimension.savingButtonsHeight)
            .width(ShirmazTheme.dimension.savingButtonsWidth),
        colors = ButtonDefaults.buttonColors(containerColor = ShirmazTheme.colors.toolBar),
        shape = RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius),

        onClick = { onIntent(CameraIntent.BackToToolbar) }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(Res.drawable.arrow),
                contentDescription = stringResource(Res.string.back_cd)
            )
            Text(
                text = stringResource(Res.string.back_cd),
                color = ShirmazTheme.colors.text,
                fontSize = ShirmazTheme.dimension.savingButtonsFontSize
            )
        }
    }
}

@Composable
private fun RowScope.SaveButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    Button(
        modifier = modifier
            .align(Alignment.CenterVertically)
            .height(ShirmazTheme.dimension.savingButtonsHeight)
            .width(ShirmazTheme.dimension.savingButtonsWidth),
        colors = ButtonDefaults.buttonColors(containerColor = ShirmazTheme.colors.toolBar),
        shape = RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius),

        onClick = { onIntent(CameraIntent.SaveImage) }
    ) {
        Text(
            text = stringResource(Res.string.save_cd),
            color = ShirmazTheme.colors.text,
            fontSize = ShirmazTheme.dimension.savingButtonsFontSize
        )
    }
}