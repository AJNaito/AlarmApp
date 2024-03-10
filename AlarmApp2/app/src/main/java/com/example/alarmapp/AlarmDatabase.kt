package com.example.alarmapp

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.jvm.Volatile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities=[AlarmTable::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao() : AlarmDao
    companion object {
        @Volatile
        private var mInstance : AlarmDatabase? = null

        fun getDatabase(context : Context,
                        scope : CoroutineScope) : AlarmDatabase {
            return mInstance?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDatabase::class.java,
                        "alarm.db").addCallback(RoomDatabaseCallback(scope)).fallbackToDestructiveMigration().build()
                    mInstance = instance
                    instance
                }
            }
        }

        private class RoomDatabaseCallback(
            private val scope : CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                mInstance?.let {
                    database->scope.launch(Dispatchers.IO) {
                        populateDbTask(database.alarmDao())
                    }
                }
            }
            suspend fun populateDbTask(alarmDao: AlarmDao) {
                alarmDao.insert(AlarmTable(0, 0, 0, true, false, 0, ""))
            }
        }
}