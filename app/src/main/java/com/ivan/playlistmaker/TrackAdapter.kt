package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(val userData: List<Track>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.song_unit, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as DataViewHolder
        holder.onBind(userData[position])
    }

    override fun getItemCount(): Int {
        return userData.size
    }
    class DataViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val coverView = itemView.findViewById<ImageView>(R.id.album_cover)
        private var songName = itemView.findViewById<TextView>(R.id.song_name)
        private var artistName = itemView.findViewById<TextView>(R.id.artist_name)


        @SuppressLint("SetTextI18n")
        fun onBind(track: Track) {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(2))
                .into(coverView)

            songName.text = track.trackName
            artistName.text = "${track.artistName} • ${track.trackTime}"
        }
    }
}


