package lv.uni.ubiassignmentapplication.di

import lv.uni.ubiassignmentapplication.UbiApplication
import org.koin.dsl.module

fun appModule(application: UbiApplication) = module {

    single<UbiApplication> { return@single application }

}

