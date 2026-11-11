package com.stash.inbox.ui.paywall

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stash.inbox.ui.theme.AmbientLight
import com.stash.inbox.ui.theme.GlassIconButton
import com.stash.inbox.ui.theme.NeonAccent
import com.stash.inbox.ui.theme.PureBlack
import com.stash.inbox.ui.theme.obsidianGlass

enum class AccessTier { LIFETIME, ANNUAL }

/** Presentation only: enable purchase and restore only after real integrations are supplied. */
@Composable
fun PaywallScreen(
    onBack: () -> Unit,
    onUnlock: (AccessTier) -> Unit,
    onRestore: () -> Unit,
    purchasesAvailable: Boolean = false
) {
    var selected by rememberSaveable { mutableStateOf(AccessTier.LIFETIME) }
    val transition = rememberInfiniteTransition(label = "ambient light")
    val glow by transition.animateFloat(
        initialValue = 0.08f, targetValue = 0.14f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse), label = "glow"
    )
    Box(Modifier.fillMaxSize().background(PureBlack).systemBarsPadding()) {
        AmbientLight(centered = true, alpha = glow)
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(16.dp)) {
                GlassIconButton(Icons.AutoMirrored.Outlined.ArrowBack, "Back to vault", onBack)
            }
            Box(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 24.dp), contentAlignment = Alignment.Center) {
                Column(
                    Modifier.fillMaxWidth(0.88f).obsidianGlass(RoundedCornerShape(32.dp)).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.size(64.dp).obsidianGlass(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Fingerprint, contentDescription = null, tint = NeonAccent, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("ENCRYPTED MEMORY", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Text("A private vault, designed for local-only access.", color = Color(0x99FFFFFF), textAlign = TextAlign.Center, lineHeight = 22.sp)
                    Spacer(Modifier.height(24.dp))
                    Column(Modifier.fillMaxWidth().selectableGroup(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        TierRow("Lifetime Vault // \$14.99", selected == AccessTier.LIFETIME) { selected = AccessTier.LIFETIME }
                        TierRow("Annual Access // \$4.99/yr", selected == AccessTier.ANNUAL) { selected = AccessTier.ANNUAL }
                    }
                    Spacer(Modifier.height(24.dp))
                    Box(
                        Modifier.fillMaxWidth().height(56.dp)
                            .background(if (purchasesAvailable) NeonAccent else NeonAccent.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                            .clickable(enabled = purchasesAvailable, role = Role.Button) { onUnlock(selected) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AUTHENTICATE & UNLOCK", color = PureBlack, fontWeight = FontWeight.Black, fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                    TextButton(onClick = onRestore, enabled = purchasesAvailable) {
                        Text("Restore Hardware Key", color = if (purchasesAvailable) Color(0x99FFFFFF) else Color(0x4DFFFFFF), fontSize = 12.sp)
                    }
                    if (!purchasesAvailable) {
                        Text(
                            "Preview only. Purchases, biometrics and encryption are not implemented; notes are stored unencrypted on this device.",
                            color = Color(0xB3FFFFFF), fontSize = 12.sp, lineHeight = 17.sp, textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TierRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().obsidianGlass(RoundedCornerShape(16.dp))
            .border(1.dp, if (selected) NeonAccent else Color(0x1AFFFFFF), RoundedCornerShape(16.dp))
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(10.dp).background(if (selected) NeonAccent else Color(0x4DFFFFFF), CircleShape))
        Text(label, Modifier.padding(start = 12.dp), color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
    }
}
