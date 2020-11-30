package com.mygallary.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mygallary.di.ViewModelFactory
import com.mygallary.viewmodel.ImagePreviewViewModel
import com.mygallary.viewmodel.SearchImageViewModel
import com.mygallary.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

 @Binds
 abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


 @Binds
 @IntoMap
 @ViewModelKey(SearchImageViewModel::class)
 abstract fun bindSearchImageViewModel(movieHomeViewModel: SearchImageViewModel): ViewModel

 @Binds
 @IntoMap
 @ViewModelKey(ImagePreviewViewModel::class)
 abstract fun bindImagePreviewViewModel(mainActivityViewModel: ImagePreviewViewModel): ViewModel

}