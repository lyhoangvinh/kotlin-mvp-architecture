package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CollectionDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.VideosDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    fun getRepoVideos(chId: String): Flowable<Resource<BaseResponseAvgle<VideosResponseAvgle>>> {
        return createResource(
            avgleService.getVideosFromKeyword(0, chId),
            object : PlainConsumer<BaseResponseAvgle<VideosResponseAvgle>> {
                override fun accept(t: BaseResponseAvgle<VideosResponseAvgle>) {
                    if (t.success) {
                        Completable.fromAction {
                            videosDao.deleteAll()
                            videosDao.insertIgnore(t.response.videos)
                            videosDao.updateIgnore(t.response.videos)
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : CompletableObserver {
                                override fun onComplete() {
                                    Log.d(VideosRepo::class.java.simpleName, "onComplete")
                                }

                                override fun onSubscribe(d: Disposable) {
                                    Log.d(VideosRepo::class.java.simpleName, "onSubscribe")
                                }

                                override fun onError(e: Throwable) {
                                    Log.d(VideosRepo::class.java.simpleName, "onError")
                                }
                            })
                    }
                }
            })
    }
}