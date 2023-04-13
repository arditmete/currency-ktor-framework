package model

import com.google.gson.annotations.SerializedName

internal class ApiResponse {
    @SerializedName("quotes")
    var quotes: Map<String, Double>? = null

    @SerializedName("source")
    var source: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("timestamp")
    var timestamp: Long? = null
}