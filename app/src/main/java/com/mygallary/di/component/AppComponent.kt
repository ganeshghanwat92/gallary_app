package com.mygallary.di.component


import android.app.Application
import com.mygallary.App
import com.mygallary.di.module.AppModule
import com.mygallary.di.module.MainActivityModule
import com.mygallary.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, MainActivityModule::class, AppModule::class, ViewModelModule::class])
interface AppComponent {

   fun inject(app: App)

   @Component.Builder
   interface Builder {
      fun build(): AppComponent

      @BindsInstance
      fun application(application: Application): Builder
   }

}