package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

class PostOffice {
    @SerializedName("Name")
    var name: String? = null

    @SerializedName("Description")
    var description: String? = null

    @SerializedName("BranchType")
    var branchType: String? = null

    @SerializedName("DeliveryStatus")
    var deliveryStatus: String? = null

    @SerializedName("Taluk")
    var taluk: String? = null

    @SerializedName("Circle")
    var circle: String? = null

    @SerializedName("District")
    var district: String? = null

    @SerializedName("Division")
    var division: String? = null

    @SerializedName("Region")
    var region: String? = null

    @SerializedName("State")
    var state: String? = null

    @SerializedName("Country")
    var country: String? = null
}

class Root {
    @SerializedName("Message")
    var message: String? = null

    @SerializedName("Status")
    var status: String? = null

    @SerializedName("PostOffice")
    var postOffice: ArrayList<PostOffice>? = null
}