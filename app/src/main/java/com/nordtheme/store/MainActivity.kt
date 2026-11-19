package com.nordtheme.store

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nordtheme.store.core.navigation.Routes
import com.nordtheme.store.core.theme.LocalNordPalette
import com.nordtheme.store.core.theme.NordThemeStoreTheme
import com.nordtheme.store.data.local.SeedCatalog
import com.nordtheme.store.data.local.StoreDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent {
            NordThemeStoreTheme(darkTheme = true) {
                NordThemeStoreApp()
            }
        }
    }
}

private enum class BottomTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(Routes.Home.route, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    Category(Routes.Category.route, "Category", Icons.Filled.GridView, Icons.Outlined.GridView),
    OMG(Routes.OMG.route, "OMG", Icons.Filled.ThumbUp, Icons.Outlined.ThumbUp),
    Me(Routes.Me.route, "Me", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
private fun NordThemeStoreApp() {
    val context = LocalContext.current
    val db = remember { StoreDatabase.getInstance(context) }
    val dao = remember { db.storeDao() }
    val palette = LocalNordPalette.current
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    LaunchedEffect(Unit) {
        if (dao.count() == 0) {
            dao.insertAllIgnore(SeedCatalog.initialItems())
        }
    }

    val allItems by dao.observeAll().collectAsState(initial = emptyList())
    val showBottomBar = currentRoute in setOf(
        Routes.Home.route,
        Routes.Category.route,
        Routes.OMG.route,
        Routes.Me.route
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
            .systemBarsPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.Home.route) {
                PlaceholderTab("Home", "${allItems.size} catalog items loaded from Room")
            }
            composable(Routes.Category.route) {
                PlaceholderTab("Category", "Themes & Fonts categories ready")
            }
            composable(Routes.OMG.route) {
                PlaceholderTab("Explore", "Curated collections ready")
            }
            composable(Routes.Me.route) {
                PlaceholderTab("Me", "Profile, Downloads & Favorites ready")
            }
        }

        if (showBottomBar) {
            FloatingGlassBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            )
        }
    }
}

@Composable
private fun PlaceholderTab(title: String, subtitle: String) {
    val palette = LocalNordPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = palette.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = palette.textMuted,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun FloatingGlassBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalNordPalette.current
    val pillShape = RoundedCornerShape(32.dp)
    Row(
        modifier = modifier
            .fillMaxWidth(0.80f)
            .height(62.dp)
            .clip(pillShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(palette.glassTop, palette.glassBottom)
                )
            )
            .border(0.75.dp, palette.glassBorder, pillShape)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = currentRoute == tab.route
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigate(tab.route) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.label,
                        tint = if (isSelected) palette.textPrimary else palette.textMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    if (tab == BottomTab.Me) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(palette.badgeRed)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = tab.label,
                    color = if (isSelected) palette.textPrimary else palette.textMuted,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}
