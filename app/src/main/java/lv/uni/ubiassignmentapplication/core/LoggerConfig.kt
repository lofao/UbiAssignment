package lv.uni.ubiassignmentapplication.core

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity


/**
 * Factory function to create a StaticConfig
 */
fun loggerConfigInit(vararg logWriters: LogWriter, minSeverity: Severity = DEFAULT_MIN_SEVERITY): co.touchlab.kermit.LoggerConfig =
    StaticConfig(logWriterList = logWriters.toList(), minSeverity = minSeverity)

/**
 * Immutable LoggerConfig. Access is thread safe because state is immutable. Use this is you are worried about logging
 * performance.
 */
data class StaticConfig(
    override val minSeverity: Severity = DEFAULT_MIN_SEVERITY,
    override val logWriterList: List<LogWriter> = listOf(CommonWriter()),
) : co.touchlab.kermit.LoggerConfig

internal val DEFAULT_MIN_SEVERITY = Severity.Warn
