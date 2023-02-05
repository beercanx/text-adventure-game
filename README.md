# Text Adventure Game
An attempt at a text adventure game, using Discord as the GUI.

## Project requirements
* JDK 17 (Gradle toolchain is setup to provide one if it cannot be found)
* A discord application to integrate against

## Project layout

### Common
In the common module you'll find the basic common stuff like logging configuration.

### Datastore
In the datastore module you'll find all the logic about persisting data.

### Engine
In the engine module you'll find all the game engine, which is decoupled from the game UI as much as possible.

### Bot
In the bot module you'll find all the integrations with Discord from setting up the game to handling user interactions. 

## Project configuring
For the most part the only extra configuration required will be the secrets that manage the Discord integration.

The application is build upon the 12 factor premise that external configuration is handled via environment variables.

These can be managed within say IntelliJ IDEA by using the EnvFile plugin and a `.env` file in the root of the project.

### Required (to start application)
```
# The application id for the Discord application you want to run this under.
APPLICATION_ID=123

# The bot token for the Discord application you want to run this under.
TOKEN=abc-123
```

### Optional
```
# Used to toggle the application logging, enable trace to see all events recieved. 
# Default: DEBUG
LOGGING_LEVEL=TRACE
```

### Discord permissions
To use this integration with a Discord application (integration), it will need the following permissions.

#### Privileged Gateway Intents:
* Message Content Intent

#### General Permissions:
* Read Messages / View Channels

#### Text Permissions:
* Send Messages

## Running the project
For now to run the project, you'll be starting the Discord bot.

Of course, you'll still have to sort out your own environment variables, especially if you run from the command line as the .env file isn't picked up by default.

### Direct method
Run the main function in the [MainKt](bot/src/main/kotlin/uk/co/baconi/games/tag/bot/Main.kt) class within the bot module. 

### Gradle method
Run the Gradle application, which is defined in the bot module.
```bash
./gradlew run
```