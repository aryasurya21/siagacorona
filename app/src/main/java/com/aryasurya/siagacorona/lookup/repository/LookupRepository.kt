package com.aryasurya.siagacorona.lookup.repository

import androidx.lifecycle.MutableLiveData
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.repository.HotlineRepository
import com.aryasurya.siagacorona.lookup.model.LookupModel
import com.aryasurya.siagacorona.lookup.model.LookupResponseWrapper
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class LookupRepository {
    private var okHttpClient = OkHttpClient()

    companion object {
        private var instance: LookupRepository? = null

        val getInstance: LookupRepository?
            get() {
                if(instance == null){
                    instance =
                        LookupRepository()
                }
                return instance
            }
    }

    public fun getLookupData(): MutableLiveData<List<LookupModel>> {
        var data = MutableLiveData<List<LookupModel>>()

        val request = Request.Builder()
            .url("https://api.kawalcorona.com/indonesia/provinsi")
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
                        data.postValue(lookupList)
                    } catch (e: Exception) {
                        data.postValue( null)
                    }
                }
            })
        return data
    }
}