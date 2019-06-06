package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Issues
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseComic
import com.dev.lyhoangvinh.mvparchitecture.data.services.ComicVineService
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


import javax.inject.Inject

class IssuesRepo @Inject constructor(private val comicVineService: ComicVineService, private val issuesDao: IssuesDao) :
    BaseRepo() {

    fun liveData() = issuesDao.liveData()

    fun delete(t: Issues) = issuesDao.delete(t)

    fun insert(t: Issues) = issuesDao.insert(t)

    fun getRepoIssues(refresh: Boolean, offset: Int): Flowable<Resource<BaseResponseComic<Issues>>> {
        return createResource(refresh, comicVineService.getIssues2(
            100, offset, Constants.KEY,
            "json",
            "cover_date: desc"
        ), onSave = object : OnSaveResultListener<BaseResponseComic<Issues>> {
            override fun onSave(data: BaseResponseComic<Issues>, isRefresh: Boolean) {
                if (data.results.isNotEmpty()) {
                    upsert(data.results)
                }
            }
        })
    }

    private fun upsert(entities: List<Issues>) {
        Completable.fromAction {
            issuesDao.insertIgnore(entities)
            issuesDao.updateIgnore(entities)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d(IssuesRepo::class.java.simpleName, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d(IssuesRepo::class.java.simpleName, "onSubscribe")
                }

                override fun onError(e: Throwable) {
                    Log.d(IssuesRepo::class.java.simpleName, "onError")
                }
            })
    }
}