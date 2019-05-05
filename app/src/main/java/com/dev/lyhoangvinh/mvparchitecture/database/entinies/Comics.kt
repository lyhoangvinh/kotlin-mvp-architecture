package com.dev.lyhoangvinh.mvparchitecture.database.entinies

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Comics(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var publisher: String? = null
)