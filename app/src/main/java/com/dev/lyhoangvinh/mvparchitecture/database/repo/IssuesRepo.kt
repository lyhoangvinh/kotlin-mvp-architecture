package com.dev.lyhoangvinh.mvparchitecture.database.repo

import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.dev.lyhoangvinh.mvparchitecture.database.Resource
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import com.dev.lyhoangvinh.mvparchitecture.ui.base.api.ComicVineService
import io.reactivex.Flowable
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor

import javax.inject.Inject

class IssuesRepo @Inject constructor(private val comicVineService: ComicVineService, private val issuesDao: IssuesDao) :
    BaseRepo() {

    private var offset = 0

    fun getRepoIssues(refresh: Boolean): Flowable<Resource<BaseResponse<Issues>>> {
        return createResource(refresh, comicVineService.getIssues2(
            100, offset, BuildConfig.API_KEY,
            "json",
            "cover_date: desc"
        ), onSave = object : OnSaveResultListener<BaseResponse<Issues>> {
            override fun onSave(data: BaseResponse<Issues>, isRefresh: Boolean) {
                if (refresh) {
                    offset = -1
                    issuesDao.removeAll()
                }
                offset = if (offset == -1) 0 else data.offset!! + 1
                if (data.results.isNotEmpty()) {
                    BackgroundThreadExecutor.getInstance().runOnBackground {
                        upsert(data.results)
                    }
                }
            }
        })
    }

    private fun upsert(entities: List<Issues>) {
        BackgroundThreadExecutor.getInstance().runOnBackground {
            issuesDao.insertIgnore(entities)
            issuesDao.updateIgnore(entities)
        }
    }
}