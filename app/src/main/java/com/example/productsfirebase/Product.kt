package com.example.productsfirebase

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Product(
    var id: String? = null,
    val name: String,
    val price: Int,
    val description: String
) : Parcelable
