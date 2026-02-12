package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    headerText: String = "Are You a Fan?",
    dropdownLabel: String = "Select artist to begin quiz",
    //TODO: need to pass API results into this list
    artistOptions: List<String> = emptyList(),
    onArtistSelected: (artist: String) -> Unit = {},
    onRandom: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedArtist by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Header options
        Text(
            text = headerText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Dropdown for artist names (Exposed)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedArtist,
                onValueChange = {}, // read-only; selection will come from menu
                readOnly = true,
                label = { Text(dropdownLabel) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (artistOptions.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Loading artists…", style = MaterialTheme.typography.bodyMedium) },
                        onClick = { expanded = false },
                        enabled = false
                    )
                } else {
                    artistOptions.forEach { artist ->
                        DropdownMenuItem(
                            text = { Text(artist) },
                            onClick = {
                                selectedArtist = artist
                                expanded = false
                                onArtistSelected(artist)
                            }
                        )
                    }
                }
            }
        }

        //'or' middle section
        Spacer(Modifier.height(288.dp))

        Text(
            text = "──────── OR ────────",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(288.dp))


        // Random button
        // TODO: need to add API functionality in viewModel for random btn
        Button(
            onClick = onRandom,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Randomly Generate Artist")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        //also needs to be replaced with actual dropdown
        artistOptions = listOf("Taylor Swift", "Drake", "Bad Bunny", "The Weeknd", "Ed Sheeran"),
        onArtistSelected = {},
        onRandom = {}
    )
}
