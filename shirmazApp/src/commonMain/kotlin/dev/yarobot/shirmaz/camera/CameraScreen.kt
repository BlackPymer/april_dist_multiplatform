package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector
import dev.yarobot.shirmaz.render.createModelView
import dev.yarobot.shirmaz.ui.LocalPermissionsController
import dev.yarobot.shirmaz.ui.ShirmazTheme
import dev.yarobot.shirmaz.ui.icons.ArrowBack
import dev.yarobot.shirmaz.ui.icons.Cloth
import dev.yarobot.shirmaz.ui.icons.PhotoSearch
import dev.yarobot.shirmaz.ui.icons.RefreshDot
import dev.yarobot.shirmaz.ui.icons.ShirmazIcons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.back_cd
import shirmaz.shirmazapp.generated.resources.camera_not_granted
import shirmaz.shirmazapp.generated.resources.camera_request
import shirmaz.shirmazapp.generated.resources.carousel_cd
import shirmaz.shirmazapp.generated.resources.deselect_shirt_cd
import shirmaz.shirmazapp.generated.resources.gallery_cd
import shirmaz.shirmazapp.generated.resources.save_cd

@Composable
fun CameraScreen() {
    val viewModel = viewModel { CameraViewModel() }
    val state by viewModel.state.collectAsState()
    val permissionsController = LocalPermissionsController.current
    LaunchedEffect(state.cameraProvideState) {
        viewModel.onIntent(CameraIntent.RequestCamera(permissionsController))
    }
    ScreenContent(
        onIntent = viewModel::onIntent,
        state = state
    )
}

@Composable
internal fun ScreenContent(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState,
) {
    val permissionsController = LocalPermissionsController.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.cameraProvideState) {
            CameraProvideState.Granted -> {
                GrantedView(
                    state = state,
                    onIntent = onIntent
                )
            }

            CameraProvideState.NotGranted -> {
                NotGrantedView(
                    onClick = { onIntent(CameraIntent.RequestCamera(permissionsController)) },
                    messageText = stringResource(Res.string.camera_not_granted),
                    buttonText = stringResource(Res.string.camera_request)
                )
            }
        }
    }
}

@Composable
fun NotGrantedView(
    onClick: () -> Unit,
    messageText: String,
    buttonText: String
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = messageText)
        TextButton(
            onClick = onClick
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
private fun BoxScope.GrantedView(
    state: CameraScreenState,
    onIntent: (CameraIntent) -> Unit
) {
    val modelView = remember {
        createModelView(createPoseDetector(ShirmazPoseDetectorOptions.STREAM))
    }

    CameraView(
        cameraType = remember(state.currentCamera) { state.currentCamera },
        onImageCaptured = { image ->
            modelView.updateModelPosition(image)
        },
        onPictureTaken = { image ->
            onIntent(CameraIntent.SetImage(image))
        },
        capturePhotoStarted = remember(state.saving) { state.saving }
    )

    //needs to be saved
    state.currentModel?.let { shirt ->
        modelView.ModelRendererInit(shirt, Modifier)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.saving) {
            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
            var modelSize by remember { mutableStateOf(IntSize.Zero) }

            Bitmappable(
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        modelSize = layoutCoordinates.size
                    }
                    .fillMaxSize()
            ) {
                if (modelSize.width > 0 && modelSize.height > 0) {
                    LaunchedEffect(Unit) {
                        println("!!LaunchedEffect started")
                        imageBitmap = convertContentToImageBitmap()
                        println("!! imageBitmap: $imageBitmap")
                        println("!! imageBitmap size: ${imageBitmap?.width}x${imageBitmap?.height}")
                    }
                }
            }

            state.capturedPhoto?.let { image ->
                val scaledImageBitmap = imageBitmap?.resizeTo(image.width, image.height)

                if (scaledImageBitmap != null) {
                    val overlaidImage = image.overlayAlphaPixels(scaledImageBitmap)
                    println("!! after overlay: $overlaidImage")
                    onIntent(CameraIntent.SetImage(overlaidImage))
                } else {
                    println("!! Failed to scale imageBitmap")
                }
            }

            state.capturedPhoto?.let { image ->
                RenderImage(image = image)
            }
        }
        //doesn't need to be saved
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        ) {
            Carousel(
                state = state,
                onIntent = onIntent,
            )

            if (state.saving) {
                SavingPanel(
                    onIntent = onIntent,
                    state = state
                )
            } else {
                ToolBar(onIntent = onIntent)
            }
        }
    }
}



