package com.example.nurbk.ps.audiorecorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_list_item.view.*
import java.io.File


class AudioListAdapter :
    RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {

    private var timeAgo: TimeAgo? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_list_item, parent, false)
        timeAgo = TimeAgo()
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AudioViewHolder,
        position: Int
    ) {
        val file = differ.currentList[position]

        holder.itemView.apply {

            list_title.text = file.name
            list_date.text = timeAgo!!.getTimeAgo(file.lastModified())

            setOnClickListener {
                onItemClickListener?.let {
                    it(file)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((File) -> Unit)? = null

    fun setOnItemClickListener(listener: (File) -> Unit) {
        onItemClickListener = listener
    }



}
