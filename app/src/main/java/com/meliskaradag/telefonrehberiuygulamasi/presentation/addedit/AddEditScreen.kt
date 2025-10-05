package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.meliskaradag.telefonrehberiuygulamasi.R

@Composable
fun AddEditScreen(
    contactId: String?,
    onDone: () -> Unit,
    vm: AddEditViewModel = viewModel()
) {
    val state by vm.state
    LaunchedEffect(contactId) { vm.loadIfEdit(contactId) }

    val context = LocalContext.current

    // Android 13+ Photo Picker
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) vm.onPickImage(context.copyUriToCache(uri))
    }

    // Kayıt işlemi bittiyse Lottie: res/raw/done.json
    if (state.saved) {
        /*val comp2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done_lottie))
        val progress2 by animateLottieCompositionAsState(comp2, iterations = 1)
        LottieAnimation(composition = comp2, progress = { progress2 })*/
        val comp by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done))
        val progress by animateLottieCompositionAsState(comp, iterations = 1)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LottieAnimation(
                composition = comp,
                progress = { progress },   //Float yerine lambda
                modifier = Modifier.size(180.dp)
            )
        }
        LaunchedEffect(progress) { if (progress == 1f) onDone() }
        return
    }

    Scaffold { pad ->
        Column(
            Modifier.padding(pad).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(if (contactId == null) "Yeni Kişi" else "Profili Düzenle",
                style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = state.firstName, onValueChange = vm::onChangeFirst,
                label = { Text("Ad") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.lastName, onValueChange = vm::onChangeLast,
                label = { Text("Soyad") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.phone, onValueChange = vm::onChangePhone,
                label = { Text("Telefon") }, modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Fotoğraf:", modifier = Modifier.weight(1f))
                Button(onClick = {
                    picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("Seç / Yükle") }
            }

            if (state.photoUrl != null) {
                AsyncImage(model = state.photoUrl, contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(180.dp))
            }

            if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = vm::onSave) { Text("Kaydet") }
                if (contactId != null) {
                    OutlinedButton(onClick = vm::onDelete) { Text("Sil") }
                    OutlinedButton(onClick = {
                        context.saveToDeviceContacts(
                            first = state.firstName, last = state.lastName, phone = state.phone
                        )
                    }) { Text("Rehbere Kaydet") }
                }
            }
        }
    }
}

// geçici File (UploadImage için)
fun android.content.Context.copyUriToCache(uri: Uri): java.io.File {
    val out = java.io.File(cacheDir, "picked_${System.currentTimeMillis()}.jpg")
    contentResolver.openInputStream(uri).use { i -> out.outputStream().use { o -> i?.copyTo(o) } }
    return out
}

//Cihaza kişi eklemek için
fun android.content.Context.saveToDeviceContacts(first: String, last: String, phone: String) {
    val ops = arrayListOf<android.content.ContentProviderOperation>()

    // 1) Raw contact oluşturmak için
    ops.add(
        android.content.ContentProviderOperation
            .newInsert(android.provider.ContactsContract.RawContacts.CONTENT_URI)
            .withValue(android.provider.ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(android.provider.ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build()
    )

    // 2) İsim bilgisi
    ops.add(
        android.content.ContentProviderOperation
            .newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(
                android.provider.ContactsContract.Data.MIMETYPE,
                android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            .withValue(android.provider.ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, first)
            .withValue(android.provider.ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, last)
            .build()
    )

    // 3) Telefon bilgisi
    ops.add(
        android.content.ContentProviderOperation
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
    )

    try {
        contentResolver.applyBatch(android.provider.ContactsContract.AUTHORITY, ops)
    } catch (_: Throwable) {

    }
}
