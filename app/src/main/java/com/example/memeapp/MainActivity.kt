 package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject

 class MainActivity : AppCompatActivity() {


     var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadMeme()
    }

     private fun LoadMeme(){
         (findViewById<ProgressBar>(R.id.progressBar)).visibility = View.VISIBLE
         //val queue = Volley.newRequestQueue(this)
         //val url = "https://www.google.com"
         val url = "https://meme-api.herokuapp.com/gimme"
         val JsonObjectRequest = JsonObjectRequest(
             Request.Method.GET, url, null,
             { response ->
                 currentImageUrl = response.getString("url")
                 Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean
                     ): Boolean {
                         findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                         return false
                     }

                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {
                         findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                         return false
                     }
                 }).into(findViewById<ImageView>(R.id.memeImageView))
             },
             {
                 Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

             })

// Add the request to the RequestQueue.
         MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)
         //queue.add(JsonObjectRequest)
     }

     fun shareMeme(view: View) {
         val intent = Intent(Intent.ACTION_SEND)
         intent.type = "text/plain"
         intent.putExtra(Intent.EXTRA_TEXT,"Hey check out this meme I got from reddit $currentImageUrl")
         val chooser = Intent.createChooser(intent, "Share this meme using...")
         startActivity(chooser)
     }
     fun nextMeme(view: View) {
         LoadMeme()
     }
 }