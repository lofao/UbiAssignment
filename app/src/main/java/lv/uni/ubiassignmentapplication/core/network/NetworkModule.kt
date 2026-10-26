package lv.uni.ubiassignmentapplication.core.network

import io.ktor.client.HttpClient
import io.ktor.serialization.kotlinx.json.DefaultJson
import kotlinx.serialization.json.Json
import lv.uni.ubiassignmentapplication.core.ktorLogger
import org.koin.dsl.module

val NetworkModule = module {

    single<KtorLogger> { ktorLogger }

    single<HttpClient> {
        httpClient(
            json = get<Json>(),
            logger = get<KtorLogger>(),
            baseUrl = "https://static.ui.com",
            apiToken = "",
        )
    }

    single<Json> { defaultJson() }

    single<NetworkMonitor> {
        ConnectivityNetworkMonitor(context = get())
    }

}

internal fun defaultJson() = Json(DefaultJson) {
    explicitNulls = false
    ignoreUnknownKeys = true
//    serializersModule = SerializersModule {
//        include(bigIntegerhumanReadableSerializerModule)
//        include(bigDecimalHumanReadableSerializerModule)
//    }
}
