package com.meliskaradag.telefonrehberiuygulamasi.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.meliskaradag.telefonrehberiuygulamasi.R
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.*

@Composable
fun SearchField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search by name"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_search), null, tint = Gray400)
        },
        placeholder = {
            Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = Gray400)
        },
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Outline,
            unfocusedBorderColor = Outline,
            focusedContainerColor = Gray50,
            unfocusedContainerColor = Gray50,
            cursorColor = BrandBlue
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}
