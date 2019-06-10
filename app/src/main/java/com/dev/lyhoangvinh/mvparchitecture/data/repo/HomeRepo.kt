package com.dev.lyhoangvinh.mvparchitecture.data.repo

import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.response.*
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipBiConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipThreeConsumer
import io.reactivex.Flowable
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val collectionDao: CollectionDao,
    private val videosDao: VideosDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveCategories() = categoriesDao.liveData()

    fun liveCollection() = collectionDao.liveData()

    fun liveVideos() = videosDao.liveData()

    fun getRepoHome(): Flowable<Resource<ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>>> {
        return createResource(
            avgleService.getCategories(),
            avgleService.getCollections((0..10).random(), 3),
            avgleService.getAllVideos(0),
            object :
                PlainResponseZipThreeConsumer<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>> {
                override fun accept(dto: ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                    execute {
                        categoriesDao.deleteAll()
                        collectionDao.deleteAll()
                        videosDao.deleteAll()

                        categoriesDao.insertIgnore(dto.t1?.response?.categories!!)
                        categoriesDao.updateIgnore(dto.t1?.response?.categories!!)

                        collectionDao.insertIgnore(dto.t2?.response?.collections!!)
                        collectionDao.updateIgnore(dto.t2?.response?.collections!!)

                        videosDao.insertIgnore(dto.t3?.response?.videos!!)
                        videosDao.updateIgnore(dto.t3?.response?.videos!!)
                    }
                }
            })
    }
}