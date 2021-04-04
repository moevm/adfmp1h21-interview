package com.pjs.itinterviewtrainer

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley


object VolleyWebService {
    lateinit var requestQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

    fun setupRequestQueue(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
        imageLoader = ImageLoader(requestQueue, object : ImageLoader.ImageCache {
            private val mCache: LruCache<String, Bitmap> = LruCache<String, Bitmap>(10)
            override fun putBitmap(url: String, bitmap: Bitmap) {
                mCache.put(url, bitmap)
            }

            override fun getBitmap(url: String): Bitmap? {
                return mCache.get(url)
            }
        })
    }
}