package com.motion.i_did.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.motion.i_did.data.local.dao.FavoriteDao
import com.motion.i_did.data.local.entity.FavoriteEntity

//Favorite Database
@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    //creating favorite database using room
    companion object {
        private const val DATABASE_NAME = "contact_database"
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FavoriteDatabase {
            if (INSTANCE == null){
                synchronized(FavoriteDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }

            return INSTANCE as FavoriteDatabase
        }
    }

}