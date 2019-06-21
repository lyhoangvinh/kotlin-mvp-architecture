package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface CollectionView : BaseView {
    fun connection(isConnected: Boolean)
    fun openDetail(collection: Collection)
}