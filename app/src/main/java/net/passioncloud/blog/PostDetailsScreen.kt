package net.passioncloud.blog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.amplifyframework.datastore.generated.model.Post

@Composable
fun PostDetailsScreen(post: Post) {
    Column {
        Text(text = "Title")
        Text(text = post.title)
        Text(text = "Content")
        Text(text = post.content)
    }
}

