package uk.co.baconi.games.tag.bot.guild

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

class HelpService(private val commands: MutableMap<String, String> = ConcurrentHashMap()) {

    private val mutex = Mutex()

    private var _helpMessage = ""

    val helpMessage: String
        get() = _helpMessage

    suspend fun registerCommand(name: String, description: String) {
        commands[name] = description
        recalculateHelpMessage()
    }

    suspend fun unregisterCommand(name: String) {
        commands.remove(name)
        recalculateHelpMessage()
    }

    private suspend fun recalculateHelpMessage() = coroutineScope {
        launch {// Run async
            mutex.withLock { // But only one at a time

                val max = commands.keys.fold(0) { last, next ->
                    when {
                        last >= next.length -> last
                        else -> next.length
                    }
                }

                _helpMessage = commands.entries.joinToString(separator = "\n") { (name, description) ->
                    "` /${name.padEnd(max)} ` $description"
                }
            }
        }
    }
}