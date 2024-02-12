package metal.ezplay.android

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import metal.ezplay.android.xml.R as XmlR

sealed class Screen(val route: String, @StringRes val resourceId: Int) {

    abstract fun bottomNavIcon(): ImageVector

    object Library : Screen("library", XmlR.string.library_tab_nam) {

        override fun bottomNavIcon(): ImageVector = Icons.Filled.PlayArrow
    }
    object Search : Screen("search", XmlR.string.search_tab_name) {

        override fun bottomNavIcon(): ImageVector = Icons.Filled.Search
    }
}
