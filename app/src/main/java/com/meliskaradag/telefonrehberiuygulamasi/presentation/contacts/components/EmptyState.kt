package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.meliskaradag.telefonrehberiuygulamasi.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.BrandBlue
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.Gray100
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.Gray500
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.Gray900

@Composable
fun EmptyState(
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Gray100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_person),
                contentDescription = null,
                tint = BrandBlue
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "No Contacts",
            style = MaterialTheme.typography.titleLarge,
            color = Gray900
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Contacts you’ve added will appear here.",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray500
        )
        Spacer(Modifier.height(14.dp))
        Button(onClick = onCreate) {
            Text(
                "Create New Contact",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandBlue
            )
        }
    }
}
