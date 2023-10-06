package com.algostack.stacknote.di

import android.provider.ContactsContract.CommonDataKinds.Note
import com.algostack.stacknote.api.AuthInterceptor
import com.algostack.stacknote.api.NoteApi
import com.algostack.stacknote.api.UserApi
import com.algostack.stacknote.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)


    }



    @Singleton
    @Provides
    fun provideUserApi(retrofitBulder: Retrofit.Builder) : UserApi{
        return retrofitBulder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesNoteApi(retrofitBulder: Retrofit.Builder, okHttpClient: OkHttpClient) : NoteApi {
        return  retrofitBulder
            .client(okHttpClient)
            .build().create(NoteApi::class.java)
    }

}