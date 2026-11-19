package com.nordtheme.store.features.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nordtheme.store.core.theme.LocalNordPalette
import com.nordtheme.store.core.ui.SearchPillBar

private data class ColorTile(val label: String, val top: Color, val bottom: Color, val icon: ImageVector)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryScreen(
    onSearchClick: () -> Unit,
    onCategoryClick: (String, String) -> Unit
) {
    val palette = LocalNordPalette.current
    var activeSection by remember { mutableStateOf("Themes") }

    val colorTiles = remember {
        listOf(
            ColorTile("White", Color(0xFFD5DCE4), Color(0xFF9EA9B6), Icons.Outlined.Diamond),
            ColorTile("Black", Color(0xFF42454B), Color(0xFF1F2124), Icons.Outlined.School),
            ColorTile("Yellow", Color(0xFFFFE57F), Color(0xFFFFCA28), Icons.Outlined.WbSunny),
            ColorTile("Red", Color(0xFFFFAB91), Color(0xFFFF7043), Icons.Outlined.FavoriteBorder),
            ColorTile("Purple", Color(0xFFD1B3FF), Color(0xFF9575CD), Icons.Outlined.FilterVintage),
            ColorTile("Blue", Color(0xFF81D4FA), Color(0xFF29B6F6), Icons.Outlined.Public),
            ColorTile("Green", Color(0xFF80E2A7), Color(0xFF2ECC71), Icons.Outlined.Eco),
            ColorTile("Pink", Color(0xFFFFB2D1), Color(0xFFFF80AB), Icons.Outlined.LocalFlorist),
            ColorTile("Colourful", Color(0xFFA7FFEB), Color(0xFF82B1FF), Icons.Outlined.Palette)
        )
    }

    val tagChips = listOf(
        "iphone" to Color(0xFF5C6BC0), "love" to Color(0xFF66BB6A), "anime" to Color(0xFFFFB300),
        "car" to Color(0xFFE65100), "god" to Color(0xFFE53935), "panda" to Color(0xFF29B6F6),
        "dark" to Color(0xFF7E57C2), "ios" to Color(0xFF26C6DA), "black" to Color(0xFF9CCC65),
        "cute" to Color(0xFFFFCA28)
    )
