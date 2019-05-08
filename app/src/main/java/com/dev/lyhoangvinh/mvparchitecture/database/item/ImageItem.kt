package com.dev.lyhoangvinh.mvparchitecture.database.item

import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Item

data class ImageItem(
    var id: Int? = 0,
    var text: String? = null
) : Item