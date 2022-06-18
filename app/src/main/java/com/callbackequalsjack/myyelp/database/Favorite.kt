package com.callbackequalsjack.myyelp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    val itemTitle: String,
    val itemRating: Float,
    val itemCategory: String,
    val itemPhone: String,
    val itemAddress: String,
    val itemImage: String
)
