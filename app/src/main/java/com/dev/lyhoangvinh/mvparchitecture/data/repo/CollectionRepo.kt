package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.Flowable
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    private val collectionDao: CollectionDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveData(): LiveData<List<Collection>> = collectionDao.liveDataFromType(Constants.TYPE_ALL)

    fun getRepoCollections(
        isRefresh: Boolean,
        page: Int
    ): Flowable<Resource<BaseResponseAvgle<CollectionsResponseAvgle>>> {
        return createResource(isRefresh, avgleService.getCollections(page, 50),
            onSave = object : OnSaveResultListener<BaseResponseAvgle<CollectionsResponseAvgle>> {
                override fun onSave(data: BaseResponseAvgle<CollectionsResponseAvgle>, isRefresh: Boolean) {
                    if (data.success) {
                        val collections = data.response.collections
                        for (x in 0 until collections.size) {
                            collections[x].type = Constants.TYPE_ALL
                        }
                        if (isRefresh) {
                            execute {
                                collectionDao.deleteType(Constants.TYPE_ALL)
                                collectionDao.insertIgnore(collections)
                                collectionDao.updateIgnore(collections)
                            }
                        } else {
                            execute {
                                collectionDao.insertIgnore(collections)
                                collectionDao.updateIgnore(collections)
                            }
                        }
                    }
                }
            })
    }
}