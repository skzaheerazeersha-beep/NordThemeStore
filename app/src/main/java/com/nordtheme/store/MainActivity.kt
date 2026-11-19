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

    val entityList by dao.observeAll().collectAsState(initial = emptyList())
    val storeItems = remember(entityList) { entityList.map { it.toDomain() } }
    val favoritesCount = remember(storeItems) { storeItems.count { it.isFavorite } }
    val downloadsCount = remember(storeItems) { storeItems.count { it.isDownloaded } }

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
                HomeScreen(
                    items = storeItems,
                    onSearchClick = { navController.navigate(Routes.Search.route) },
                    onItemClick = { id -> navController.navigate(Routes.Detail.create(id)) },
                    onSeeAllClick = { title, key -> navController.navigate(Routes.CategoryGrid.create(title, key)) }
                )
            }
            composable(Routes.Category.route) {
                CategoryScreen(
                    onSearchClick = { navController.navigate(Routes.Search.route) },
                    onCategoryClick = { title, key -> navController.navigate(Routes.CategoryGrid.create(title, key)) }
                )
            }
            composable(Routes.OMG.route) {
                ExploreScreen(
                    items = storeItems,
                    onItemClick = { id -> navController.navigate(Routes.Detail.create(id)) },
                    onSeeAllClick = { title, key -> navController.navigate(Routes.CategoryGrid.create(title, key)) }
                )
            }
            composable(Routes.Me.route) {
                MeScreen(
                    favoritesCount = favoritesCount,
                    downloadsCount = downloadsCount,
                    onSearchClick = { navController.navigate(Routes.Search.route) },
                    onDownloadsClick = { navController.navigate(Routes.Downloads.route) },
                    onFavoritesClick = { navController.navigate(Routes.Favorites.route) },
                    onSettingsClick = { navController.navigate(Routes.Settings.route) },
                    onResourceClick = { title -> navController.navigate(Routes.CategoryGrid.create(title, title)) }
                )
            }
            composable(Routes.Search.route) {
                SimpleSubScreen("Search", "Phase 4 will add live search & Trending/Climbing boards") {
                    navController.popBackStack()
                }
            }
            composable(Routes.Downloads.route) {
                SimpleSubScreen("Downloads & Purchased", "Phase 4 will list offline downloads") {
                    navController.popBackStack()
                }
            }
            composable(Routes.Favorites.route) {
                SimpleSubScreen("Favorites", "Phase 4 will list saved items") {
                    navController.popBackStack()
                }
            }
            composable(Routes.Settings.route) {
                SimpleSubScreen("Settings", "Phase 7 will add ColorOS 17 compatibility & theme toggle") {
                    navController.popBackStack()
                }
            }
            composable(Routes.Detail.route) { entry ->
                val id = entry.arguments?.getString("itemId").orEmpty()
                SimpleSubScreen("Detail: $id", "Phase 4 will add full-screen preview pager & Buy/Apply bar") {
                    navController.popBackStack()
                }
            }
            composable(Routes.CategoryGrid.route) { entry ->
                val title = entry.arguments?.getString("title").orEmpty()
                SimpleSubScreen(title, "Phase 4 will add the 3-column grid") {
                    navController.popBackStack()
                }
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
private fun SimpleSubScreen(title: String, subtitle: String, onBack: () -> Unit) {
    val palette = LocalNordPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, color = palette.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = subtitle, color = palette.textMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "← Go Back",
            color = palette.coralCta,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onBack)
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
