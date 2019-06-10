package com.dev.lyhoangvinh.mvparchitecture.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.VideosHome

@Dao
interface VideosHomeDao : BaseDao<VideosHome> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entities: List<VideosHome>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(entities: List<VideosHome>)

    @Query("DELETE FROM VIDEOS_HOME")
    fun deleteAll()

    @Query("SELECT * FROM VIDEOS_HOME")
    fun liveData(): LiveData<List<VideosHome>>
}