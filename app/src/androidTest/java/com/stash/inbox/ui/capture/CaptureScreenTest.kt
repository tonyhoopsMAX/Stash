package com.stash.inbox.ui.capture

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stash.inbox.ui.theme.StashTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CaptureScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun editorTakesFocusAndCommitsText() {
        var draft by mutableStateOf("")
        var committed = ""
        compose.setContent {
            StashTheme {
                CaptureScreen(
                    text = draft,
                    onTextChange = { draft = it },
                    onCommitNote = { committed = it; draft = "" },
                    onNavigateToVault = {}
                )
            }
        }

        compose.waitUntil(timeoutMillis = 5_000) {
            compose.onAllNodes(hasTestTag("capture_editor") and isFocused())
                .fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithTag("capture_editor").assertIsFocused().performTextInput("A thought")
        compose.onNodeWithText("STASH IT").performClick()
        compose.runOnIdle {
            assertEquals("A thought", committed)
            assertEquals("", draft)
        }
        compose.onNodeWithText("STASH IT").assertDoesNotExist()
    }

    @Test
    fun vaultButtonInvokesNavigation() {
        var navigated = false
        compose.setContent {
            StashTheme {
                CaptureScreen("", {}, {}, { navigated = true })
            }
        }
        compose.onNodeWithContentDescription("Open vault").performClick()
        compose.runOnIdle { assertTrue(navigated) }
    }
}
