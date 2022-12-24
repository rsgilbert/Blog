package net.passioncloud.blog

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amplifyframework.core.model.query.QueryOptions
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Post
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun PostApp(modifier: Modifier=Modifier) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Scaffold(
        scaffoldState=scaffoldState,
    ) {
        PostAppNavHost(navController = navController)
    }
}

enum class PostScreens {
    PostListScreen,
    PostDetailsScreen
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun PostAppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = PostScreens.PostListScreen.name, modifier = modifier) {
        composable(PostScreens.PostListScreen.name) {
            val onItemClick = fun(postId: String) {
                navController.navigate("${PostScreens.PostDetailsScreen.name}/$postId")
            }
            PostListScreen(onItemClick)
        }
        composable("${PostScreens.PostDetailsScreen.name}/{postId}",
            arguments=listOf(navArgument("postId") {
                type = NavType.StringType
            })) {  entry ->
            val postId = entry.arguments?.getString("postId")
            val post by Amplify.DataStore.query(Post::class, Where.matches(Post.ID.eq(postId))).collectAsState(
                initial = null
            )
            post?.let { p ->
                PostDetailsScreen(post = p)
            }
        }
    }

}

