package uk.co.baconi.games.tag.engine

import uk.co.baconi.games.tag.engine.Room.*

// TODO - Consider moving into configuration, with Kotlinx serialisation support.
val exampleLayout = Layout
    .Builder()
    .add(FrontPath, Porch, Drive)
    .add(Drive, FrontPath, Garage)
    .add(Garage, Drive, Yard)
    .add(Porch, Hallway)
    .add(Hallway, Porch, Dining, Kitchen, UnderTheStairs, Lounge, StairCase)
    .add(Dining, Hallway)
    .add(Lounge, Hallway, Conservatory, ChimneyStack)
    .add(ChimneyStack, Lounge, Roof)

    .add(StairCase, Hallway, Landing)
    .add(Landing, StairCase, FirstBedroom, SecondBedroom, Toilet, Bathroom, ThirdBedroom, FourthBedroom)
    .add(FirstBedroom, Landing)
    .add(SecondBedroom, Landing)
    .add(Toilet, Landing)
    .add(ThirdBedroom, Landing)
    .add(FourthBedroom, Landing)
    .add(Bathroom, Landing, Attic)
    .add(Attic, Bathroom, Roof)
    .add(Roof, Attic, ChimneyStack)
    .add(Kitchen, Pantry, BackPassage)

    .add(BackPassage, Kitchen, Utility)
    .add(Utility, BackPassage, Yard)

    .add(Yard, Garage, Utility, Garden, ToolShed, CoalShed)
    .add(ToolShed, Yard)
    .add(CoalShed, Yard, Garden)
    .add(Conservatory, Lounge, Garden)
    .add(Garden, Conservatory, Yard, CoalShed, SummerHouse, TreeHouse, GreenHouse, VeggiePatch, Field)
    .add(SummerHouse, Garden)
    .add(TreeHouse, Garden)
    .add(GreenHouse, Garden)
    .add(VeggiePatch, Garden, Field)

    .add(Field, VeggiePatch, Garden)

    .build()
