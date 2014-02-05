# Smack Down

A custom card game based on [Smash-Up by AEG](http://www.alderac.com/smashup/).

Written in Scala as a way of learning the language, this part of the project was intended to simply model the game logic (which became increasingly complicated as more and more cards with different abilities were added).

Abandoned for a while, but there are some neat things in there, like the Choice[T] and Decision[T] (see Core.scala) types, combined with the use of implicits to provide what are referred to as "Extension Methods" in C#, to create a very elegant syntax for combining user interactive prompts and game logic (best example might be in Pirates.scala).