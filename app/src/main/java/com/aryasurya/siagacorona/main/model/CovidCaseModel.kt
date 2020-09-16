package com.aryasurya.siagacorona.main.model

import com.google.gson.annotations.SerializedName

data class CovidCaseModel(
    @SerializedName("positif") val totalCases: String = "",
    @SerializedName("dirawat") val positiveCase: String = "",
    @SerializedName("sembuh") val recoveredCase: String = "",
    @SerializedName("meninggal") val deathCase: String = ""
)