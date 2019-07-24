package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.MediatorLiveData
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.*
import com.dev.lyhoangvinh.mvparchitecture.data.response.*
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipFourConsumer
import io.reactivex.Flowable
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val collectionDao: CollectionDao,
    private val videosDao: VideosDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    /**
     * Use MediatorLiveData To Query And Merge Multiple Data Source Type Into Single LiveData
     * link "https://code.luasoftware.com/tutorials/android/use-mediatorlivedata-to-query-and-merge-different-data-type/"
     */

    fun fetchData(): MediatorLiveData<MergedData> {
        val liveDataMerger = MediatorLiveData<MergedData>()
        liveDataMerger.addSource(categoriesDao.liveData()) {
            liveDataMerger.value = CategoryData(it!!)
        }
        liveDataMerger.addSource(collectionDao.liveDataFromType(Constants.TYPE_HOME_BANNER)) {
            liveDataMerger.value = CollectionBannerData(it!!)
        }
        liveDataMerger.addSource(collectionDao.liveDataFromType(Constants.TYPE_HOME_BOTTOM)) {
            liveDataMerger.value = CollectionBottomData(it!!)
        }
        liveDataMerger.addSource(videosDao.liveDataFromType(Constants.TYPE_HOME)) {
            liveDataMerger.value = VideoData(it!!)
        }
        return liveDataMerger
    }

    fun getRepoHome(): Flowable<Resource<ResponseFourZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>>> {
        return createResource(
            avgleService.getCategories(),
            avgleService.getCollections((1..10).random(), (3..5).random()),
            avgleService.getCollections(0, 20),
            avgleService.getAllVideos((0..10).random()),
            object :
                PlainResponseZipFourConsumer<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>> {
                override fun accept(dto: ResponseFourZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                    execute {
                        categoriesDao.deleteAll()
                        collectionDao.deleteAll()
                        videosDao.deleteType(Constants.TYPE_HOME)

                        categoriesDao.insertIgnore(dto.t1?.response?.categories!!)
                        categoriesDao.updateIgnore(dto.t1?.response?.categories!!)

                        val collectionsHome = dto.t2?.response?.collections!!
                        for (x in 0 until collectionsHome.size) {
                            collectionsHome[x].type = Constants.TYPE_HOME_BANNER
                        }
                        collectionDao.insertIgnore(collectionsHome)
                        collectionDao.updateIgnore(collectionsHome)

                        val collectionsHomeBottom = dto.t3?.response?.collections!!
                        for (x in 0 until collectionsHome.size) {
                            collectionsHomeBottom[x].type = Constants.TYPE_HOME_BOTTOM
                        }
                        collectionDao.insertIgnore(collectionsHomeBottom)
                        collectionDao.updateIgnore(collectionsHomeBottom)

                        val videos = dto.t4?.response?.videos!!
                        for (x in 0 until videos.size) {
                            videos[x].type = Constants.TYPE_HOME
                        }
                        videosDao.insertIgnore(videos)
                        videosDao.updateIgnore(videos)
                    }
                }
            })
    }
}