package uk.co.baconi.games.tag.bot.guild

class HelpService(private val commands: MutableMap<String, String> = mutableMapOf()) {

    private var _helpMessage = ""

    val helpMessage: String
        get() = _helpMessage

    @Synchronized
    fun registerCommand(name: String, description: String) {
        commands[name] = description
        recalculateHelpMessage()
    }

    @Synchronized
    fun unregisterCommand(name: String) {
        commands.remove(name)
        recalculateHelpMessage()
    }

    private fun recalculateHelpMessage() {

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