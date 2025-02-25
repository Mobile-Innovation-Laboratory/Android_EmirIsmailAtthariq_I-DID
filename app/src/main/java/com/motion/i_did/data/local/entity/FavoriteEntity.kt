package com.motion.i_did.data.local.entity

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
//Entity for favorite
@Entity
data class FavoriteEntity(
    //stores all the information of favorite
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String
)
