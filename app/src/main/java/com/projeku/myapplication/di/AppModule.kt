package com.projeku.myapplication.di

import android.content.Context
import androidx.room.Room
import com.projeku.myapplication.data.local.ToDoDao
import com.projeku.myapplication.data.local.ToDoDatabase
import com.projeku.myapplication.data.remote.ToDoApi
import com.projeku.myapplication.data.repository.ToDoRepositoryImpl
import com.projeku.myapplication.domain.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/todos"

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ToDoApi = retrofit.create(ToDoApi::class.java)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): ToDoDatabase =
        Room.databaseBuilder(ctx, ToDoDatabase::class.java, "todo.db").build()

    @Provides
    fun provideDao(db: ToDoDatabase): ToDoDao = db.toDoDao()

    @Provides
    @Singleton
    fun provideRepo(api: ToDoApi, dao: ToDoDao): ToDoRepository =
        ToDoRepositoryImpl(api, dao, simulateOffline = false)


}