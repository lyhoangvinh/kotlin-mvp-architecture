package com.dev.lyhoangvinh.mvparchitecture.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import io.reactivex.Single

@Dao
interface ComicsDao : BaseDao<Comics> {

    @Query("SELECT * FROM COMICS")
    fun getAll(): Single<List<Comics>>

    @Query("SELECT * FROM COMICS")
    fun liveData(): LiveData<List<Comics>>
}