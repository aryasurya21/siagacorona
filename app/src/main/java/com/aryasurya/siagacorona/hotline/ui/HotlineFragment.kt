package com.aryasurya.siagacorona.hotline.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.hotline.adapter.HotlineAdapter
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_hotline.*
import kotlinx.android.synthetic.main.fragment_hotline.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class HotlineFragment : BottomSheetDialogFragment() {

    private val okHttpClient = OkHttpClient()

    private val listData = mutableListOf<HotlineModel>(
        HotlineModel("","Loading...","")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hotline, container, false)
        view.rv_hotline.layoutManager = LinearLayoutManager(inflater.context)
        val hotlineAdapter = HotlineAdapter(this.listData)
        view.rv_hotline.adapter = hotlineAdapter

        val request = Request.Builder()
            .url("https://bncc-corona-versus.firebaseio.com/v1/hotlines.json")
            .build()

        this.okHttpClient.newCall(request)
            .enqueue(getCallback(hotlineAdapter))
        return view
    }

    private fun getCallback(adapter: HotlineAdapter): Callback{
        return object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                this@HotlineFragment.activity?.runOnUiThread {
                    Toast.makeText(this@HotlineFragment.activity,e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonData = response.body?.string()
                    val jsonArray = JSONArray(jsonData)
                    val hotlineList = mutableListOf<HotlineModel>()

                    for (i in 0 until jsonArray.length()) {
                        hotlineList.add(
                            HotlineModel(
                                imgIcon = jsonArray.getJSONObject(i).getString("img_icon"),
                                name = jsonArray.getJSONObject(i).getString("name"),
                                phone = jsonArray.getJSONObject(i).getString("phone")
                            )
                        )
                    }
                    this@HotlineFragment.activity?.runOnUiThread {
                        adapter.updateData(hotlineList)
                    }
                } catch (e: Exception){
                    this@HotlineFragment.activity?.runOnUiThread {
                        Toast.makeText(this@HotlineFragment.activity,e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}