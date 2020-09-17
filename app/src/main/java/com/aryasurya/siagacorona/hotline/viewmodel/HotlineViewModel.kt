package com.aryasurya.siagacorona.hotline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.repository.HotlineRepository

class HotlineViewModel: ViewModel() {
    private var hotlineList = MutableLiveData<List<HotlineModel>>()
    private lateinit var lookupRepository: HotlineRepository

    companion object {
        private var instance: HotlineViewModel? = null

        val getInstance: HotlineViewModel?
            get() {
                if(instance == null){
                    instance =
                        HotlineViewModel()
                }
                return instance
            }
    }

    public fun init(){
        if(hotlineList.value != null){
            return
        }
        this.lookupRepository = HotlineRepository.getInstance ?: HotlineRepository()
        this.hotlineList = this.lookupRepository.getHotlineData()
    }

    public fun getHotlines(): LiveData<List<HotlineModel>> {
        return this.hotlineList
    }
}