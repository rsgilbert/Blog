package net.passioncloud.blog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amplifyframework.datastore.generated.model.Post
import net.passioncloud.blog.ui.theme.MyShapes

@Composable
fun PostDetailsScreen(post: Post) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 16.dp)) {
        item {
            MyTextBox(value = post.title, label="Title")
            MyTextBox(value = post.content, label="Content")
        }
    }
}



@Composable
fun MyTextBox(
    value: String,
    label: String? = null,
    readOnly: Boolean = false,
    onValueChange: (newValue: String) -> Unit = {}
) {
    label?.let { Text(it, modifier = Modifier.padding(vertical = 4.dp)) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 2.dp, color = Color.Black),
                shape = MyShapes.editTextShape
            )
            .clip(MyShapes.editTextShape)
            .background(color = MaterialTheme.colors.onSurface)

    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly
        )
    }
}