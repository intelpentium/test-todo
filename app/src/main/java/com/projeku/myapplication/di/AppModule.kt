package com.projeku.myapplication.di

import android.content.Context
import androidx.room.Room
import com.projeku.myapplication.data.local.ToDoDao
import com.projeku.myapplication.data.local.ToDoDatabase
import com.projeku.myapplication.data.remote.ToDoApi
import com.projeku.myapplication.data.repository.ToDoRepositoryImpl
import com.projeku.myapplication.domain.repository.ToDoRepository
import com.projeku.myapplication.domain.usecase.GetToDoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideToDoApi(retrofit: Retrofit): ToDoApi =
        retrofit.create(ToDoApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ToDoDatabase =
        Room.databaseBuilder(appContext, ToDoDatabase::class.java, "todo_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideToDoDao(db: ToDoDatabase): ToDoDao = db.toDoDao()

    @Provides
    @Singleton
    fun provideToDoRepository(api: ToDoApi, dao: ToDoDao): ToDoRepository {
        val simulateOfflineFlag = false
        return ToDoRepositoryImpl(api = api, dao = dao, simulateOffline = simulateOfflineFlag)
    }

    @Provides
    fun provideGetToDoListUseCase(repository: ToDoRepository): GetToDoListUseCase =
        GetToDoListUseCase(repository)
}