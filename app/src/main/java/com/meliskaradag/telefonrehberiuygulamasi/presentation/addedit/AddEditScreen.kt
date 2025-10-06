package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import coil.compose.AsyncImage
//import coil3.compose.AsyncImage
import com.meliskaradag.telefonrehberiuygulamasi.R
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meliskaradag.telefonrehberiuygulamasi.core.util.createImageUri
import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    isEdit: Boolean,
    //başlangıç değerleri (edit ise dolu gelir)
    contactId: String? = null,
    firstNameInit: String = "",
    lastNameInit: String = "",
    phoneInit: String = "",
    photoUrlInit: String? = null,
    isInDeviceContactsInit: Boolean = false,
    onCancel: () -> Unit,
    onSaveToDevice: (first: String, last: String, phone: String) -> Unit
) {
    //val ctx = LocalContext.current
    //val scope = rememberCoroutineScope()
    val vm: AddEditViewModel = viewModel()
    val context = LocalContext.current
    val ui = vm.state.value
    var localInDevice by remember { mutableStateOf(isInDeviceContactsInit) }
    LaunchedEffect(contactId) { //mevcut veriyi göstermek için
        vm.loadIfEdit(contactId)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showPhotoSheet by remember { mutableStateOf(false) }

    //Gallery picker
    val pickMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) vm.setLocalPhoto(uri) }

    //Camera
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) vm.setLocalPhoto(pendingCameraUri)
        pendingCameraUri = null
    }

    //izinler
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* izin sonrası kullanıcı tekrar dener */ }

    //Lottie "Done" ekranı göstermek için
    var showDone by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done))
    val doneProgress by animateLottieCompositionAsState(composition, iterations = 1)
    LaunchedEffect(ui.saved) {
        if (ui.saved) {
            showDone = true
        }
    }

    //Üst bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (isEdit) "Edit Contact" else "New Contact", //Edit Contact
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onCancel) { Text("Cancel", color = BrandBlue) }
                },
                actions = {
                    TextButton(
                        onClick = { vm.onSave() },
                        enabled = ui.firstName.isNotBlank() && ui.phone.isNotBlank()
                    ) { Text("Done", color = BrandBlue) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            AvatarWithGlow(
                localUri = vm.localPhotoUri,
                remoteUrl = ui.photoUrl,
                fallbackRes = R.drawable.ic_person
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isEdit) "Change Photo" else "Add Photo",
                color = BrandBlue,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable { showPhotoSheet = true }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedField(
                value = ui.firstName,
                onChange = vm::onChangeFirst,
                placeholder = "First Name"
            )
            Spacer(Modifier.height(12.dp))
            OutlinedField(
                value = ui.lastName,
                onChange = vm::onChangeLast,
                placeholder = "Last Name"
            )
            Spacer(Modifier.height(12.dp))
            OutlinedField(
                value = ui.phone,
                onChange = vm::onChangePhone,
                placeholder = "Phone Number",
                keyboardType = KeyboardType.Phone
            )

            Spacer(Modifier.height(20.dp))

            SaveToPhoneButton(
                enabled = !localInDevice,
                onClick = {
                    if (Build.VERSION.SDK_INT < 33) {
                        permissionLauncher.launch(arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                        ))
                    } else {
                        permissionLauncher.launch(arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                        ))
                    }
                    onSaveToDevice(ui.firstName.trim(), ui.lastName.trim(), ui.phone.trim())
                    localInDevice = true
                },
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = localInDevice) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(painterResource(R.drawable.ic_info), contentDescription = null, tint = Gray400)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "This contact is already saved your phone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray400
                    )
                }
            }
        }

        //Foto kaynak seçimi (Camera / Gallery)
        if (showPhotoSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPhotoSheet = false },
                sheetState = sheetState,
                dragHandle = { DragHandle() },
                containerColor = SurfaceColor,
                shape = MaterialTheme.shapes.large
            ) {
                SheetAction(
                    icon = R.drawable.ic_camera,
                    label = "Camera",
                    onClick = {
                        val uri = createImageUri(context)
                        pendingCameraUri = uri
                        takePictureLauncher.launch(uri)
                    }
                )
                Spacer(Modifier.height(12.dp))
                SheetAction(
                    icon = R.drawable.ic_gallery,
                    label = "Gallery",
                    onClick = {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(Modifier.height(12.dp))
                TextButton(
                    onClick = { showPhotoSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { Text("Cancel", color = BrandBlue) }
            }
        }

        //Done ekranı (Lottie)
        if (showDone) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.98f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LottieAnimation(
                        composition = composition,
                        progress = { doneProgress },
                        modifier = Modifier.size(160.dp)
                    )
                    Text("All Done!", style = MaterialTheme.typography.titleMedium, color = SuccessGreen)
                    Text("New contact saved 🎉",
                        style = MaterialTheme.typography.bodyMedium, color = Gray500)
                }
                //animasyon bitince kapatması için
                LaunchedEffect(doneProgress) {
                    if (doneProgress >= 1f) {
                        showDone = false
                    }
                }
            }
        }
    }
}

