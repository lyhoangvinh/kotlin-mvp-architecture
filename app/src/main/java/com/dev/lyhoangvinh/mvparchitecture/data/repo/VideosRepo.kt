package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import io.reactivex.Flowable
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

class VideosRepo @Inject constructor(
    private val videosDao: VideosDao,
    private val avgleService: AvgleService,
    private val backgroundThreadExecutor: BackgroundThreadExecutor
) : BaseRepo() {

    fun liveData(): LiveData<List<Video>> = videosDao.liveData()

    fun liveDataVideoAll(): LiveData<List<Video>> = videosDao.liveDataFromType(Constants.TYPE_VIDEO)

    fun liveDataSearch(): LiveData<List<Video>> = videosDao.liveDataFromType(Constants.TYPE_SEARCH)

    fun deleteAll() {
        BackgroundThreadExecutor.getInstance().runOnBackground {
            videosDao.deleteAll()
        }
    }

    fun getRepoVideos(
        isRefresh: Boolean,
        chId: String,
        page: Int
    ): Flowable<Resource<BaseResponseAvgle<VideosResponseAvgle>>> {
        return createResource(isRefresh, avgleService.getVideosFromKeyword(page, chId),
            onSave = object : OnSaveResultListener<BaseResponseAvgle<VideosResponseAvgle>> {
                override fun onSave(data: BaseResponseAvgle<VideosResponseAvgle>, isRefresh: Boolean) {
                    if (data.success) {
                        val videos = data.response.videos
                        for (x in 0 until videos.size) {
                            videos[x].type = Constants.TYPE_VIDEO
                        }
                        if (isRefresh) {
                            backgroundThreadExecutor.runOnBackground {
                                videosDao.deleteType(Constants.TYPE_VIDEO)
                                videosDao.insertIgnore(videos)
                                videosDao.updateIgnore(videos)
                            }
                        } else {
                            backgroundThreadExecutor.runOnBackground {
                                videosDao.insertIgnore(videos)
                                videosDao.updateIgnore(videos)
                            }
                        }
                    }
                }
            })
    }

    fun search(
        isRefresh: Boolean,
        query: String,
        page: Int
    ): Flowable<Resource<ResponseBiZip<BaseResponseAvgle<VideosResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>>> {
        return createResource(isRefresh, avgleService.searchVideos(query, page), avgleService.searchJav(query, page),
            object :
                OnSaveBiResultListener<BaseResponseAvgle<VideosResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>> {
                override fun onSave(
                    data: ResponseBiZip<BaseResponseAvgle<VideosResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>,
                    isRefresh: Boolean
                ) {

                    val data1 = data.t1
                    val data2 = data.t1
                    val newList: ArrayList<Video> = ArrayList()
                    if (data1 != null && data1.success && data1.response.videos.isNotEmpty()) {
                        newList.addAll(data1.response.videos)
                    }
                    if (data2 != null && data2.success && data2.response.videos.isNotEmpty()) {
                        newList.addAll(data2.response.videos)
                    }

                    for (x in 0 until newList.size) {
                        newList[x].type = Constants.TYPE_SEARCH
                    }
                    if (isRefresh) {
                        backgroundThreadExecutor.runOnBackground {
                            videosDao.deleteType(Constants.TYPE_SEARCH)
                            videosDao.insertIgnore(newList)
                            videosDao.updateIgnore(newList)
                        }
                    } else {
                        backgroundThreadExecutor.runOnBackground {
                            videosDao.insertIgnore(newList)
                            videosDao.updateIgnore(newList)
                        }
                    }

                }
            })
    }
}