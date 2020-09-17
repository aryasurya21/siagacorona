package com.aryasurya.siagacorona.hotline.repository

import androidx.lifecycle.MutableLiveData
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class HotlineRepository {
    private var okHttpClient = OkHttpClient()


    companion object {
        private var instance: HotlineRepository? = null

        val getInstance: HotlineRepository?
        get() {
            if(instance == null){
                instance =
                    HotlineRepository()
            }
            return instance
        }
    }

    public fun getHotlineData(): MutableLiveData<List<HotlineModel>> {
        var data = MutableLiveData<List<HotlineModel>>()

        val request = Request.Builder()
            .url("https://bncc-corona-versus.firebaseio.com/v1/hotlines.json")
            .build()

        this.okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    data.postValue(null)
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
                        data.postValue(hotlineList)
                    } catch (e: Exception) {
                        data.postValue( null)
                    }
                }
            })
        return data
    }
}
