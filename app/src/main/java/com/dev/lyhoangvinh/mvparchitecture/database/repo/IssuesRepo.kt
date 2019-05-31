package com.dev.lyhoangvinh.mvparchitecture.database.repo

import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.database.Resource
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import com.dev.lyhoangvinh.mvparchitecture.ui.base.api.ComicVineService
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


import javax.inject.Inject

class IssuesRepo @Inject constructor(private val comicVineService: ComicVineService, private val issuesDao: IssuesDao) :
    BaseRepo() {

    private var offset = 0

    fun getRepoIssues(refresh: Boolean): Flowable<Resource<BaseResponse<Issues>>> {
        return createResource(refresh, comicVineService.getIssues2(
            100, offset, Constants.KEY,
            "json",
            "cover_date: desc"
        ), onSave = object : OnSaveResultListener<BaseResponse<Issues>> {
            override fun onSave(data: BaseResponse<Issues>, isRefresh: Boolean) {
                offset = if (refresh) 0 else data.offset!! + 1
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