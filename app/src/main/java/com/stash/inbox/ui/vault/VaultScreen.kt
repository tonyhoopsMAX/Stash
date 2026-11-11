package com.stash.inbox.ui.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stash.inbox.data.Note
import com.stash.inbox.ui.theme.AmbientLight
import com.stash.inbox.ui.theme.GlassIconButton
import com.stash.inbox.ui.theme.NeonAccent
import com.stash.inbox.ui.theme.PureBlack
import com.stash.inbox.ui.theme.obsidianGlass
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val timestampFormat = DateTimeFormatter.ofPattern("dd MMM yyyy // HH:mm 'UTC'", Locale.US).withZone(ZoneOffset.UTC)

@Composable
fun VaultScreen(notes: List<Note>, onBack: () -> Unit, onSecurity: () -> Unit, onDelete: (String) -> Unit) {
    Box(Modifier.fillMaxSize().background(PureBlack).systemBarsPadding()) {
        AmbientLight()
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                GlassIconButton(Icons.AutoMirrored.Outlined.ArrowBack, "Back to capture", onBack)
                Text("THE VAULT", color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                GlassIconButton(Icons.Outlined.Lock, "Security options", onSecurity)
            }
            if (notes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nothing stashed yet.", color = Color(0x99FFFFFF), fontFamily = FontFamily.Monospace)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(notes, key = { it.id }, contentType = { "NoteItem" }) { note ->
                        DismissibleNote(note = note, onDelete = { onDelete(note.id) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissibleNote(note: Note, onDelete: () -> Unit) {
    val state = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value != SwipeToDismissBoxValue.Settled) {
            onDelete()
            true
        } else false
    })
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(Modifier.fillMaxSize().background(Color(0xFF3A1010), RoundedCornerShape(16.dp)).padding(horizontal = 20.dp), contentAlignment = Alignment.CenterEnd) {
                Icon(Icons.Outlined.DeleteOutline, contentDescription = "Delete note", tint = Color.White)
            }
        }
    ) {
        Column(Modifier.fillMaxWidth().obsidianGlass(RoundedCornerShape(16.dp)).semantics {
            customActions = listOf(CustomAccessibilityAction("Delete note") { onDelete(); true })
        }.padding(16.dp)) {
            Text(timestampFormat.format(Instant.ofEpochMilli(note.createdAt)), color = NeonAccent, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            Text(note.content, color = Color(0xE6FFFFFF), fontSize = 16.sp, lineHeight = 24.sp)
        }
    }
}
