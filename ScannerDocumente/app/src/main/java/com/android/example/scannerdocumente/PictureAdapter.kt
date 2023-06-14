package com.android.example.scannerdocumente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PictureAdapter(private var entries: List<ImageData>): RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView) {
        private val pictureImageView: ImageView = itemView.findViewById(R.id.pictureImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val documentTypeTextView: TextView = itemView.findViewById(R.id.documentTypeTextView)

        fun bind(entry: ImageData) {
            val convertor = Converters()
            Glide.with(itemView)
                .load(convertor.toUri(entry.uri))
                .into(pictureImageView)
            nameTextView.text = entry.name
            dateTextView.text = entry.currentDate
            documentTypeTextView.text = entry.documentType
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return PictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun setData(entries: List<ImageData>) {
        this.entries = entries
        notifyDataSetChanged()
    }

}