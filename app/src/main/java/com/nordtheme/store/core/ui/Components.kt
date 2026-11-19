package com.nordtheme.store.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nordtheme.store.core.theme.LocalNordPalette
import com.nordtheme.store.domain.model.ItemType
import com.nordtheme.store.domain.model.StoreItem

@Composable
fun SearchPillBar(hint: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val p = LocalNordPalette.current
    Row(
        modifier = modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(50))
            .background(p.surfacePill).clickable(onClick = onClick).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, "Search", tint = p.textMuted, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(hint, color = p.textMuted, fontSize = 15.sp, maxLines = 1)
    }
}

data class HeroBannerUi(
    val id: String, val badge: String?, val subtitle: String, val title: String,
    val topColor: Color, val bottomColor: Color, val isPhoneMockup: Boolean = false
)

@Composable
fun HeroBannerCarousel(banners: List<HeroBannerUi>, onBannerClick: (HeroBannerUi) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(banners, key = { it.id }) { b ->
            Box(
                modifier = Modifier.width(172.dp).height(228.dp).clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(listOf(b.topColor, b.bottomColor)))
                    .clickable { onBannerClick(b) }.padding(12.dp)
            ) {
                if (b.isPhoneMockup) {
                    Box(
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 6.dp)
                            .width(106.dp).height(148.dp).clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF1A2238), b.topColor, Color(0xFF6B4E9B))))
                            .border(2.dp, Color(0xFF0E0E10), RoundedCornerShape(16.dp))
                    ) {
                        Text("10:00", color = Color.White.copy(alpha = 0.85f), fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 18.dp))
                    }
                } else {
                    Box(
                        modifier = Modifier.align(Alignment.Center).padding(bottom = 26.dp).size(96.dp)
                            .clip(CircleShape).background(Color.White.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) { Text(b.title.take(3).uppercase(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold) }
                }
                b.badge?.let {
                    Box(Modifier.align(Alignment.TopStart).clip(RoundedCornerShape(6.dp)).background(Color(0x99222226)).padding(horizontal = 7.dp, vertical = 2.dp)) {
                        Text(it, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Column(Modifier.align(Alignment.BottomStart)) {
                    Text(b.subtitle, color = Color.White.copy(alpha = 0.88f), fontSize = 11.sp, maxLines = 1)
                    Text(b.title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}
