package com.dev.lyhoangvinh.mvparchitecture.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category

@Dao
interface CategoriesDao : BaseDao<Category> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entities: List<Category>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(entities: List<Category>)

    @Query("SELECT * FROM CATEGORY")
    fun liveData(): LiveData<List<Category>>
}