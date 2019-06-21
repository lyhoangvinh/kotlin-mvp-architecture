package com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces

import android.support.annotation.NonNull
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseFourZip
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseThreeZip

interface PlainResponseZipFourConsumer<T1, T2, T3, T4> {
    fun accept(@NonNull dto: ResponseFourZip<T1, T2, T3, T4>)
}