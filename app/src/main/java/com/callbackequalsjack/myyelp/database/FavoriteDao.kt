package com.callbackequalsjack.myyelp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    suspend fun getFavorites() : List<Favorite>

    @Query("DELETE FROM favorite WHERE itemTitle = :itemTitle")
    suspend fun deleteFavorite(itemTitle: String)
}