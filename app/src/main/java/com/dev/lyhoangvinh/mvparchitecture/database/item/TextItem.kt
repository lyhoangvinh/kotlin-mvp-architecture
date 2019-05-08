package com.dev.lyhoangvinh.mvparchitecture.database.item

import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Item

data class TextItem(
    var id: Int? = 0,
    var text: String? = null
) : Item