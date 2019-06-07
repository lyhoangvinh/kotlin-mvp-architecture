package com.dev.lyhoangvinh.mvparchitecture.data.repo

import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipBiConsumer
import io.reactivex.Flowable
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val collectionDao: CollectionDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveCategories() = categoriesDao.liveData()

    fun liveCollection() = collectionDao.liveData()

    fun getRepoHome(): Flowable<Resource<ResponseBiZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>>>> {
        return createResource(
            avgleService.getCategories(),
            avgleService.getCollections(0, 10),
            object :
                PlainResponseZipBiConsumer<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>> {
                override fun accept(dto: ResponseBiZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>>) {
                    execute {
                        categoriesDao.deleteAll()
                        collectionDao.deleteAll()

                        categoriesDao.insertIgnore(dto.t1?.response?.categories!!)
                        categoriesDao.updateIgnore(dto.t1?.response?.categories!!)

                        collectionDao.insertIgnore(dto.t2?.response?.collections!!)
                        collectionDao.updateIgnore(dto.t2?.response?.collections!!)
                    }
                }
            })
    }
}