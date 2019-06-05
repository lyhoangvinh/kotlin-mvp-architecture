package com.dev.lyhoangvinh.mvparchitecture.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection

@Dao
interface CollectionDao : BaseDao<Collection> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entities: List<Collection>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(entities: List<Collection>)

    @Query("DELETE FROM COLLECTION")
    fun deleteAll()

    @Query("SELECT * FROM COLLECTION")
    fun liveData(): LiveData<List<Collection>>
}