package dev.yarobot.shirmaz.camera

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.R
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage
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
                    messageText = stringResource(R.string.camera_not_granted),
                    buttonText = stringResource(R.string.camera_request)
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

    when (state.appMode) {
        AppMode.CameraMode -> {
            CameraView(
                cameraType = remember(state.currentCamera) { state.currentCamera },
                onImageCaptured = { image ->
                    modelView.updateModelPosition(image)
                },
                onPictureTaken = { image ->
                    println("!!!Picture si taken")
                    onIntent(CameraIntent.SetImage(image))
                },
                capturePhotoStarted = state.isSaving && state.appMode == AppMode.CameraMode
            )

            ToolBarPanel(
                onIntent = onIntent,
                state = state
            )
        }

        AppMode.StaticImage -> {
            state.staticImage?.let { imageBitmap ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = imageBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                modelView.updateModelPosition(imageBitmap.toPlatformInputImage())
            } ?: CircularProgressIndicator(Modifier.align(Alignment.Center))
            SavingBarPanel(
                state = state,
                onIntent = onIntent
            )
        }

        AppMode.SavingImage -> {
            SavingImage(
                state = state,
                onIntent = onIntent
            )
        }
    }
    modelView.ModelRendererInit(state, Modifier, onIntent)
}

@Composable
private fun CameraView(
    cameraType: CameraType,
    onImageCaptured: (PlatformImage) -> Unit,
    onPictureTaken: (ImageBitmap) -> Unit,
    capturePhotoStarted: Boolean
) {
    val viewModel = viewModel { CameraXViewModel() }
    val context = LocalContext.current

    viewModel.setAnalyzeUseCase(onImageCaptured)
    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    LaunchedEffect(capturePhotoStarted) {
        if (capturePhotoStarted) {
            viewModel.takePicture { bitmap ->
                onPictureTaken(bitmap.asImageBitmap())
            }
        }
    }
    LaunchedEffect(lifecycleOwner, cameraType) {
        viewModel.bindToCamera(
            context.applicationContext,
            lifecycleOwner,
            when (cameraType) {
                CameraType.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraType.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }
        )
    }
    surfaceRequest?.let { request ->
        CameraXViewfinder(surfaceRequest = request)
    }
}


@Composable
private fun SavingImage(
    state: CameraScreenState,
    onIntent: (CameraIntent) -> Unit
) {
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
            LaunchedEffect(state.viewCreated) {
                if (state.viewCreated) {
                    imageBitmap = convertContentToImageBitmap()
                }
            }
        }
    }

    LaunchedEffect(state.staticImage) {
        state.staticImage?.let { image ->
            val scaledImageBitmap = imageBitmap?.resizeTo(image.width, image.height)
            if (scaledImageBitmap != null) {
                val overlaidImage = image.overlayAlphaPixels(scaledImageBitmap)
                onIntent(CameraIntent.SetImage(overlaidImage))
                onIntent(CameraIntent.OnImageCreated)
            } else {
                onIntent(CameraIntent.SetImage(state.staticImage))
                onIntent(CameraIntent.OnImageCreated)
            }
        } ?: run {
            println("!!capturedPhoto is null")
        }
    }
}

@Composable
private fun BoxScope.ToolBarPanel(
    state: CameraScreenState,
    onIntent: (CameraIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(1f)
    ) {
        Carousel(
            state = state,
            onIntent = onIntent,
        )
        ToolBar(onIntent = onIntent)
    }
}

@Composable
private fun BoxScope.SavingBarPanel(
    state: CameraScreenState,
    onIntent: (CameraIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(1f)
    ) {
        Carousel(
            state = state,
            onIntent = onIntent,
        )
        SavingPanel(
            state = state,
            onIntent = onIntent,
        )
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
                    contentDescription = stringResource(R.string.deselect_shirt_cd)
                )
            }
        }
        items(
            items = shirts,
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
    var isSelectingImage by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShirmazTheme.dimension.toolBarHeight)
            .background(ShirmazTheme.colors.toolBar),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelectingImage) {
            GetImageFromGallery { imageFromGallery ->
                onIntent(CameraIntent.SetImage(imageFromGallery))
                isSelectingImage = false
            }
        }
        IconButton(onClick = { isSelectingImage = true }) {
            Icon(
                modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize),
                imageVector = ShirmazIcons.PhotoSearch,
                contentDescription = stringResource(R.string.gallery_cd),
                tint = Color.White
            )
        }
        TakePictureButton(onIntent = onIntent)
        IconButton(onClick = { onIntent(CameraIntent.ChangeCamera) }) {
            Icon(
                modifier = Modifier.size(ShirmazTheme.dimension.sideCarouselButtonSize),
                imageVector = ShirmazIcons.RefreshDot,
                contentDescription = stringResource(R.string.carousel_cd),
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
            onClick = { onIntent(CameraIntent.BackToCamera) },
        ) {
            Icon(
                imageVector = ShirmazIcons.ArrowBack,
                contentDescription = stringResource(R.string.back_cd)
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.back_cd),
                color = ShirmazTheme.colors.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(ShirmazIcons.ArrowBack.defaultWidth))
        }
        ShirmazButton(
            onClick = {
                state.staticImage?.let { imageBitmap ->
                    savePhoto(imageBitmap)
                    onIntent(CameraIntent.SaveImage)
                }
            }
        ) {
            Text(
                text = stringResource(R.string.save_cd),
                color = ShirmazTheme.colors.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ShirmazButton(
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
