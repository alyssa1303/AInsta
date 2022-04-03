package com.example.ainsta

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var posts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        postsRecyclerView = view.findViewById<RecyclerView>(R.id.postRecyclerView)

        adapter = PostAdapter(requireContext(), posts)
        postsRecyclerView.adapter = adapter
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Specify the object id
        query.include(Post.KEY_USER)
        // Return posts in descending order
        query.setLimit(20)
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Post> {
            override fun done(allPosts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(MainActivity.TAG, "Error fetching posts!")
                    Toast.makeText(requireContext(), "Can't get posts", Toast.LENGTH_SHORT).show()
                } else {
                    if (allPosts != null) {
                        for (post in posts) {
                            Log.i( TAG, "Post: " + post.getDescription() + " Username: " +
                                    post.getUser()?.username)
                        }

                        posts.clear()
                        adapter.notifyDataSetChanged()

                        posts.addAll(allPosts)
                        adapter.notifyDataSetChanged()

                        swipeContainer.setRefreshing(false)

                    }
                }
            }
        })
    }

    companion object{
        const val TAG = "FeedFragment"
    }
}
