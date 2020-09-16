package com.aryasurya.siagacorona.lookup.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aryasurya.siagacorona.lookup.model.LookupModel
import kotlinx.android.synthetic.main.item_lookup.view.*

class LookupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    fun bindUI(data: LookupModel){
        itemView.tvLookUpProvinceName.text = data.provinceName
        itemView.tvLookUpDeathCase.text = data.deathCase.toString()
        itemView.tvLookUpConfirmedCase.text = data.positiveCase.toString()
        itemView.tvLookUpRecoverCase.text = data.recoveredCase.toString()
    }
}