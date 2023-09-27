package com.luridevlabs.citylights.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luridevlabs.citylights.databinding.RowMonumentListItemBinding
import com.luridevlabs.citylights.model.Monument

class MonumentListAdapter : RecyclerView.Adapter<MonumentListAdapter.MonumentListViewHolder>() {

    private var monumentList: List<Monument> = emptyList()

    var onClickListener: (Monument) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonumentListViewHolder {
        val binding = RowMonumentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonumentListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return monumentList.size
    }

    override fun onBindViewHolder(holder: MonumentListViewHolder, position: Int) {
        val item = monumentList[position]

        holder.rootView.setOnClickListener {
            onClickListener.invoke(item)
        }

        holder.nameTextView.text = item.title

        Glide.with(holder.monumentImageView)
            .load(item.image)
            .into(holder.monumentImageView)
    }

    fun submitList(list: List<Monument>) {
        monumentList = list
        notifyDataSetChanged()
    }

    inner class MonumentListViewHolder(binding: RowMonumentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val rootView = binding.root
        val nameTextView = binding.tvMonumentItemName
        val monumentImageView = binding.ivMonumentItemImage
    }

}