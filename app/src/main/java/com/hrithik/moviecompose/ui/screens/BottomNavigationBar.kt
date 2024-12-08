package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hrithik.moviecompose.viewModel.BottomNavigationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BottomNavigationBar(
    selectedIndex: Int, onItemSelected: (Int) -> Unit,
    viewModel: BottomNavigationViewModel = koinViewModel()
) {
    val favoritesCount by viewModel.favoritesCount.collectAsState()

    val items = listOf(
        BottomNavigationItem(
            title = "Movies",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            notification = false
        ),
        BottomNavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            notification = false,
        ),
        BottomNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite,
            notification = favoritesCount > 0,
            badgeCount = favoritesCount
        )
    )

    NavigationBar(containerColor = Color(0xFF1F2A44)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.notification && item.badgeCount != null) {
                                Badge(
                                    modifier = Modifier.padding(4.dp),
                                    contentColor = Color.White,
                                    containerColor = Color.Red
                                ) {
                                    Text(
                                        text = item.badgeCount.toString(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = if (selectedIndex == index) Color.Red else Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (selectedIndex == index) Color.Red else Color.Gray
                    )
                }
            )
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val notification: Boolean,
    val badgeCount: Int? = null
)
