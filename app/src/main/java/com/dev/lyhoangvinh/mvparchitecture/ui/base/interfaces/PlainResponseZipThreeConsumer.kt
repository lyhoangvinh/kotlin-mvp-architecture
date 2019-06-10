package com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces

import android.support.annotation.NonNull
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseThreeZip

interface PlainResponseZipThreeConsumer<T1, T2, T3> {
    fun accept(@NonNull dto: ResponseThreeZip<T1, T2, T3>)
}