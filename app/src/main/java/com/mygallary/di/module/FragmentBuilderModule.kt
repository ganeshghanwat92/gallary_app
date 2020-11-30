package com.mygallary.di.module

import com.mygallary.ui.ImagePreviewFragment
import com.mygallary.ui.SearchImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchImageFragment() : SearchImageFragment

    @ContributesAndroidInjector
    abstract fun contributeImagePreviewFragment() : ImagePreviewFragment



}