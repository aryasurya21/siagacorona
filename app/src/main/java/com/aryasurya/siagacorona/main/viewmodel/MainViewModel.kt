package com.aryasurya.siagacorona.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.repository.HotlineRepository
import com.aryasurya.siagacorona.hotline.viewmodel.HotlineViewModel
import com.aryasurya.siagacorona.main.model.CovidCaseModel
import com.aryasurya.siagacorona.main.repository.MainRepository

class MainViewModel: ViewModel() {
    private var covidStatistics = MutableLiveData<CovidCaseModel>()
    private lateinit var mainRepository: MainRepository

    companion object {
        private var instance: MainViewModel? = null

        val getInstance: MainViewModel?
            get() {
                if(instance == null){
                    instance =
                        MainViewModel()
                }
                return instance
            }
    }

    public fun init(){
        if(covidStatistics.value != null){
            return
        }
        this.mainRepository = MainRepository.getInstance ?: MainRepository()
        this.covidStatistics = this.mainRepository.getCovidStatistic()
    }

    public fun getStatistics(): LiveData<CovidCaseModel> {
        return this.covidStatistics
    }
}