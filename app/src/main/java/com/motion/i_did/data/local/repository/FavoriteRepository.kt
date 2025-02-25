package com.motion.i_did.data.local.repository

import androidx.lifecycle.LiveData

import com.motion.i_did.data.local.dao.FavoriteDao
import com.motion.i_did.data.local.entity.FavoriteEntity

//Favorite Repository
class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao,
) {
    //creating favorite repository
    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(
            contactDao: FavoriteDao,
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(
                    favoriteDao = contactDao,
                ).also { instance = it }
            }
    }
    //Add favorite
     suspend fun insertFavorite(favorite: FavoriteEntity) {
        favoriteDao.insertFavorite(favorite)
    }
    //Delete Favorite
    suspend fun deleteFavorite(favorite: FavoriteEntity) {
        favoriteDao.deleteFavorite(favorite)
    }
    //get favorites
     fun getFavorites(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getFavorites()
    }
    //delete All favorite
    suspend fun deleteALL(){
        return favoriteDao.deleteAll()
    }
    //checking if already favorite
    suspend fun  isFavorite(id: String): Int{
        return favoriteDao.isFavorite(id)
    }
}