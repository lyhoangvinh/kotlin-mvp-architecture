package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
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
    private val avgleService: AvgleService
) : BaseRepo() {

    fun liveData(): LiveData<List<Video>> = videosDao.liveData()

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
                        if (isRefresh) {
                            BackgroundThreadExecutor.getInstance().runOnBackground {
                                videosDao.deleteAll()
                                videosDao.insertIgnore(data.response.videos)
                                videosDao.updateIgnore(data.response.videos)
                            }
                        } else {
                            BackgroundThreadExecutor.getInstance().runOnBackground {
                                videosDao.insertIgnore(data.response.videos)
                                videosDao.updateIgnore(data.response.videos)
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
                    if (isRefresh) {
                        BackgroundThreadExecutor.getInstance().runOnBackground {
                            videosDao.deleteAll()
                            videosDao.insertIgnore(newList)
                            videosDao.updateIgnore(newList)
                        }
                    } else {
                        BackgroundThreadExecutor.getInstance().runOnBackground {
                            videosDao.insertIgnore(newList)
                            videosDao.updateIgnore(newList)
                        }
                    }

                }
            })
    }
}