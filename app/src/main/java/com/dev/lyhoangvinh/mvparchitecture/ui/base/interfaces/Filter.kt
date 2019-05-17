package com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces

interface Filter<T, E> {
    fun isMatched(obj: T, text: E): Boolean
}
