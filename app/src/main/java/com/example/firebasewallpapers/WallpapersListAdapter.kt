package com.example.firebasewallpapers

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_single_item.view.*

class WallpapersListAdapter(
    var wallpapersList: List<WallpapersModel>, private val clickListener: (WallpapersModel)
    -> Unit) : RecyclerView.Adapter<WallpapersListAdapter.WallpapersViewHolder>() {

    class WallpapersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(wallpapers: WallpapersModel, clickLister: (WallpapersModel) -> Unit) {
            //Load the Image
            Glide.with(itemView.context)
                .load(wallpapers.thumbnail)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.list_single_progress.visibility = View.GONE
                        return false
                    }

                })
                .into(itemView.list_single_image)

            //Click Listener
            itemView.setOnClickListener {
                clickLister(wallpapers)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_single_item, parent, false)
        return WallpapersViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) {
        holder.bind(wallpapersList[position], clickListener)
    }

    override fun getItemCount(): Int = wallpapersList.size


}