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
import com.nordtheme.store.features.category.CategoryScreen
import com.nordtheme.store.features.home.HomeScreen
import com.nordtheme.store.features.omg.ExploreScreen
import com.nordtheme.store.features.profile.MeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent { NordThemeStoreTheme(darkTheme = true) { NordThemeStoreApp() } }
    }
}

private enum class BottomTab(val route: String, val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    Home(Routes.Home.route, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    Category(Routes.Category.route, "Category", Icons.Filled.GridView, Icons.Outlined.GridView),
    OMG(Routes.OMG.route, "OMG", Icons.Filled.ThumbUp, Icons.Outlined.ThumbUp),
    Me(Routes.Me.route, "Me", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
private fun NordThemeStoreApp() {
    val context = LocalContext.current
    val dao = remember { StoreDatabase.getInstance(context).storeDao() }
    val p = LocalNordPalette.current
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    LaunchedEffect(Unit) { if (dao.count() == 0) dao.insertAllIgnore(SeedCatalog.initialItems()) }

    val entityList by dao.observeAll().collectAsState(initial = emptyList())
    val items = remember(entityList) { entityList.map { it.toDomain() } }
    val favCount = remember(items) { items.count { it.isFavorite } }
    val dlCount = remember(items) { items.count { it.isDownloaded } }
    val showBar = route in setOf(Routes.Home.route, Routes.Category.route, Routes.OMG.route, Routes.Me.route)

    Box(Modifier.fillMaxSize().background(p.background).systemBarsPadding()) {
        NavHost(navController = nav, startDestination = Routes.Home.route, modifier = Modifier.fillMaxSize()) {
            composable(Routes.Home.route) {
                HomeScreen(items, { nav.navigate(Routes.Search.route) },
                    { id -> nav.navigate(Routes.Detail.create(id)) },
                    { t, k -> nav.navigate(Routes.CategoryGrid.create(t, k)) })
            }
            composable(Routes.Category.route) {
                CategoryScreen({ nav.navigate(Routes.Search.route) }, { t, k -> nav.navigate(Routes.CategoryGrid.create(t, k)) })
            }
            composable(Routes.OMG.route) {
                ExploreScreen(items, { id -> nav.navigate(Routes.Detail.create(id)) },
                    { t, k -> nav.navigate(Routes.CategoryGrid.create(t, k)) })
            }
            composable(Routes.Me.route) {
                MeScreen(favCount, dlCount, { nav.navigate(Routes.Search.route) },
                    { nav.navigate(Routes.Downloads.route) }, { nav.navigate(Routes.Favorites.route) },
                    { nav.navigate(Routes.Settings.route) }, { t -> nav.navigate(Routes.CategoryGrid.create(t, t)) })
            }
            composable(Routes.Search.route) { SimpleSubScreen("Search", "Coming in Phase 4") { nav.popBackStack() } }
            composable(Routes.Downloads.route) { SimpleSubScreen("Downloads", "Coming in Phase 4") { nav.popBackStack() } }
            composable(Routes.Favorites.route) { SimpleSubScreen("Favorites", "Coming in Phase 4") { nav.popBackStack() } }
            composable(Routes.Settings.route) { SimpleSubScreen("Settings", "Coming in Phase 7") { nav.popBackStack() } }
            composable(Routes.Detail.route) { e -> SimpleSubScreen("Detail: ${e.arguments?.getString("itemId")}", "Coming in Phase 4") { nav.popBackStack() } }
            composable(Routes.CategoryGrid.route) { e -> SimpleSubScreen("${e.arguments?.getString("title")}", "Coming in Phase 4") { nav.popBackStack() } }
        }
        if (showBar) {
            FloatingGlassBottomBar(route, { r ->
                nav.navigate(r) { popUpTo(Routes.Home.route) { saveState = true }; launchSingleTop = true; restoreState = true }
            }, Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp))
        }
    }
}

@Composable
private fun SimpleSubScreen(title: String, subtitle: String, onBack: () -> Unit) {
    val p = LocalNordPalette.current
    Column(Modifier.fillMaxSize().background(p.background).padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text(title, color = p.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, color = p.textMuted, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Text("← Go Back", color = p.coralCta, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable(onClick = onBack))
    }
}

@Composable
private fun FloatingGlassBottomBar(currentRoute: String?, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    val p = LocalNordPalette.current
    val pill = RoundedCornerShape(32.dp)
    Row(
        modifier.fillMaxWidth(0.80f).height(62.dp).clip(pill)
            .background(Brush.verticalGradient(listOf(p.glassTop, p.glassBottom)))
            .border(0.75.dp, p.glassBorder, pill).padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically
    ) {
        BottomTab.entries.forEach { tab ->
            val sel = currentRoute == tab.route
            Column(Modifier.weight(1f).clickable(remember { MutableInteractionSource() }, null) { onNavigate(tab.route) },
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Box {
                    Icon(if (sel) tab.selectedIcon else tab.unselectedIcon, tab.label, tint = if (sel) p.textPrimary else p.textMuted, modifier = Modifier.size(22.dp))
                    if (tab == BottomTab.Me) Box(Modifier.size(6.dp).clip(CircleShape).background(p.badgeRed).align(Alignment.TopEnd))
                }
                Spacer(Modifier.height(3.dp))
                Text(tab.label, color = if (sel) p.textPrimary else p.textMuted, fontSize = 10.sp, fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal)
            }
        }
    }
}
