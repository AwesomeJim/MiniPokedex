package com.awesomejim.pokedex.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */
@Parcelize
data class Pokemon(
    var page: Int = 0,
    val name: String,
    var id: Int,
    val url: String,
) : Parcelable
