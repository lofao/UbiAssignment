package lv.uni.ubiassignmentapplication.di

import io.ktor.client.HttpClient
import lv.uni.ubiassignmentapplication.UbiApplication
import lv.uni.ubiassignmentapplication.core.ktorLogger
import lv.uni.ubiassignmentapplication.core.network.KtorLogger
import lv.uni.ubiassignmentapplication.core.network.httpClient
import org.koin.dsl.module

fun appModule(application: UbiApplication) = module {

    single<UbiApplication> { return@single application }

}

