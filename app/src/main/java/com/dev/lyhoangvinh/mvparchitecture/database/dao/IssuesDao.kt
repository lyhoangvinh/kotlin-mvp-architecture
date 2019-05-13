package com.dev.lyhoangvinh.mvparchitecture.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update
import retrofit2.http.DELETE


@Dao
interface IssuesDao : BaseDao<Issues> {
    @Query("SELECT * FROM Issues")
    fun liveData(): LiveData<List<Issues>>

    @Insert
    fun inserts(list: List<Issues>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entities: List<Issues>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(entities: List<Issues>)

    @Query("DELETE FROM Issues")
    fun removeAll()
}