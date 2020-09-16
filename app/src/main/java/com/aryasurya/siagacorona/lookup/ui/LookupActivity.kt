package com.aryasurya.siagacorona.lookup.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.lookup.adapter.LookupAdapter
import com.aryasurya.siagacorona.lookup.model.LookupModel
import com.aryasurya.siagacorona.lookup.model.LookupResponseWrapper
import kotlinx.android.synthetic.main.activity_lookup.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class LookupActivity : AppCompatActivity() {

    private var okHttpClient = OkHttpClient()

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

        this.requestData(lookupAdapter)

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

    private fun requestData(adapter: LookupAdapter){
        var request = Request.Builder().url("https://api.kawalcorona.com/indonesia/provinsi").build()
        this.okHttpClient.newCall(request)
            .enqueue(populateData(adapter))
    }

    private fun populateData(adapter: LookupAdapter): Callback {
        return object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                this@LookupActivity.runOnUiThread {
                    Toast.makeText(this@LookupActivity,e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonData = response.body?.string()
                    val jsonArray = JSONArray(jsonData)
                    val lookupResponseList = mutableListOf<LookupResponseWrapper>()
                    val lookupList = mutableListOf<LookupModel>()

                    for (i in 0 until jsonArray.length()) {
                        lookupResponseList.add(LookupResponseWrapper(attributes = jsonArray.getJSONObject(i).getJSONObject("attributes")))
                    }

                    for(i in 0 until lookupResponseList.size){
                        lookupList.add(
                            LookupModel(
                                provinceName = lookupResponseList[i].attributes.getString("Provinsi"),
                                positiveCase = lookupResponseList[i].attributes.getInt("Kasus_Posi"),
                                recoveredCase = lookupResponseList[i].attributes.getInt("Kasus_Semb"),
                                deathCase = lookupResponseList[i].attributes.getInt("Kasus_Meni")
                            )
                        )
                    }
                    this@LookupActivity.runOnUiThread {
                        adapter.updateData(lookupList)
                    }
                } catch (e: Exception){
                    this@LookupActivity.runOnUiThread {
                        Toast.makeText( this@LookupActivity,e.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}

