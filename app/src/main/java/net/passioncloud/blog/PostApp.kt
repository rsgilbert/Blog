package net.passioncloud.blog

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.datastore.generated.model.PostStatus
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun PostApp(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Blog")
                },
                actions = {
                    PostActions(navController = navController)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                createNewPost(scope)
            }) {
                Image(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        PostAppNavHost(modifier = modifier, navController = navController)
    }
}

enum class PostScreens {
    PostListScreen,
    PostDetailsScreen
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun PostAppNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    NavHost(
        navController = navController,
        startDestination = PostScreens.PostListScreen.name,
        modifier = modifier
    ) {
        composable(PostScreens.PostListScreen.name) {
            val onItemClick = fun(postId: String) {
                navController.navigate("${PostScreens.PostDetailsScreen.name}/$postId")
            }
            PostListScreen(onItemClick)
        }
        composable(
            "${PostScreens.PostDetailsScreen.name}/{postId}",
            arguments = listOf(navArgument("postId") {
                type = NavType.StringType
            })
        ) { entry ->
            val postId = entry.arguments?.getString("postId")
            val post by Amplify.DataStore.query(Post::class, Where.matches(Post.ID.eq(postId)))
                .collectAsState(
                    initial = null
                )
            post?.let { p ->
                PostDetailsScreen(post = p)
            }
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun PostActions(navController: NavHostController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    val scope = rememberCoroutineScope()


    Row(modifier = Modifier.padding(end = 16.dp)) {
        when (currentRoute) {
            "${PostScreens.PostDetailsScreen.name}/{postId}" -> {
                val postId = backStackEntry.value?.arguments?.get("postId").toString()
                Button(onClick = {
                    Log.d("PostApp", "deleting post $postId")
                    scope.launch {
                        Amplify.DataStore.query(Post::class, Where.matches(Post.ID.eq(postId)))
                            .collect {
                                Amplify.DataStore.delete(it)
                                navController.popBackStack()
                            }
                    }
                }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}



fun createNewPost(coroutineScope: CoroutineScope) {
    val post = Post.builder()
        .title("Test post")
        .status(PostStatus.ACTIVE)
        .rating(5)
        .content("This is sample content")
        .build()
    coroutineScope.launch {
        Amplify.DataStore.save(post)
    }
}


