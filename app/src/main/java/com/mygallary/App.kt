package com.mygallary

import android.app.Application
import com.mygallary.di.component.AppComponent
import com.mygallary.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
class App : Application(), HasAndroidInjector {


    lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        appComponent.inject(this@App)


    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}