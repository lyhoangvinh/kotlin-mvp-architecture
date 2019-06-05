package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
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

    fun getRepoVideos(isRefresh: Boolean, chId: String, page:Int): Flowable<Resource<BaseResponseAvgle<VideosResponseAvgle>>> {
        return createResource(isRefresh, avgleService.getVideosFromKeyword(page, chId),
            onSave = object : OnSaveResultListener<BaseResponseAvgle<VideosResponseAvgle>> {
                override fun onSave(data: BaseResponseAvgle<VideosResponseAvgle>, isRefresh: Boolean) {
                    if (data.success && data.response.hasMore) {
                        if (isRefresh) {
//                            execute(
//                                videosDao.deleteAll()
//                                videosDao.insertIgnore(data.response.videos)
//                                videosDao.updateIgnore(data.response.videos)
//                            )
                            BackgroundThreadExecutor.getInstance().runOnBackground {
                                videosDao.deleteAll()
                                videosDao.insertIgnore(data.response.videos)
                                videosDao.updateIgnore(data.response.videos)
                            }
                        } else {
//                            execute({
//                                videosDao.insertIgnore(data.response.videos)
//                                videosDao.updateIgnore(data.response.videos)
//                            }, { currentPage += 1 })
                            BackgroundThreadExecutor.getInstance().runOnBackground {
                                videosDao.insertIgnore(data.response.videos)
                                videosDao.updateIgnore(data.response.videos)
                            }
                        }
                    }
                }
            })
    }
}