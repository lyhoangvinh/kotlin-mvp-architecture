package com.dev.lyhoangvinh.mvparchitecture.data.repo

import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosHomeDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.VideosHome
import com.dev.lyhoangvinh.mvparchitecture.data.response.*
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipThreeConsumer
import io.reactivex.Flowable
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val collectionDao: CollectionDao,
    private val videosDao: VideosDao,
    private val videosHomeDao: VideosHomeDao,
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveCategories() = categoriesDao.liveData()

    fun liveCollection() = collectionDao.liveData()

    fun liveVideos() = videosDao.liveData()

    fun liveVideosHome() = videosHomeDao.liveData()

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

    fun getRepoHomeMap(): Flowable<Resource<ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>>> {
        return createResource(
            avgleService.getCategories(),
            avgleService.getCollections((0..10).random(), 3),
            avgleService.getAllVideos(0),
            object :
                PlainResponseZipThreeConsumer<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>> {
                override fun accept(dto: ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                    val videosHomes = ArrayList<VideosHome>()
                    val videos = dto.t3?.response?.videos!!
                    for (video in videos) {
                        videosHomes.add(
                            VideosHome(
                                vid = video.vid,
                                uid = video.uid,
                                title = video.title
                                ,
                                keyword = video.keyword,
                                channel = video.channel,
                                duration = video.duration
                                ,
                                framerate = video.framerate,
                                hd = video.hd,
                                addTime = video.addTime
                                ,
                                viewNumber = video.viewNumber,
                                likes = video.likes,
                                dislikes = video.dislikes
                                ,
                                videoUrl = video.videoUrl,
                                embeddedUrl = video.embeddedUrl,
                                previewUrl = video.previewUrl
                                ,
                                previewVideoUrl = video.previewVideoUrl
                            )
                        )
                    }
                    execute {

                        categoriesDao.deleteAll()
                        collectionDao.deleteAll()

                        categoriesDao.insertIgnore(dto.t1?.response?.categories!!)
                        categoriesDao.updateIgnore(dto.t1?.response?.categories!!)

                        collectionDao.insertIgnore(dto.t2?.response?.collections!!)
                        collectionDao.updateIgnore(dto.t2?.response?.collections!!)

                        videosHomeDao.deleteAll()
                        videosHomeDao.insertIgnore(videosHomes)
                        videosHomeDao.updateIgnore(videosHomes)
                    }
                }
            })
    }
}