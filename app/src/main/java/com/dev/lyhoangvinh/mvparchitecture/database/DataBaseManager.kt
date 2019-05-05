package com.dev.lyhoangvinh.mvparchitecture.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.dev.lyhoangvinh.mvparchitecture.database.dao.ComicsDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics


@Database(entities = [Comics::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao
}
