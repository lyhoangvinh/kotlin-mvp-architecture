package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    private val collectionDao: CollectionDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveData(): LiveData<List<Collection>> = collectionDao.liveData()

    fun getRepoCollections(keyword: String): Flowable<Resource<BaseResponseAvgle<CollectionsResponseAvgle>>> {
        return createResource(
            avgleService.getVideoCollections(keyword),
            object : PlainConsumer<BaseResponseAvgle<CollectionsResponseAvgle>> {
                override fun accept(t: BaseResponseAvgle<CollectionsResponseAvgle>) {
                    if (t.success) {
                        Completable.fromAction {
                            collectionDao.deleteAll()
                            collectionDao.insertIgnore(t.response.collections)
                            collectionDao.updateIgnore(t.response.collections)
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : CompletableObserver {
                                override fun onComplete() {
                                    Log.d(CollectionRepo::class.java.simpleName, "onComplete")
                                }

                                override fun onSubscribe(d: Disposable) {
                                    Log.d(CollectionRepo::class.java.simpleName, "onSubscribe")
                                }

                                override fun onError(e: Throwable) {
                                    Log.d(CollectionRepo::class.java.simpleName, "onError")
                                }
                            })
                    }
                }
            })
    }
}