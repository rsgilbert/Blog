package net.passioncloud.blog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.model.query.ObserveQueryOptions
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.generated.model.PostStatus
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@Composable
fun PostListScreen(onItemClick: (String)->Unit) {
    val coroutineScope = rememberCoroutineScope()
    val posts = remember { mutableStateListOf<Post>() }
    com.amplifyframework.core.Amplify.DataStore.observeQuery(
        Post::class.java, ObserveQueryOptions(), {},
        {
            posts.clear()
            posts.addAll(it.items)
        }, {}, {})

    Column {
        LazyColumn {
            items(items = posts, key = Post::getId) { post ->
                PostItem(post = post, onItemClick=onItemClick)
            }
        }
        Button(onClick = { createNewPost(coroutineScope) }) {
            Text(text = "CREATE")
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostItem(post: Post, onItemClick: (String) -> Unit, modifier: Modifier = Modifier) {
    ListItem(modifier = modifier.clickable { onItemClick(post.id) }.padding(8.dp),
        text = {
            Text(text = post.title)
        },
        secondaryText = {
            Text(text = post.content)
        }
    )
    Divider()
}


fun createNewPost(coroutineScope: CoroutineScope) {
    val post = Post.builder()
        .title("Test post")
        .status(PostStatus.ACTIVE)
        .content("This is sample content")
        .build()
    coroutineScope.launch {
        Amplify.DataStore.save(post)
    }
}





