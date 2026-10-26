package lv.uni.ubiassignmentapplication

import org.koin.dsl.module

fun appModule(application: UbiApplication) = module {

    single<UbiApplication> { return@single application }

}

