package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold { innerPadding ->
                FullScreenUI(
                    modifier = Modifier
                        .padding(innerPadding)

                )
            }
        }
    }
}



data class PostsResponse(
    val posts: List<Post>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val tags: List<String>,
    val reactions: Reactions,
    val views: Int,
    val userId: Int
)

data class Reactions(
    val likes: Int,
    val dislikes: Int
)



interface PostsApi {
    @GET("posts")
    suspend fun getAllPosts(): PostsResponse
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://dummyjson.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: PostsApi = retrofit.create(PostsApi::class.java)



@Composable
fun FullScreenUI(modifier: Modifier = Modifier) {

    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            val response = api.getAllPosts()
            posts = response.posts
        } catch (e: Exception) {
            posts = emptyList()
        } finally {
            isLoading = false
        }
    }

    Column(modifier = modifier.fillMaxSize()) {




        when {
            isLoading -> {
                Text("Loading...")
            }

            posts.isEmpty() -> {
                Text("No posts found")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    items(posts) { post ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text(
                                    text = post.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = post.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = "Tags: ${post.tags.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = "👍 ${post.reactions.likes}   👎 ${post.reactions.dislikes}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "👁 Views: ${post.views}   👤 User: ${post.userId}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullScreenPreview() {
    Scaffold { innerPadding ->
        FullScreenUI(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )
    }
}
