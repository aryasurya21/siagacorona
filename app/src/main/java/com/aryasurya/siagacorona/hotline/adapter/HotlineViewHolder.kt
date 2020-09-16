package com.aryasurya.siagacorona.hotline.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_hotline.view.*

class HotlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindUI(data: HotlineModel){
        itemView.tv_institue_name.text = data.name
        itemView.tv_institute_phone.text = data.phone
        if(data.imgIcon.isNotBlank()) {
            Picasso.get().load(data.imgIcon).into(itemView.iv_institute_logo)
        }
    }
}