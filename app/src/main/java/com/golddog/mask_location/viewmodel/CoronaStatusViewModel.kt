package com.golddog.mask_location.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.golddog.mask_location.base.BaseViewModel
import com.golddog.mask_location.data.datasource.StatusDataSource
import com.golddog.mask_location.entity.AccumulateCoronaData
import com.golddog.mask_location.entity.CityCoronaData
import com.golddog.mask_location.entity.CoronaResult
import io.reactivex.SingleObserver
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class CoronaStatusViewModel(private val statusDataSource: StatusDataSource) : BaseViewModel() {
    val accumulateData: MutableLiveData<AccumulateCoronaData> =
        MutableLiveData(AccumulateCoronaData("갱신 중...", CoronaResult()))
    val citiesData: MutableLiveData<CityCoronaData> =
        MutableLiveData(CityCoronaData("갱신 중...", mutableListOf()))
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        setAccumulateData()
        setCitiesData()
    }

    private fun setAccumulateData() {
        isLoading.value = true
        addDisposable(
            statusDataSource.getAccumulateData()
                .subscribe({
                    accumulateData.value = it
                    isLoading.value = false
                }, {
                    accumulateData.value?.baseDate = "갱신 실패"
                    isLoading.value = false
                })
        )
    }

    private fun setCitiesData() {
        addDisposable(
            statusDataSource.getCitiesData()
                .subscribe({
                    citiesData.value = it
                }, {
                    citiesData.value?.baseDate = "갱신 실패"
                })
        )
    }
}