package com.stash.inbox.ui.capture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stash.inbox.ui.theme.AmbientLight
import com.stash.inbox.ui.theme.GlassIconButton
import com.stash.inbox.ui.theme.NeonAccent
import com.stash.inbox.ui.theme.PureBlack
import com.stash.inbox.ui.theme.obsidianGlass

@Composable
fun CaptureScreen(
    text: String,
    onTextChange: (String) -> Unit,
    onCommitNote: (String) -> Unit,
    onNavigateToVault: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val windowFocused = LocalWindowInfo.current.isWindowFocused
    // On a cold start the Compose view can exist before its window can accept IME requests.
    // Re-run when the window gains focus, including when returning from the vault.
    LaunchedEffect(windowFocused) {
        if (windowFocused) {
            withFrameNanos { }
            focusRequester.requestFocus()
            // Allow the focused text field to establish its input connection before showing the IME.
            withFrameNanos { }
            keyboard?.show()
        }
    }

    Box(Modifier.fillMaxSize().background(PureBlack).systemBarsPadding().imePadding()) {
        AmbientLight()
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("STASH", color = Color(0x99FFFFFF), fontFamily = FontFamily.Monospace, fontSize = 12.sp, letterSpacing = 4.sp)
                GlassIconButton(Icons.Outlined.Inventory2, "Open vault") {
                    keyboard?.hide()
                    onNavigateToVault()
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.weight(1f).fillMaxWidth().obsidianGlass().padding(20.dp)) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester).testTag("capture_editor"),
                    textStyle = TextStyle(color = Color.White, fontSize = 18.sp, lineHeight = 26.sp),
                    cursorBrush = SolidColor(NeonAccent),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxSize()) {
                            if (text.isEmpty()) Text("Capture...", color = Color(0x66FFFFFF), fontSize = 18.sp)
                            innerTextField()
                        }
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth().height(48.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("LOCAL // NOT ENCRYPTED", color = Color(0x99FFFFFF), fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                AnimatedVisibility(visible = text.isNotBlank(), enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()) {
                    Box(
                        Modifier.height(48.dp).clip(CircleShape).background(NeonAccent)
                            .clickable { onCommitNote(text.trim()) }.padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("STASH IT", color = PureBlack, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
        }
    }
}
