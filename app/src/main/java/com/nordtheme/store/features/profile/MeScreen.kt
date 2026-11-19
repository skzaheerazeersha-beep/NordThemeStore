        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)).background(palette.surfaceCard).padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("My resources", color = palette.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("All >", color = palette.textMuted, fontSize = 13.sp, modifier = Modifier.clickable { onDownloadsClick() })
                }
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ResourceBadge(Icons.Outlined.AutoFixHigh, "Themes", Color(0xFFFF3B30)) { onResourceClick("Themes") }
                    ResourceBadge(Icons.Outlined.TextFields, "Fonts", Color(0xFFFF8C00)) { onResourceClick("Font") }
                    ResourceBadge(Icons.Outlined.Wallpaper, "Wallpapers", Color(0xFFFFB300)) { onResourceClick("Wallpapers") }
                    ResourceBadge(Icons.Outlined.PlayCircleFilled, "Live\nwallpaper", Color(0xFF007AFF)) { onResourceClick("Motion wallpapers") }
                }
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    ResourceBadge(Icons.Outlined.Notifications, "Video\nringtone", Color(0xFFFF453A)) { onResourceClick("Video ringtones") }
                    Spacer(Modifier.width(28.dp))
                    ResourceBadge(Icons.Outlined.Widgets, "Icon packs", Color(0xFF34C759)) { onResourceClick("Icons") }
                }
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)).background(palette.surfaceCard).padding(16.dp)
            ) {
                Text("My services", color = palette.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    QuickActionItem(Icons.Outlined.MonetizationOn, "Orders") { onDownloadsClick() }
                    QuickActionItem(Icons.Outlined.ConfirmationNumber, "Coupons") { onResourceClick("Coupons") }
                    QuickActionItem(Icons.Outlined.HeadsetMic, "Support") { onSettingsClick() }
                }
            }
        }
        item { RankPromoRow(onRankClick = { onResourceClick("Top Ranked") }, onFeaturedClick = { onResourceClick("Collections") }) }
    }
}

@Composable
private fun TopCircleBtn(icon: ImageVector, desc: String, onClick: () -> Unit) {
    val p = LocalNordPalette.current
    Box(Modifier.size(38.dp).clip(CircleShape).background(p.surfaceCard).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Icon(icon, desc, tint = p.textPrimary, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    val p = LocalNordPalette.current
    Column(Modifier.clickable(onClick = onClick).padding(horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = p.textPrimary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(6.dp))
        Text(label, color = p.textMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ResourceBadge(icon: ImageVector, label: String, bg: Color, onClick: () -> Unit) {
    val p = LocalNordPalette.current
    Column(Modifier.width(72.dp).clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(bg), contentAlignment = Alignment.Center) {
            Icon(icon, label, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, color = p.textPrimary, fontSize = 11.sp, lineHeight = 14.sp, textAlign = TextAlign.Center)
    }
}
