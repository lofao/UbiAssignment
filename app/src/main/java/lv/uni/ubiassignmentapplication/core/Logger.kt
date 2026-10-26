package lv.uni.ubiassignmentapplication.core

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import lv.uni.ubiassignmentapplication.core.network.KtorLogger

val ktorLogger: KtorLogger
    get() {
        val params = LoggingParams.Ktor
        val logger = Logger(
            loggerConfigInit(platformLogWriter(), minSeverity = params.severity),
            params.tag,
        )
        return object : KtorLogger {
            override fun log(message: String) {
                logger.i(message)
            }
        }
    }

private enum class LoggingParams(val severity: Severity, val tag: String) {
    Analytics(Severity.Info, "analytics"),
    AppException(Severity.Error, "app_exception"),
    Koin(Severity.Info, "koin"),
    Ktor(Severity.Info, "ktor"),
}