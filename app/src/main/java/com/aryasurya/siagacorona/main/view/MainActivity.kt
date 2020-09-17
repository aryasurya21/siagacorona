package com.aryasurya.siagacorona.main.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aryasurya.siagacorona.R
import com.aryasurya.siagacorona.aboutus.view.AboutFragment
import com.aryasurya.siagacorona.hotline.view.HotlineFragment
import com.aryasurya.siagacorona.lookup.view.LookupActivity
import com.aryasurya.siagacorona.main.model.CovidCaseModel
import com.aryasurya.siagacorona.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val preferenceName = "covid"
    private lateinit var preferences: SharedPreferences
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lookup_view.setOnClickListener(this)
        hotline_view.setOnClickListener(this)
        about_dialog.setOnClickListener(this)

        this.title = "Home"
        this.preferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

        if(!this.preferences.contains("total")){
            this.viewModel = ViewModelProviders.of(this).get(MainViewModel.getInstance!!.javaClass)
            this.viewModel.init()
            this.viewModel.getStatistics().observe(this,
                Observer<CovidCaseModel> { statistics ->
                    if (statistics == null) {
                        this.runOnUiThread {
                            Toast.makeText(this,"Something Went Wrong When Fetching Data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        this.runOnUiThread {
                            displayData(statistics)
                            setDataLocally(statistics.totalCases, statistics.recoveredCase, statistics.positiveCase, statistics.deathCase)
                        }
                    }
                })
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

    private fun displayData(covidStat: CovidCaseModel){
        covid_total.text = covidStat.totalCases
        tv_recovered_case.text = covidStat.recoveredCase
        tv_positive_case.text = covidStat.positiveCase
        tv_death_case.text = covidStat.deathCase
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
