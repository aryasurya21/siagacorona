package com.aryasurya.siagacorona.hotline.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.view.adapter.HotlineAdapter
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.viewmodel.HotlineViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_hotline.view.*

class HotlineFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HotlineViewModel

    private var listData = mutableListOf<HotlineModel>(
        HotlineModel("","Loading...","")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hotline, container, false)
        view.rv_hotline.layoutManager = LinearLayoutManager(inflater.context)
        val hotlineAdapter = HotlineAdapter(this.listData)
        view.rv_hotline.adapter = hotlineAdapter
        view.closeButton.setOnClickListener {
            this.dismiss()
        }

        this.viewModel = ViewModelProviders.of(this).get(HotlineViewModel.getInstance!!.javaClass)
        this.viewModel.init()
        this.viewModel.getHotlines().observe(this,
            Observer<List<HotlineModel>> { hotlineList ->
                if (hotlineList == null) {
                    this@HotlineFragment.activity?.runOnUiThread {
                        Toast.makeText(this@HotlineFragment.activity,"Something Went Wrong When Fetching Data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    this@HotlineFragment.activity?.runOnUiThread {
                        hotlineAdapter.updateData(hotlineList as MutableList<HotlineModel>)
                    }
                }
            })

        return view
    }
}