package uk.co.baconi.games.tag.common

import ch.qos.logback.classic.Level.*
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants.*
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

/**
 * Provides customised version of https://logback.qos.ch/manual/layouts.html#coloring
 */
class LogbackHighlighter : ForegroundCompositeConverterBase<ILoggingEvent>() {
    override fun getForegroundColorCode(event: ILoggingEvent): String = when(event.level) {
        ERROR -> BOLD + RED_FG
        WARN -> YELLOW_FG
        INFO -> BLUE_FG
        DEBUG -> MAGENTA_FG
        else -> DEFAULT_FG
    }
}