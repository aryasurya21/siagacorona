package com.aryasurya.siagacorona.lookup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aryasurya.siagacorona.hotline.model.HotlineModel
import com.aryasurya.siagacorona.hotline.repository.HotlineRepository
import com.aryasurya.siagacorona.hotline.viewmodel.HotlineViewModel
import com.aryasurya.siagacorona.lookup.model.LookupModel
import com.aryasurya.siagacorona.lookup.repository.LookupRepository

class LookupViewModel: ViewModel(){
    private var lookupList = MutableLiveData<List<LookupModel>>()
    private lateinit var lookupRepository: LookupRepository

    companion object {
        private var instance: LookupViewModel? = null

        val getInstance: LookupViewModel?
            get() {
                if(instance == null){
                    instance =
                        LookupViewModel()
                }
                return instance
            }
    }

    public fun init(){
        if(lookupList.value != null){
            return
        }
        this.lookupRepository = LookupRepository.getInstance ?: LookupRepository()
        this.lookupList = this.lookupRepository.getLookupData()
    }

    public fun getLookUpData(): LiveData<List<LookupModel>> {
        return this.lookupList
    }
}