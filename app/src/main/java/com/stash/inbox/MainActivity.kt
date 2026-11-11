package com.stash.inbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.stash.inbox.data.NoteStore
import com.stash.inbox.ui.capture.CaptureScreen
import com.stash.inbox.ui.paywall.PaywallScreen
import com.stash.inbox.ui.theme.StashTheme
import com.stash.inbox.ui.vault.VaultScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { StashTheme { StashApp() } }
    }
}

private enum class Screen { CAPTURE, VAULT, PAYWALL }

@Composable
private fun StashApp() {
    val context = LocalContext.current
    val store = remember { NoteStore(context) }
    val notes = remember { mutableStateListOf(*store.load().toTypedArray()) }
    var screen by rememberSaveable { mutableStateOf(Screen.CAPTURE) }
    // Keep an unfinished capture when visiting the vault or recreating the activity.
    var draft by rememberSaveable { mutableStateOf("") }

    BackHandler(enabled = screen != Screen.CAPTURE) {
        screen = if (screen == Screen.PAYWALL) Screen.VAULT else Screen.CAPTURE
    }
    when (screen) {
        Screen.CAPTURE -> CaptureScreen(
            text = draft,
            onTextChange = { draft = it },
            onCommitNote = { text ->
                if (text.isNotBlank()) {
                    notes.add(0, store.newNote(text))
                    store.save(notes)
                    draft = ""
                }
            },
            onNavigateToVault = { screen = Screen.VAULT }
        )
        Screen.VAULT -> VaultScreen(
            notes = notes,
            onBack = { screen = Screen.CAPTURE },
            onSecurity = { screen = Screen.PAYWALL },
            onDelete = { id ->
                notes.removeAll { it.id == id }
                store.save(notes)
            }
        )
        Screen.PAYWALL -> PaywallScreen(
            onBack = { screen = Screen.VAULT },
            onUnlock = {},
            onRestore = {}
        )
    }
}
