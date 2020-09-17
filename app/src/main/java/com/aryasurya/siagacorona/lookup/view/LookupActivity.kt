package com.aryasurya.siagacorona.lookup.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.viewmodel.HotlineViewModel
import com.aryasurya.siagacorona.lookup.view.adapter.LookupAdapter
import com.aryasurya.siagacorona.lookup.model.LookupModel
import com.aryasurya.siagacorona.lookup.model.LookupResponseWrapper
import com.aryasurya.siagacorona.lookup.viewmodel.LookupViewModel
import kotlinx.android.synthetic.main.activity_lookup.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class LookupActivity : AppCompatActivity() {

    private lateinit var hotlieList: List<HotlineModel>
    private lateinit var viewModel: LookupViewModel

    private var listData = mutableListOf(
        LookupModel("Loading...",0,0,0)
    )

    private fun filterList(keyword: CharSequence?, adapter: LookupAdapter){
        var filteredResults = ArrayList<LookupModel>()
        for (item in this.listData){
            if(keyword?.let { item.provinceName.toLowerCase().contains(it.toString().toLowerCase()) }!!){
                filteredResults.add(item)
            }
        }
        adapter.filterList(filteredResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lookup)

        rv_lookup.layoutManager = LinearLayoutManager(this)
        val lookupAdapter = LookupAdapter(this.listData)
        rv_lookup.adapter = lookupAdapter

        setSupportActionBar(toolbar)

        this.viewModel = ViewModelProviders.of(this).get(LookupViewModel.getInstance!!.javaClass)
        this.viewModel.init()
        this.viewModel.getLookUpData().observe(this,
            Observer<List<LookupModel>> { lookupList ->
                if (lookupList == null) {
                    this.runOnUiThread {
                        Toast.makeText(this,"Something Went Wrong When Fetching Data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    this.runOnUiThread {
                        lookupAdapter.updateData(lookupList as MutableList<LookupModel>)
                    }
                }
            })
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        et_searchfield.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s, lookupAdapter)
            }
        })
    }
}

