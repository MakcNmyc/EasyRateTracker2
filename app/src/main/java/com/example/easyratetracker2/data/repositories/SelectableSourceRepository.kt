package com.example.easyratetracker2.data.repositories

import android.content.Context
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.work.*
import com.example.easyratetracker2.MultilingualSup
import com.example.easyratetracker2.MultilingualSup.Companion.SUPPORTED_LANGUAGE_EN
import com.example.easyratetracker2.MultilingualSup.Companion.SUPPORTED_LANGUAGE_RU
import com.example.easyratetracker2.data.dao.SelectableSourceDao
import com.example.easyratetracker2.data.models.SourceSelectionModel
import com.example.easyratetracker2.data.repositories.utilities.StorageRequest
import com.example.easyratetracker2.data.store.room.SelectableSource
import com.example.easyratetracker2.di.AppEntryPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectableSourceRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val selectableDao: SelectableSourceDao
) : RoomRepository<SelectableSource>(selectableDao) {

    private var supportedLanguages: List<String> = listOf(
        SUPPORTED_LANGUAGE_RU,
        SUPPORTED_LANGUAGE_EN
    )

    private var currentLanguage = MultilingualSup.takeLangByLangCollection(
        supportedLanguages,
        MultilingualSup.getPrimaryLanguage(context)
    )

    private suspend fun checkSelectableSources() {
        if(!selectableDao.selectableResourcesIsInitialized(currentLanguage)) initSelectableSources(currentLanguage)
    }

    private fun initSelectableSources(lang: String) {
        Data.Builder().apply {
            putString(
                FILENAME_PREPOPULATE_PARAM,
                takeFileName(lang)
            )
            putString(LANGUAGE_PARAM, lang)
        }.let { dataBuilder ->
            WorkManager.getInstance(context).enqueue(
                OneTimeWorkRequest.Builder(SelectableSourcesWorker::class.java)
                    .setInputData(dataBuilder.build())
                    .build()
            )
        }
    }

    suspend fun getAllSourcesForList() : PagingSource<Int, SourceSelectionModel>{
        checkSelectableSources()
        return selectableDao.getAllSourcesForList(currentLanguage)
    }

    private fun takeFileName(lang: String) =
        when (lang) {
            SUPPORTED_LANGUAGE_RU -> "sources_ru.json"
            SUPPORTED_LANGUAGE_EN -> "sources_en.json"
            else -> throw IndexOutOfBoundsException()
        }

    class SelectableSourcesWorker(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {

        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            try {
                fromApplication(applicationContext, AppEntryPoint::class.java)
                    .createSelectableSourceRepository()
                    .saveToDb(
                        StorageRequest(
                            getSelectableSourceForSave(
                                applicationContext,
                                inputData.getString(FILENAME_PREPOPULATE_PARAM)!!,
                                inputData.getString(LANGUAGE_PARAM)!!
                            ))
                    )
                Result.success()
            } catch (e: IOException) {
                Log.e(LogTag, "Error with populate selectable sources " + e.message)
                Result.failure()
            }
        }

        companion object {

            @Throws(IOException::class)
            fun getSelectableSourceFromAssert(
                context: Context,
                filename: String
            ): List<SelectableSource> = context.assets.open(filename).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    Gson().fromJson(jsonReader,
                        object : TypeToken<List<SelectableSource>>() {}.type
                    )
                }
            }


            @Throws(IOException::class)
            fun getSelectableSourceForSave(
                context: Context,
                filename: String,
                lang: String
            ): List<SelectableSource>
                = getSelectableSourceFromAssert(context, filename).onEach { v -> v.language = lang }
        }
    }

    companion object {
        const val FILENAME_PREPOPULATE_PARAM = "sourceFilename"
        const val LANGUAGE_PARAM = "language"
        var LogTag = "SelectableSourceRepository"
    }
}