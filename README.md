<img src="https://user-images.githubusercontent.com/29915177/58609187-e9460780-82d0-11e9-943b-73d71d4f9de2.jpeg"/> 

# Kotlin Mvp Architecture Template 
This repository contains a detailed sample app that implements MVP architecture in Kotlin using Dagger2, Room, RxJava2...

# MVP Design Pattern

In Android we have a problem arising from the fact that activities are closely coupled to interface and data access mechanisms.

The Model-View-Presenter pattern allows to separate the presentation layer from the logic, so that everything about how the interface works is separated from how we represent it on screen.

MVP lets us to make views independent from data source and it will be easier to test each layer.

# Libraries  

* [Kotlin](https://kotlinlang.org/)
* [Room](https://developer.android.com/topic/libraries/architecture/room.html)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
* Android Support Libraries
* [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
* [Dagger 2 (2.11)](https://github.com/google/dagger)
* [Picasso](https://square.github.io/picasso/)
* [Retrofit](http://square.github.io/retrofit/)
* [OkHttp](http://square.github.io/okhttp/)
* [Gson](https://github.com/google/gson)
* [Timber](https://github.com/JakeWharton/timber)

# Dependency Injection: Dagger2
Dagger uses Components and Modules to define which dependencies will be injected in which objects. 

This application will contain an activity (named as MainActivity) and several fragments.

So we will need three components: ApplicationComponent, ActivityComponent (where presenter resides in) and FragmentComponent (where presenter reside in).
#### Here is AppComponent.kt :
```kotlin
@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    fun getDataBaseManager(): DatabaseManager

    fun getComicVineService(): ComicVineService

    fun getSharedPrefs(): SharedPrefs

    fun getConnectionLiveData(): ConnectionLiveData

    fun comicsDao(): ComicsDao

    fun issuesDao(): IssuesDao

    fun inject(app: MyApplication)
}
```

#### And AppModule.kt :
```kotlin
@Module
class AppModule(private var application: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
    }
}
```
It’s quite simple. It just injects application and provides it when needed.

Let’s assume, if we want to use Calligraphy, Crashliytcs, Timber or Stetho, application module should inject those, too.

#### ActivityComponent.kt
```kotlin
@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    @ActivityFragmentManager
    fun defaultFragmentManager(): FragmentManager

    fun lifeCycleOwner(): LifecycleOwner

    fun inject(activity: MainActivity)

    fun inject(activity: FeatureActivity)
}
```

#### FragmentComponent.kt
```kotlin
@PerFragment
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun lifeCycleOwner(): LifecycleOwner

    //inject fragment
}
```
 
#### NetworkModule.kt
```kotlin
@Module
class NetworkModule(private var context: Application) {

    @Provides
    @OkHttpNoAuth
    @Singleton
    internal fun provideOkHttpClientNoAuth(): OkHttpClient {
        return makeOkHttpClientBuilder(context).build()
    }

    @Provides
    @Singleton
    internal fun provideGithubService(gson: Gson, @OkHttpNoAuth okHttpClient: OkHttpClient): ComicVineService {
        return makeService(ComicVineService::class.java, gson, okHttpClient)
    }

    @Singleton
    @Provides
    internal fun providesConnectionLiveData(): ConnectionLiveData {
        return ConnectionLiveData(context)
    }
}
```
#### DataModule.kt
```kotlin
@Module
class DataModule(private var context: Application) {

    @Singleton
    @Provides
    fun providesRoomDatabase(): DatabaseManager {
        return Room.databaseBuilder(context, DatabaseManager::class.java, "my-database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideComicsDao(databaseManager: DatabaseManager): ComicsDao {
        return databaseManager.comicsDao()
    }

    @Provides
    @Singleton
    fun provideIssuesDao(databaseManager: DatabaseManager): IssuesDao {
        return databaseManager.issuesDao()
    }

    @Singleton
    @Provides
    internal fun providesSharePeres(): SharedPrefs {
        return SharedPrefs.getInstance(context)
    }
}
```

# Repository

<img src="https://user-images.githubusercontent.com/29915177/58611991-38913580-82db-11e9-80ce-3b289efabc83.png"/> 

Why the Repository Pattern ?

 - Decouples the application from the data sources

 - Provides data from multiple sources (DB, API) without clients being concerned about this

 - Isolates the data layer

 - Single place, centralized, consistent access to data

 - Testable business logic via Unit Tests

 - Easily add new sources
 
 So our repository now talks to the API data source and with the cache data source. We would now want to add another source for our data, a database source.
 
 On Android, we have several options here :

- using pure SQLite (too much boilerplate)

- Realm ( too complex for our use case, we don’t need most of it’s features)

- GreenDao ( a good ORM, but I think they will focus the development on objectbox in the future)

- Room ( the newly introduced ORM from Google, good support for RXJava 2 )

 I will be using for my example Room, the new library introduced by Google.

#### Execute room
```kotlin
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
```
#### BaseRepo.kt
```kotlin
    /**
     * For single data
     * @param remote
     * @param onSave
     * @param <T>
     * @return
    </T> */
    fun <T> createResource(
        remote: Single<Response<T>>,
        onSave: PlainConsumer<T>
    ): Flowable<Resource<T>> {
        return Flowable.create({
            object : SimpleNetworkBoundSource<T>(it, true) {
                override fun getRemote(): Single<Response<T>> = remote
                override fun saveCallResult(data: T, isRefresh: Boolean) {
                    onSave.accept(data)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }
```
#### IssuesRepo.kt
```kotlin
    fun getRepoIssues(refresh: Boolean): Flowable<Resource<BaseResponse<Issues>>> {
        return createResource(refresh, comicVineService.getIssues2(
            100, offset, BuildConfig.API_KEY,
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
```

#### Presenter
```kotlin
    issuesDao.liveData().observe(getLifeCircleOwner(), Observer {
            mainAdapter?.updateNotes(it!!)
            getView()?.size(it!!.size)
            getView()?.hideProgress()
        })
```

### Contributing to Android Kotlin MVP Architecture
All pull requests are welcome, make sure to follow the [contribution guidelines](CONTRIBUTING.md)
when you submit pull request.
