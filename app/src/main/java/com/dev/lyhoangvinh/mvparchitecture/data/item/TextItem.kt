package com.dev.lyhoangvinh.mvparchitecture.data.item

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.ItemViewModel

data class TextItem(
    var id: Int? = 0,
    var text: String? = null
) : ItemViewModel