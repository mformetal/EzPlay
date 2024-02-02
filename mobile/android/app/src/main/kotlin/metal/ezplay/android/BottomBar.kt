package metal.ezplay.android

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import metal.ezplay.android.xml.R as XmlR

@Composable
fun BottomBar(
    navController: NavController,
    content: @Composable (Modifier) -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text(stringResource(XmlR.string.home_tab_name)) },
                    selected = true,
                    onClick = {

                    }
                )
            }
        }, content = {
            content(Modifier.padding(bottom = it.calculateBottomPadding()))
        })
}