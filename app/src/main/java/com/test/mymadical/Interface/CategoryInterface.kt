package com.test.mymadical.Interface

import com.test.mymadical.model.*
import retrofit2.Call
import retrofit2.http.*

interface CategoryInterface {
    @GET("display_category.php")

    fun getdata(): Call<CategoryInfo>
}


interface SubcategoryInterface {
    @FormUrlEncoded
    @POST("display-subcategory.php")
    fun getsubdata(
        @Field("cat_id") cat_id: Int
    ): Call<SubcategoryInfo>
}
interface ProductInterface {
    @FormUrlEncoded
    @POST("display_product.php")
    fun getprodata(
        @Field("sub_cat_id") sub_cat_id: Int
    ): Call<ProductInfo>
}
interface SearchProductInterface {
    @FormUrlEncoded
    @POST("product_search_mymedical.php")
    fun getsearchprodata(
        @Field("p_name") p_name: String
    ): Call<ProductInfo>
}
interface ProductdetailsInterface {
    @FormUrlEncoded
    @POST("display_product_details.php")
    fun getprodetaildata(
        @Field("pro_id") pro_id: Int
    ): Call<ProductInfo>


}
interface GetUserInfoInterface {
    @FormUrlEncoded
    @POST("user_get_details.php")
    fun getprodetaildata(
        @Field("user_mobileno") user_mobileno: String
    ): Call<ModelgetUserDetailsInfo>

}
interface GetCartInfoInterface {
    @FormUrlEncoded
    @POST("display_cart_mymedical.php")
    fun getcartdetaildata(
        @Field("user_id") user_id: Int
    ): Call<ModelCartDisplayInfo>

}

interface GetUserLoginInterface {
    @FormUrlEncoded
    @POST("mymedical_login.php")
    fun getlogindata(
        @Field("user_mobileno") user_mobileno: String
    ): Call<ModelLoginInfo>

}

interface AddToCartInterface {
    @FormUrlEncoded
    @POST("cart_add_quntity.php")
    fun insertdata(

        @Field("user_id") user_id: String?,
        @Field("product_id") product_id: String?,
        @Field("quantity") quantity: String?
    ): Call<ModelCartaddInfo>


}

interface GetAddressInfoInterface {
    @FormUrlEncoded
    @POST("display_my_address.php")
    fun getaddressdetaildata(
        @Field("user_id") user_id: String
    ): Call<ModelAddressDisplayInfo>

}

interface PostAddressInfoInterface {
    @FormUrlEncoded
    @POST("add_address_mymedical.php")
    fun postaddressdetaildata(
        @Field("types") types: String,
        @Field("address_id") address_id: String,
        @Field("user_id") user_id: String,
        @Field("name") name: String,
        @Field("number") number: String,
        @Field("address") address: String,
        @Field("area") area: String,
        @Field("pincode") pincode: String,
        @Field("city") city: String,
        @Field("state") state: String,
    ): Call<ModelAddEditAddressInfo>

}

interface DeleteAddressInfoInterface {
    @FormUrlEncoded
    @POST("delete_address_mymedical.php")
    fun deleteaddressdetaildata(
        @Field("address_id") address_id: String

    ): Call<ModelAddEditAddressInfo>

}

interface CouponcodeInfoInterface {
    @FormUrlEncoded
    @POST("discount_code_mymedical.php")
    fun couponcodedetaildata(
        @Field("user_id") user_id: String,
        @Field("coupon_code") coupon_code: String
    ): Call<ModelCouponcodeInfo>

}

interface orderplacedInfoInterface {
    @FormUrlEncoded
    @POST("order_placed_mymedical.php")
    fun orderplaceddetails(
        @Field("user_id") user_id: String,
        @Field("value_total") value_total: String,
        @Field("status") status: String,
        @Field("order_date") order_date: String,
        @Field("delivary_date") delivary_date: String,
        @Field("address_id") address_id: String,
        @Field("payment_status") payment_status: String,
        @Field("total_item") total_item: String,

    ): Call<ModelOrderPlaced>

}
interface OrderDisplayInterface {
    @FormUrlEncoded
    @POST("order_display_mymedical.php")
    fun getorderdata(
        @Field("user_id") user_id: Int
    ): Call<ModelMyOrder>
}

