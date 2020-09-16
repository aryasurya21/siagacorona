package com.aryasurya.siagacorona.hotline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.model.HotlineModel

class HotlineAdapter(val hotlineList: MutableList<HotlineModel>) :
    RecyclerView.Adapter<HotlineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotlineViewHolder {
        return HotlineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hotline, parent, false))
    }

    override fun getItemCount(): Int {
        return this.hotlineList.size
    }

    override fun onBindViewHolder(holder: HotlineViewHolder, position: Int) {
       holder.bindUI(this.hotlineList[position])
    }

    fun updateData(newList: MutableList<HotlineModel>){
        this.hotlineList.clear()
        this.hotlineList.addAll(newList)
        notifyDataSetChanged()
    }
}