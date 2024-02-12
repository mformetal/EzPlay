package metal.ezplay.android

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import metal.ezplay.android.xml.R as XmlR

@Composable
fun BottomBar(navController: NavController) {
    BottomNavigation {
        LibraryItem(navController)
        SearchItem(navController)
    }
}

@Composable
private fun RowScope.LibraryItem(navController: NavController) {
    BottomNavigationItem(
        icon = { Icon(Icons.Filled.PlayArrow, contentDescription = null) },
        label = { Text(stringResource(XmlR.string.library_tab_nam)) },
        selected = true,
        onClick = {
            navController.navigate("library")
        }
    )
}

@Composable
private fun RowScope.SearchItem(navController: NavController) {
    BottomNavigationItem(
        icon = { Icon(Icons.Filled.Search, contentDescription = null) },
        label = { Text(stringResource(XmlR.string.search_tab_name)) },
        selected = true,
        onClick = {
            navController.navigate("search")
        }
    )
}