@Composable
private fun Carousel(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = ShirmazTheme.dimension.carouselPaddingLeft,
                bottom = ShirmazTheme.dimension.carouselPaddingBottom
            ),
        horizontalArrangement = Arrangement.spacedBy(ShirmazTheme.dimension.itemSpacing)
    ) {
        item {
            CarouselElement(
                onIntent = onIntent,
                shirt = null,
                isSelected = state.currentShirt == null,
            ) {
                Icon(
                    modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize)
                        .offset(0.dp, 2.dp).blur(4.dp),
                    imageVector = ShirmazIcons.Cloth,
                    tint = Color.Black.copy(alpha = 0.25f),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize),
                    imageVector = ShirmazIcons.Cloth,
                    tint = Color.White,
                    contentDescription = stringResource(Res.string.deselect_shirt_cd)
                )
            }
        }
        items(
            items = shirts,
            key = { it.nameRes.key }
        ) { shirt ->
            CarouselElement(
                onIntent = onIntent,
                shirt = shirt,
                isSelected = state.currentShirt == shirt,
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Image(
                        modifier = Modifier
                            .size(ShirmazTheme.dimension.shirtPicture)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(shirt.painterRes),
                        contentDescription = stringResource(shirt.nameRes)
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .basicMarquee(),
                        color = ShirmazTheme.colors.text,
                        text = stringResource(shirt.nameRes),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun CarouselElement(
    onIntent: (CameraIntent) -> Unit,
    shirt: Shirt?,
    isSelected: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .height(ShirmazTheme.dimension.shirtButtonHeight)
            .width(ShirmazTheme.dimension.shirtButtonWidth)
            .clip(RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius))
            .background(ShirmazTheme.colors.shirtBackground)
            .clickable {
                if (isSelected) return@clickable
                onIntent(CameraIntent.ChooseShirt(shirt))
            }
            .border(
                width = ShirmazTheme.dimension.borderThikness,
                color = if (isSelected) ShirmazTheme.colors.takePictureButton
                else Color.Transparent,
                shape = RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun ToolBar(onIntent: (CameraIntent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShirmazTheme.dimension.toolBarHeight)
            .background(ShirmazTheme.colors.toolBar),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onIntent(CameraIntent.OpenGallery) }) {
            Icon(
                modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize),
                imageVector = ShirmazIcons.PhotoSearch,
                contentDescription = stringResource(Res.string.gallery_cd),
                tint = Color.White
            )
        }
        TakePictureButton(onIntent = onIntent)
        IconButton(onClick = { onIntent(CameraIntent.ChangeCamera) }) {
            Icon(
                modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize),
                imageVector = ShirmazIcons.RefreshDot,
                contentDescription = stringResource(Res.string.carousel_cd),
                tint = Color.White
            )
        }
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
private fun SavingPanel(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShirmazTheme.dimension.savingPanelHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        ShirmazButton(
            onClick = { onIntent(CameraIntent.BackToToolbar) },
        ) {
            Icon(
                imageVector = ShirmazIcons.ArrowBack,
                contentDescription = stringResource(Res.string.back_cd)
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(Res.string.back_cd),
                color = ShirmazTheme.colors.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(ShirmazIcons.ArrowBack.defaultWidth))
        }
        ShirmazButton(
            onClick = {
                state.capturedPhoto?.let { imageBitmap ->
                    savePhoto(imageBitmap)
                    onIntent(CameraIntent.SaveImage)
                }
            }
        ) {
            Text(
                text = stringResource(Res.string.save_cd),
                color = ShirmazTheme.colors.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ShirmazButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier
            .height(ShirmazTheme.dimension.savingButtonsHeight)
            .width(ShirmazTheme.dimension.savingButtonsWidth),
        colors = ButtonDefaults.buttonColors(containerColor = ShirmazTheme.colors.toolBar),
        shape = RoundedCornerShape(ShirmazTheme.dimension.buttonCornerRadius),
        onClick = onClick
    ) {
        content()
    }
}

