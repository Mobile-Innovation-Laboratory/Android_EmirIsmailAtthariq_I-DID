package com.motion.i_did.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.motion.i_did.data.local.entity.FavoriteEntity
//Dao for the Favorites
@Dao
interface FavoriteDao {
    //function for adding favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertFavorite(favorite: FavoriteEntity)
    //function for getting favorites
    @Query("SELECT * FROM favoriteentity")
     fun getFavorites(): LiveData<List<FavoriteEntity>>
    //function for deleting favorite
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
    //function for deleting all favorites
    @Query("DELETE FROM favoriteentity")
    suspend fun deleteAll()
    //function for checking is it already favorite
    @Query("SELECT COUNT(*) FROM FavoriteEntity WHERE id = :id")
    suspend fun isFavorite(id: String): Int

}