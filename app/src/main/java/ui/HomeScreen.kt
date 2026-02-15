@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.a438_project1.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.a438_project1.ItunesApi
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    headerText: String = "Are You a Fan?",
    dropdownLabel: String = "Select artist to begin quiz",
    itunesApi: ItunesApi,
    onStartGame: (String) -> Unit
) {
    var selectedArtist by remember { mutableStateOf<String?>(null) }

    var artists by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }

    // ✅ custom dropdown state
    var showDropdown by remember { mutableStateOf(false) }
    var fieldRect by remember { mutableStateOf<Rect?>(null) }
    val density = LocalDensity.current

    // ✅ Limit genres here
    val seeds = remember { listOf("pop", "hip hop", "rock") }

    // Load artists
    LaunchedEffect(reloadKey) {
        try {
            isLoading = true
            error = null

            val set = linkedSetOf<String>()
            for (seed in seeds) {
                val res = itunesApi.searchSongs(term = seed)
                res.results
                    .mapNotNull { it.artistName }
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .forEach { set.add(it) }
            }

            artists = set.toList().sorted()
            selectedArtist = null

            Log.d("HomeScreen", "Loaded ${artists.size} artists")
        } catch (e: Exception) {
            Log.e("HomeScreen", "Artist load failed", e)
            error = e.toString()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = headerText, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        Text(text = dropdownLabel, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))

        // ✅ Clickable wrapper so taps ALWAYS open the dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coords ->
                    val pos = coords.positionInWindow()
                    val size = coords.size
                    fieldRect = Rect(
                        pos.x,
                        pos.y,
                        pos.x + size.width,
                        pos.y + size.height
                    )
                    fieldRect = Rect(pos.x, pos.y, pos.x + size.width, pos.y + size.height)
                }
                .clickable(enabled = !isLoading && artists.isNotEmpty()) {
                    showDropdown = true
                }
        ) {
            // Disabled field (so Box receives click), but styled like normal
            OutlinedTextField(
                value = selectedArtist ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                placeholder = { Text("Tap to choose") },
                trailingIcon = { Text("▾") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // ✅ Custom anchored popup dropdown (scrollable + stable)
        if (showDropdown && fieldRect != null) {
            val rect = fieldRect!!
            val menuWidthDp = with(density) { rect.width.toDp() }

            Popup(
                offset = IntOffset(
                    x = rect.left.roundToInt(),
                    y = rect.bottom.roundToInt()
                ),
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { showDropdown = false }
            ) {
                Surface(
                    tonalElevation = 4.dp,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.width(menuWidthDp)
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 320.dp)
                    ) {
                        items(artists) { artist ->
                            ListItem(
                                headlineContent = { Text(artist) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedArtist = artist
                                        showDropdown = false
                                    }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(10.dp))
                Text("Loading artists...")
            }
        }

        if (error != null) {
            Spacer(Modifier.height(10.dp))
            Text("Error: $error")
            Spacer(Modifier.height(8.dp))
            Button(onClick = { reloadKey++ }) { Text("Retry") }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text("  OR  ")
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (artists.isNotEmpty()) {
                    selectedArtist = artists.random()
                }
            },
            enabled = !isLoading && artists.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Randomly Generate Artist")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { selectedArtist?.let(onStartGame) },
            enabled = selectedArtist != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }
    }
}
