package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts.components

@Composable
fun EmptyState(onCreate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(R.drawable.ic_person), contentDescription = null,
            tint = Gray200, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(12.dp))
        Text("No Contacts", style = MaterialTheme.typography.titleMedium, color = Gray500)
        Spacer(Modifier.height(4.dp))
        Text("Contacts you’ve added will appear here.", style = MaterialTheme.typography.bodyMedium, color = Gray400)
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onCreate) {
            Text("Create New Contact", color = BrandBlue)
        }
    }
}
