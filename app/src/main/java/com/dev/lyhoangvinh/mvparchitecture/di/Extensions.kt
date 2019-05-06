package com.dev.lyhoangvinh.mvparchitecture.di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent
import com.google.gson.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import lyhoangvinh.com.myutil.network.Tls12SocketFactory
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


fun getAppComponent(fragment: Fragment): AppComponent? {
    return getAppComponent(fragment.activity)
}

fun getAppComponent(activity: Activity?): AppComponent? {
    return activity?.let { MyApplication.getInstance().get(it).getAppComponent() }
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}


fun <T> makeService(serviceClass: Class<T>, gson: Gson, okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    return retrofit.create(serviceClass)
}

fun makeOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
    val logging = HttpLoggingInterceptor()

    if (BuildConfig.DEBUG) {
        logging.level = HttpLoggingInterceptor.Level.BODY
    }

    // : 4/26/2017 add the UnauthorisedInterceptor to this retrofit, or 401
    var builder: OkHttpClient.Builder? = null
    builder = OkHttpClient.Builder()
//        .addInterceptor(UnauthorisedInterceptor(context))
        .addInterceptor(logging)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .cache(null)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    // add cache to client
    val baseDir = context.cacheDir
    if (baseDir != null) {
        val cacheDir = File(baseDir, "HttpResponseCache")
        builder!!.cache(Cache(cacheDir, (10 * 1024 * 1024).toLong())) // 10 MB
    }

    return enableTls12OnPreLollipop(builder)
}

/**
 * Enable TLS 1.2 on Pre Lollipop android versions
 * @param client OkHttpClient Builder
 * @return builder with SSL Socket Factory set
 * according to [OkHttpClient.Builder.sslSocketFactory] deprecation,
 * Please add config SSL with [X509TrustManager] by using [CustomTrustManager]
 * * how to enable tls on android 4.4
 * [](https://github.com/square/okhttp/issues/2372)
 */
@SuppressLint("ObsoleteSdkInt")
fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
    if (Build.VERSION.SDK_INT in 16..21) {
        try {
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            // TODO: 9/7/17 set SSL socket factory with X509TrustManager
            client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            val specs = ArrayList<ConnectionSpec>()
            specs.add(cs)
            specs.add(ConnectionSpec.COMPATIBLE_TLS)
            specs.add(ConnectionSpec.CLEARTEXT)

            client.connectionSpecs(specs)
        } catch (exc: Exception) {
            Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
        }

    }

    return client
}

fun <T> makeRequestSingle(
    request: Single<Response<T>>,
    shouldUpdateUi: Boolean,
    responseConsumer: PlainConsumer<T>,
    onComplete: Action?
): Disposable {

    var single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
    if (shouldUpdateUi) {
        single = single.observeOn(AndroidSchedulers.mainThread())
    }

    return single.subscribe({ response ->
        if (response.isSuccessful) {
            response.body()?.let { responseConsumer.accept(it) }
        }
        onComplete?.run()
    }, { throwable ->
        if (throwable is RuntimeException) {
            // must be fixed while developing
            throw Exception(throwable)
        }
//            // handle error
//            if (errorConsumer != null) {
//                errorConsumer!!.accept(ErrorEntity.getError(throwable))
//            }
        onComplete?.run()
    })
}

/**
 * Make gson with custom date time deserializer
 * @return [Gson] object
 */
fun makeGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateDeserializer())
        .create()
}

class DateDeserializer : JsonDeserializer<Date> {
    @SuppressLint("SimpleDateFormat")
    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
        val date = element.asString
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        try {
            return formatter.parse(date)
        } catch (e: ParseException) {
            return null
        }

    }
}