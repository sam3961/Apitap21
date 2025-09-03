package com.apitap.views.fragments.specials.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apitap.views.fragments.specials.data.AllProductsListResponse
import com.apitap.views.fragments.specials.data.PromotionListingResponse
import com.apitap.views.fragments.specials.network.RetrofitClient
import com.apitap.views.fragments.specials.utils.CommonFunctions
import com.apitap.views.fragments.specials.utils.Constant
import com.apitap.views.fragments.specials.utils.SingleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpecialsViewModel : ViewModel() {

    private val _sessionExpire = MutableLiveData<SingleEvent<Boolean>>()
    val sessionExpire: LiveData<SingleEvent<Boolean>> get() = _sessionExpire

    private val _apiError = MutableLiveData<SingleEvent<String>>()
    val apiError: LiveData<SingleEvent<String>> get() = _apiError

    private val _showLoading = MutableLiveData<SingleEvent<Boolean>>()
    val showLoading: LiveData<SingleEvent<Boolean>> get() = _showLoading

    private val _promotionsByIdResponse =
        MutableLiveData<SingleEvent<List<PromotionListingResponse>>>()
    val promotionsByIdResponse: LiveData<SingleEvent<List<PromotionListingResponse>>> get() = _promotionsByIdResponse

    private val _activeProductResponse =
        MutableLiveData<SingleEvent<List<AllProductsListResponse>>>()
    val activeProductResponse: LiveData<SingleEvent<List<AllProductsListResponse>>> get() = _activeProductResponse


    fun promotionById(locationId: Int) {
        _showLoading.postValue(SingleEvent(true))

        RetrofitClient.instance.promotionById(locationId)
            .enqueue(object : Callback<List<PromotionListingResponse>> {
                override fun onResponse(
                    call: Call<List<PromotionListingResponse>>,
                    response: Response<List<PromotionListingResponse>>
                ) {
                    _showLoading.postValue(SingleEvent(false))
                    if (response.isSuccessful) {
                        _promotionsByIdResponse.postValue(
                            SingleEvent(
                                response.body() ?: emptyList()
                            )
                        )
                    } else if (response.code() == Constant.UNAUTHORIZED) {
                        _sessionExpire.postValue(SingleEvent(true))
                    } else {
                        val errorMessage = response.errorBody()?.string()
                            ?.let { CommonFunctions.parseErrorMessage(it) }
                        _apiError.postValue(SingleEvent(errorMessage ?: "Server Error"))
                    }
                }

                override fun onFailure(call: Call<List<PromotionListingResponse>>, t: Throwable) {
                    _apiError.postValue(SingleEvent(t.message ?: "Server Error"))
                    _showLoading.postValue(SingleEvent(false))
                }
            })
    }

    fun getActiveItemsByIds(productId: String) {
        _showLoading.postValue(SingleEvent(true))

        RetrofitClient.instance.getActiveItemsByIds(productId)
            .enqueue(object : Callback<List<AllProductsListResponse>> {
                override fun onResponse(
                    call: Call<List<AllProductsListResponse>>,
                    response: Response<List<AllProductsListResponse>>
                ) {
                    _showLoading.postValue(SingleEvent(false))
                    if (response.isSuccessful) {
                        _activeProductResponse.postValue(
                            SingleEvent(
                                response.body() ?: emptyList()
                            )
                        )
                    } else if (response.code() == Constant.UNAUTHORIZED) {
                        _sessionExpire.postValue(SingleEvent(true))
                    } else {
                        val errorMessage = response.errorBody()?.string()
                            ?.let { CommonFunctions.parseErrorMessage(it) }
                        _apiError.postValue(SingleEvent(errorMessage ?: "Server Error"))
                    }
                }

                override fun onFailure(call: Call<List<AllProductsListResponse>>, t: Throwable) {
                    _apiError.postValue(SingleEvent(t.message ?: "Server Error"))
                    _showLoading.postValue(SingleEvent(false))
                }
            })
    }


}