package metal.ezplay.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
  viewModel: SearchViewModel,
  onRefreshList: () -> Unit
) {
  val currentSearchTerm = viewModel.searchTerm
  var textFieldValue by remember { mutableStateOf(TextFieldValue(currentSearchTerm)) }
  TextField(
    textFieldValue,
    onValueChange = {
      textFieldValue = it
      viewModel.search(textFieldValue.text)

      onRefreshList()
    },
    Modifier
      .wrapContentHeight()
      .fillMaxWidth(),
    placeholder = { Text("Search for a song…") },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(
      onSearch = {
        viewModel.search(textFieldValue.text)

        onRefreshList()
      },
    ),
    singleLine = true,
  )
}
