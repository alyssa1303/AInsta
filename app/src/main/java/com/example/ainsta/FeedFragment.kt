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
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class FeedFragment : Fragment() {

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

        postsRecyclerView = view.findViewById<RecyclerView>(R.id.postRecyclerView)

        adapter = PostAdapter(requireContext(), posts)
        postsRecyclerView.adapter = adapter
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Specify the object id
        query.include(Post.KEY_USER)
        // Return posts in descending order
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Post> {
            override fun done(allPosts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(MainActivity.TAG, "Error fetching posts!")
                    Toast.makeText(requireContext(), "Can't get posts", Toast.LENGTH_SHORT).show()
                } else {
                    if (allPosts != null) {
                        for (post in posts) {
                            Log.i(
                                MainActivity.TAG, "Post: " + post.getDescription() + " Username: " +
                                    post.getUser()?.username)
                        }

                        posts.addAll(allPosts)
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        })
    }

    companion object{
        const val TAG = "FeedFragment"
    }
}
