package com.aryasurya.siagacorona.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.about.ui.AboutFragment
import com.aryasurya.siagacorona.hotline.ui.HotlineFragment
import com.aryasurya.siagacorona.lookup.ui.LookupActivity
import com.aryasurya.siagacorona.main.model.CovidCaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val okHttpClient = OkHttpClient()
    private val preferenceName = "covid"
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lookup_view.setOnClickListener(this)
        hotline_view.setOnClickListener(this)
        about_dialog.setOnClickListener(this)
        this.title = "Home"
        this.preferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        if(!this.preferences.contains("total")){
            this.requestData()
        } else {
            covid_total.text = this.preferences.getString("total", "0")
            tv_recovered_case.text = this.preferences.getString("recovered", "0")
            tv_positive_case.text = this.preferences.getString("positive", "0")
            tv_death_case.text = this.preferences.getString("death", "0")
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            lookup_view -> navigateTo(LookupActivity())
            hotline_view -> showBottomSheet()
            about_dialog -> showAboutDialog()
        }
    }

    private fun requestData(){
        var request = Request.Builder().url("https://api.kawalcorona.com/indonesia/").build()
        this.okHttpClient.newCall(request)
            .enqueue(populateData())
    }

    private fun setDataLocally(
        totalCases: String,
        recovered: String,
        positive: String,
        death: String
    ){
        val editor = this.preferences.edit()
        editor.putString("total", totalCases)
        editor.putString("recovered", recovered)
        editor.putString("positive", positive)
        editor.putString("death", death)
        editor.apply()
    }

    private fun populateData(): Callback{
            return object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val gson = Gson()
                        val jsonData = response.body?.string()
                        val covidCaseType = object : TypeToken<Array<CovidCaseModel>>(){}.type

                        val data: Array<CovidCaseModel> = gson.fromJson(jsonData, covidCaseType)
                        this@MainActivity?.runOnUiThread {
                            covid_total.text = data[0].totalCases
                            tv_recovered_case.text = data[0].recoveredCase
                            tv_positive_case.text = data[0].positiveCase
                            tv_death_case.text = data[0].deathCase
                        }
                        this@MainActivity.setDataLocally(
                            data[0].totalCases,
                            data[0].recoveredCase,
                            data[0].positiveCase,
                            data[0].deathCase
                        )
                    } catch (e: Exception){
                        this@MainActivity?.runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                e.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
    }

    private fun showBottomSheet(){
        val bottomSheet = HotlineFragment()
        bottomSheet.show(supportFragmentManager, "hotlineFragment")
    }

    private fun navigateTo(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    private fun showAboutDialog(){
        val aboutDialog = AboutFragment()
        aboutDialog.show(supportFragmentManager, "aboutFragment")
    }
}
