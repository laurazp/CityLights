package com.luridevlabs.citylights.presentation.fragment.personallists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.RowPersonalListItemBinding
import com.luridevlabs.citylights.model.MonumentList
import com.luridevlabs.citylights.presentation.fragment.personallists.adapter.PersonalListAdapter.PersonalListViewHolder

class PersonalListAdapter : RecyclerView.Adapter<PersonalListViewHolder>() {

    private var personalLists: List<MonumentList> = mutableListOf()
    private var onClickListener: (MonumentList) -> Unit = {}

    fun submitList(list: List<MonumentList>) {
        personalLists = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalListViewHolder {
        val binding = RowPersonalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonalListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return personalLists.size
    }

    override fun onBindViewHolder(holder: PersonalListViewHolder, position: Int) {
        val item = personalLists[position]

        holder.rootView.setOnClickListener {
            onClickListener.invoke(item)
        }

        holder.listName.text = item.listName
        holder.monumentNumber.text = holder.rootView.context.getString(R.string.list_monument_number, item.monuments.size)
    }

    inner class PersonalListViewHolder(binding: RowPersonalListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val rootView = binding.root
        val listName = binding.tvPersonalListTitle
        val monumentNumber = binding.tvPersonalListMonuments
    }
}