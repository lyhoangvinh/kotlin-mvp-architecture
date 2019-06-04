package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoriesRepo @Inject constructor(
    private val avgleService: AvgleService,
    private val categoriesDao: CategoriesDao
) : BaseRepo() {

    fun liveData(): LiveData<List<Category>> = categoriesDao.liveData()

    fun getRepoCategories(): Flowable<Resource<BaseResponseAvgle<CategoriesResponse>>> {
        return createResource(
            avgleService.getCategories(),
            object : PlainConsumer<BaseResponseAvgle<CategoriesResponse>> {
                override fun accept(t: BaseResponseAvgle<CategoriesResponse>) {
                    if (t.success) {
                        Completable.fromAction {
                            categoriesDao.insertIgnore(t.response.categories!!)
                            categoriesDao.updateIgnore(t.response.categories!!)
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : CompletableObserver {
                                override fun onComplete() {
                                    Log.d(CategoriesRepo::class.java.simpleName, "onComplete")
                                }

                                override fun onSubscribe(d: Disposable) {
                                    Log.d(CategoriesRepo::class.java.simpleName, "onSubscribe")
                                }

                                override fun onError(e: Throwable) {
                                    Log.d(CategoriesRepo::class.java.simpleName, "onError")
                                }
                            })
                    }
                }
            })
    }
}