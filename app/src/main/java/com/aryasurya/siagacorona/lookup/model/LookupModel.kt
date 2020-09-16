package com.aryasurya.siagacorona.lookup.model

import org.json.JSONObject

data class LookupResponseWrapper(
    val attributes: JSONObject
)

data class LookupModel(
    val provinceName: String = "",
    val positiveCase: Int = 10,
    val recoveredCase: Int = 20,
    val deathCase: Int = 30
)