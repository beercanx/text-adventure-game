package uk.co.baconi.games.tag.engine

// Guidance from:
//  https://www.microheaven.com/ifguide/step3.html
//  https://docs.textadventures.co.uk/quest/tutorial/interacting_with_objects.html
//
enum class Verb {
    // Common
    Look,
    // TODO - Take,
    // TODO - Drop,
    // TODO - Examine,
    // TODO - Search,
    // TODO - Inventory,
    Open,
    // TODO - Close,
    // TODO - Lock,
    // TODO - Unlock,
    // TODO - Wait,
    // TODO - Again,
    // TODO - Ask,
    // TODO - Tell,
    // TODO - Say,
    // TODO - Give,
    // TODO - Show,

    // Special
    Start, // Starts a new game.
    Quit, // Ends the current game.
    Help, // Shows some information about the game and its author, in some cases even hints to some of the puzzles.
    // TODO - Undo, // Takes back the last move you made.
    // TODO - Restart, // Starts the game over from the beginning.
    // TODO - Save, // Saves your current position to a file on disk.
    // TODO - Restore, // Loads a previously saved game position.
    // TODO - Verbose, // Tells the game you want a long description of every room you enter, even if you've been there before.
    // TODO - Brief, // Tells the game you want a long description the first time you enter a room, and a short description when you come back. This is the default mode.
    // TODO - SuperBrief, // Tells the game you always want short descriptions of all rooms.

    // Other or Future ones
    // TODO - Attack,
    // TODO - Buy,
    // TODO - Sell,
    // TODO - Cover,
    // TODO - Drink,
    // TODO - Eat,
    // TODO - Fill,
    // TODO - Jump,
    // TODO - Kiss,
    // TODO - Knock,
    // TODO - Listen,
    // TODO - Move,
    // TODO - Pull,
    // TODO - Remove,
    // TODO - Read,
    // TODO - Sit,
    // TODO - Sleep,
    // TODO - Stand,
    // TODO - Throw,
    // TODO - Tie,
    // TODO - Touch,
    // TODO - Turn,
    // TODO - Type,
    // TODO - Untie,
    // TODO - Wear,
}