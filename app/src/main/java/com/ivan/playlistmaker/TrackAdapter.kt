package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(val userData: List<Track>, private val showHeaderFooter: Boolean = false) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onTrackClick: ((Track) -> Unit)? = null
    var onClearHistoryClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_TRACK -> {
                DataViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.song_unit, parent, false)
                )

            }

            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.search_history_header, parent, false)
                )
            }

            VIEW_TYPE_FOOTER -> {
                FooterViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.search_history_footer, parent, false)
                ){
                    onClearHistoryClick?.invoke()
                }
            }

            else -> {}
        } as RecyclerView.ViewHolder


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val trackPosition = if (showHeaderFooter) {
            position - 1
        } else {
            position
        }
        when (getItemViewType(position)) {
            VIEW_TYPE_TRACK -> {
                holder as DataViewHolder
                holder.onBind(userData[trackPosition])
                holder.itemView.setOnClickListener {
                    onTrackClick?.invoke(userData[trackPosition])
                }
            }

            VIEW_TYPE_HEADER -> {

            }

            VIEW_TYPE_FOOTER -> {

            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!showHeaderFooter) {
            return VIEW_TYPE_TRACK
        }
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        if (position == itemCount - 1) {
            return VIEW_TYPE_FOOTER
        }
        return VIEW_TYPE_TRACK
    }

    override fun getItemCount(): Int {
        return if (showHeaderFooter) {
            userData.size + 2
        } else {
            userData.size
        }
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverView = itemView.findViewById<ImageView>(R.id.album_cover)
        private var songName = itemView.findViewById<TextView>(R.id.song_name)
        private var artistName = itemView.findViewById<TextView>(R.id.artist_name)


        @SuppressLint("SetTextI18n")
        fun onBind(track: Track) {
            val radius = (2 * itemView.resources.displayMetrics.density).toInt()
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(radius))
                .into(coverView)

            songName.text = track.trackName
            artistName.text =
                "${track.artistName} • ${track.convertTrackTimeToLocale(track.trackTimeMillis)}"
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class FooterViewHolder(itemView: View, private val onClearHistoryClick: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val clearHistoryButton = itemView.findViewById<Button>(R.id.clearHistoryButton)

        init {
            clearHistoryButton.setOnClickListener {
                onClearHistoryClick()
            }
        }
    }

    private companion object {
        const val VIEW_TYPE_TRACK = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_FOOTER = 2
    }
}


