package com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces

import android.support.annotation.NonNull
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip


interface PlainResponseZipBiConsumer<T1, T2> {
    fun accept(@NonNull dto: ResponseBiZip<T1, T2>)
}