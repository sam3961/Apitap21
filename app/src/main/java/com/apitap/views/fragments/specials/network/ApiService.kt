package com.apitap.views.fragments.specials.network

import com.apitap.views.fragments.specials.data.AllProductsListResponse
import com.apitap.views.fragments.specials.data.InventoryResponse
import com.apitap.views.fragments.specials.data.OrderByIdResponse
import com.apitap.views.fragments.specials.data.ProductByIdResponse
import com.apitap.views.fragments.specials.data.PromotionListingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Define the API interface
interface ApiService {

    @POST("api/aioproducts/itemInventoryByProductId/{productId}")
    suspend fun getInventoryByProductId(
        @Path("productId") productId: Int?
    ):  List<InventoryResponse>


    @GET("api/aioproducts/promotionsByCompanyId/{locationId}/{companyId}")
    fun getPromotionsByCompanyId(
        @Path("locationId") locationId: Int?,
        @Path("companyId") companyId: Int?,
    ): Call<List<PromotionListingResponse>>

    @GET("api/aioproducts/activeProductsByCompanyId/{companyId}")
    fun activeProductsByCompanyId(
        @Path("companyId") companyId: Int?,
    ): Call<List<AllProductsListResponse>>

    @GET("api/aioproducts/productById/{id}")
    fun getProductById(
        @Path("id") id: Int?,
    ): Call<ProductByIdResponse>


    @GET("api/aioorders/deleteOrderDetail/{tableOrder}/{detailId}/{productRegularPrice}/{elapsedTime}")
    fun deleteOrderDetail(
        @Path("tableOrder") id: Int?,
        @Path("detailId") detailId: Int?,
        @Path("productRegularPrice") productRegularPrice: Double?,
        @Path("elapsedTime") elapsedTime: String?,
    ): Call<Any>


    @GET("api/aioorders/orderById/{id}/{locationId}")
    fun getOrderById(
        @Path("id") id: Int?,
        @Path("locationId") locationId: Int?
    ): Call<OrderByIdResponse>


    @GET("api/aioproducts/promotionById/{promotionById}")
    fun promotionById(
        @Path("promotionById") promotionById: Int?,
    ): Call<List<PromotionListingResponse>>

    @GET("api/aioproducts/getActiveItemsByIds/{ids}")
    fun getActiveItemsByIds(
        @Path("ids") ids: String
    ): Call<List<AllProductsListResponse>>


}
