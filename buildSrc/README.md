# Just what is this buildSrc thing?

I'm basically using it to share configuration between subprojects via the Gradle convention plugin mechanism.

* [text-adventure-game.common](src/main/kotlin/text-adventure-game.common.gradle.kts) convention plugin to manage the
  common configuration and dependencies, like how one would do in Maven using inheritance from parent modules.