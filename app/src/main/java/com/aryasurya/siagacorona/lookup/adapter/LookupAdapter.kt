package com.aryasurya.siagacorona.lookup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.lookup.model.LookupModel

class LookupAdapter(private var items: MutableList<LookupModel>): RecyclerView.Adapter<LookupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookupViewHolder {
        return LookupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lookup, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LookupViewHolder, position: Int) {
        holder.bindUI(items[position])
    }

    fun filterList(items: ArrayList<LookupModel>){
        this.items = items
        notifyDataSetChanged()
    }

    fun updateData(newList: MutableList<LookupModel>){
        this.items.clear()
        this.items.addAll(newList)
        notifyDataSetChanged()
    }
}