@Composable
private fun AvatarWithGlow(
    localUri: Uri?,
    remoteUrl: String?,
    @DrawableRes fallbackRes: Int
) {
    val glow = Brush.radialGradient(
        colors = listOf(Color(0xFFFFE2EC), Color.Transparent),
        center = Offset(0.5f, 0.5f),
        radius = 240f
    )
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(glow, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Gray100),
            contentAlignment = Alignment.Center
        ) {
            when {
                localUri != null -> {
                    AsyncImage(
                        model = localUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(fallbackRes),
                        error = painterResource(fallbackRes)
                    )
                }
                remoteUrl != null -> {
                    AsyncImage(
                        model = remoteUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(fallbackRes),
                        error = painterResource(fallbackRes)
                    )
                }
                else -> {
                    Icon(
                        painterResource(fallbackRes),
                        contentDescription = null,
                        tint = Gray300,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun OutlinedField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = {
            Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = Gray400)
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
        ),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Gray50,
            unfocusedContainerColor = Gray50,
            focusedBorderColor = Outline,
            unfocusedBorderColor = Outline,
            cursorColor = BrandBlue
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}

@Composable
private fun SaveToPhoneButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, if (enabled) Gray300 else Gray200),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) Gray900 else Gray300
        ),
        modifier = modifier.height(48.dp)
    ) {
        Icon(painterResource(R.drawable.ic_bookmark), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text("Save to My Phone Contact")
    }
}

@Composable
private fun SheetAction(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Gray50,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(icon), contentDescription = null, tint = Gray900)
            Spacer(Modifier.width(12.dp))
            Text(label, style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun android.content.Context.saveToDeviceContacts(first: String, last: String, phone: String) {
    val ops = arrayListOf<android.content.ContentProviderOperation>()

    //Raw contact
    ops += android.content.ContentProviderOperation
        .newInsert(android.provider.ContactsContract.RawContacts.CONTENT_URI)
        .withValue(android.provider.ContactsContract.RawContacts.ACCOUNT_TYPE, null)
        .withValue(android.provider.ContactsContract.RawContacts.ACCOUNT_NAME, null)
        .build()

    //İsim
    ops += android.content.ContentProviderOperation
        .newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(
            android.provider.ContactsContract.Data.MIMETYPE,
            android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        )
        .withValue(android.provider.ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, first)
        .withValue(android.provider.ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, last)
        .build()

    //Telefon
    ops += android.content.ContentProviderOperation
        .newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(
            android.provider.ContactsContract.Data.MIMETYPE,
            android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        .withValue(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
        .withValue(
            android.provider.ContactsContract.CommonDataKinds.Phone.TYPE,
            android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        )
        .build()

    try {
        contentResolver.applyBatch(android.provider.ContactsContract.AUTHORITY, ops)
    } catch (_: Throwable) {
        //unutma
    }
}
