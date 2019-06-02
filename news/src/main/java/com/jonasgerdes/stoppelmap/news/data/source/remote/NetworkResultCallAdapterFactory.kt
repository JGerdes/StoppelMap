package com.jonasgerdes.stoppelmap.news.data.source.remote

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/*
class NetworkResultCallAdapterFactory : CallAdapter.Factory() {


    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class NetworkResultCallAdapter<SuccessType : Any, ErrorType : Any>(
        private val successBodyType: Type,
        private val delegateAdapter: CallAdapter<SuccessType, Deferred<SuccessType>>,
        private val errorConverter: Converter<ResponseBody, ErrorType>
    ) : CallAdapter<SuccessType, Any> {

        override fun adapt(call: Call<SuccessType>): Any {
            async { delegateAdapter.adapt(call).await() }
        }

        override fun responseType(): Type {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


    }
}*/
