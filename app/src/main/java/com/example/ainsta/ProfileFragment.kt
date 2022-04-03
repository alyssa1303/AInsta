package com.example.ainsta

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {


    override fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Specify the object id
        query.include(Post.KEY_USER)
        // Only return posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // Return posts in descending order
        query.addDescendingOrder("createdAt")
        // Only return most 20 recent posts
        query.setLimit(20)

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

                        posts.addAll(allPosts)
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        })
    }

}