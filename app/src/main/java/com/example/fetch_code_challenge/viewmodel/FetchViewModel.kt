package com.example.fetch_code_challenge.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fetch_code_challenge.Repo.FetchApi
import com.example.fetch_code_challenge.Repo.RetrofitHelper
import com.example.fetch_code_challenge.sampledata.Model.FetchDataItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel

class FetchViewModel {
    private val _fetchDataList = MutableLiveData<List<FetchDataItem>>()
    val fetchDataList: LiveData<List<FetchDataItem>> = _fetchDataList

    val fetchApi: FetchApi = RetrofitHelper.getInstance().create(FetchApi::class.java)

//    fun getFetchData(){
//        GlobalScope.launch {
//            val result = fetchApi.getFetchData()
//            if (result.isSuccessful){
//                _fetchDataList.value = result.body()
//                Log.d(TAG, "getFetchData: " + _fetchDataList.value)
//            }
//        }
//    }
}