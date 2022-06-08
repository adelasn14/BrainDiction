package com.example.braindiction.paginglistpatient

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.braindiction.api.ApiService
import com.example.braindiction.api.PatientData
import com.example.braindiction.preference.LoginSession

class PatientListPagingSource(private val apiService: ApiService) :
    PagingSource<Int, PatientData>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PatientData> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData =
                apiService.displayAllPatient(position, params.loadSize)
            val data = responseData
            Log.d("PagingSource", "data : $data")
            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, PatientData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}