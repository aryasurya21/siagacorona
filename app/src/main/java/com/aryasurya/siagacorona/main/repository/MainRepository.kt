package com.aryasurya.siagacorona.main.repository

import androidx.lifecycle.MutableLiveData
import com.aryasurya.siagacorona.main.model.CovidCaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class MainRepository {

    private var okHttpClient = OkHttpClient()

    companion object {
        private var instance: MainRepository? = null

        val getInstance: MainRepository?
            get() {
                if (instance == null) {
                    instance =
                        MainRepository()
                }
                return instance
            }
    }

    public fun getCovidStatistic(): MutableLiveData<CovidCaseModel> {
        var data = MutableLiveData<CovidCaseModel>()

        val request = Request.Builder()
            .url("https://api.kawalcorona.com/indonesia")
            .build()

        this.okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    data.postValue(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val gson = Gson()
                        val jsonData = response.body?.string()
                        val covidCaseType = object : TypeToken<Array<CovidCaseModel>>(){}.type

                        val statiscs: Array<CovidCaseModel> = gson.fromJson(jsonData, covidCaseType)

                        data.postValue(statiscs[0])
                    } catch (e: Exception) {
                        data.postValue(null)
                    }
                }
            })
        return data
    }
}
