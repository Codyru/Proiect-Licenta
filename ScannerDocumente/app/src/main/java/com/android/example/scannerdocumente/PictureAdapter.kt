package com.android.example.scannerdocumente

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PictureAdapter(private var entries: List<ImageData>, private val deleteListener: OnDeleteListener): RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    interface OnDeleteListener {
        fun onDelete(entry: ImageData)
    }

    inner class PictureViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView) {
        private val pictureImageView: ImageView = itemView.findViewById(R.id.pictureImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val documentTypeTextView: TextView = itemView.findViewById(R.id.documentTypeTextView)
        private val btnDeletePicture: ImageView = itemView.findViewById(R.id.ivDeletePicture)
        private val btnSendValidationData: ImageView = itemView.findViewById(R.id.ivSendValidationData)

        init {
            btnSendValidationData.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val entry = entries[position]
                    val entryUri = entry.uri
                    val entryDocumentType = entry.documentType
                    val context = itemView.context
                    val intent = Intent(context, FilePickerActivity::class.java)
                    intent.putExtra("PICTURE_URI", entryUri)
                    Log.d("PICTURE_ADAPTER", "$entryUri")
                    intent.putExtra("PICTURE_DOCUMENT_TYPE", entryDocumentType)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(entry: ImageData) {
            val convertor = Converters()
            Glide.with(itemView)
                .load(convertor.toUri(entry.uri))
                .into(pictureImageView)
            nameTextView.text = entry.name
            dateTextView.text = entry.currentDate
            documentTypeTextView.text = entry.documentType

            btnDeletePicture.setOnClickListener {
                deleteListener.onDelete(entry)
            }
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