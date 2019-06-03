package com.dev.lyhoangvinh.mvparchitecture.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.ErrorEntity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.Filter
import com.google.gson.*
import com.squareup.picasso.Picasso
import io.reactivex.CompletableObserver
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

fun ImageView.loadImage(url: String) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ic_placeholder_rectangle_200px)
        .error(R.drawable.ic_placeholder_rectangle_200px)
        .centerCrop()
        .fit()
        .into(this)
}

fun Activity.showToastMessage(message: String) {
    if (!message.isEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToastMessage(message: String) {
    if (this.context != null && !message.isEmpty()) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard(editText: EditText) {
    activity?.showKeyboard(editText)
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(yourEditText: EditText) {
    try {
        val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        input.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        input.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT)
    } catch (ignored: Exception) {
    }
}

fun clearText(vararg textViews: TextView) {
    textViews.iterator().forEach { it.text = "" }
}

fun TextView.startCustomAnimation(isCollapsing: Boolean, finalText: String, duration: Long) {
    cancelAnimation()
    val mStartText = this.text
    val animator =
        if (isCollapsing) ValueAnimator.ofFloat(1.0f, 0.0f)
        else ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.addUpdateListener {
        val currentValue = it.animatedValue as Float
        val ended = (isCollapsing && currentValue == 0.0f) || (!isCollapsing && currentValue == 1.0f)
        if (ended) {
            this.text = finalText
        } else {
            val n = (mStartText.length * currentValue).toInt()
            val text = mStartText.substring(0, n)
            if (text != this.text) {
                this.text = text
            }
        }
    }
    this.tag = animator
    animator.duration = duration
    animator.start()
}

fun TextView.startExpandingAnimation(text: String, duration: Long) {
    cancelAnimation()
    val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.addUpdateListener {
        val currentValue = it.animatedValue as Float
        val ended = currentValue == 1.0f
        if (ended) {
            this.text = text
        } else {
            val n = (text.length * currentValue).toInt()
            val currentText = text.substring(0, n)
            if (currentText != this.text) {
                this.text = currentText
            }
        }
    }
    this.tag = animator
    animator.duration = duration
    animator.start()
}

fun TextView.startCollapsingAnimation(finalText: String, duration: Long) {
    this.startCustomAnimation(true, finalText, duration)
}

fun TextView.cancelAnimation() {
    val o = this.tag
    if (o != null && o is ValueAnimator) {
        o.cancel()
    }
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun <T> makeService(serviceClass: Class<T>, gson: Gson, okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.ENDPOINT)
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

fun <T> makeRequest(
    request: Single<Response<T>>,
    shouldUpdateUi: Boolean,
    @NonNull responseConsumer: PlainConsumer<T>,
    @Nullable errorConsumer: PlainConsumer<ErrorEntity>,
    @Nullable onComplete: Action?
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
        // handle error
        errorConsumer.accept(ErrorEntity.getError(throwable))
        onComplete?.run()
    })
}

fun <T> makeRequest(
    @NonNull request: Single<Response<T>>, shouldUpdateUi: Boolean,
    @Nullable responseConsumer: PlainConsumer<T>,
    @Nullable errorConsumer: PlainConsumer<ErrorEntity>
): Disposable {
    return makeRequest(request, shouldUpdateUi, responseConsumer, errorConsumer, null)
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

fun getAppDateFormatter(createdDate: String): String? {
    var out: String? = null
    var date_formatter: Date? = null
    if (!TextUtils.isEmpty(createdDate)) {
        date_formatter = parseToDate(createdDate)
    }
    if (date_formatter != null) {
        out = formatToDate(date_formatter)
    }
    if (TextUtils.isEmpty(out)) {
        out = createdDate
    }
    return out
}

fun formatToDate(date: Date): String {
    var result = ""
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        result = sdf.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun parseToDate(date: String?): Date? {
    var d: Date? = null
    if (!TextUtils.isEmpty(date)) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        //sdf.setTimeZone(...);
        try {
            d = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }
    return d
}

class FilterList<E> {
    fun <T> filterList(originalList: List<T>, filter: Filter<T, E>, text: E): List<T> {
        val filterList = ArrayList<T>()
        for (obj in originalList) {
            if (filter.isMatched(obj, text)) {
                filterList.add(obj)
            }
        }
        return filterList
    }
}

fun getRandomMaterialColor(context: Context): Int {
    var returnColor = Color.GRAY
    val arrayId = context.resources.getIdentifier("mdcolor_500", "array", context.packageName)

    if (arrayId != 0) {
        val colors = context.resources.obtainTypedArray(arrayId)
        val index = (Math.random() * colors.length()).toInt()
        returnColor = colors.getColor(index, Color.GRAY)
        colors.recycle()
    }
    return returnColor
}

fun getRandomColorDrawable(ctx: Context): ColorDrawable {
    return ColorDrawable(getRandomMaterialColor(ctx))
}

interface CompleteCompletableObserver : CompletableObserver {
    override fun onError(e: Throwable) {}

    override fun onSubscribe(d: Disposable) {}
}


