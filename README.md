# README
## CS 351 - 002
### Project 3: Scrabble
#### John Tran

### Introduction

All `List` objects were implemented as an `ArrayList`.

### Instructions

The `.jar` files are configured with the debug messages ON. I might have left some vital checking inside the debug parts, so I didn't want to break the program by turning them off (and lack of time to comb through and edit the debug messages).

The `.jar` files are found at the root and can be run with the usual
```
java -jar JAR_NAME
```
where `JAR_NAME` is the name of the jar file. The `.jar` for the console is `dominosConsole.jar` and the one for the GUI is `dominosGUI.jar`.

### Assumptions

For the GUI version, I decided that the human player would always go first (so the word solver doesn't go checking the entire board...). Also, I assumed the tile bag (for any file to initialize the tiles in the bag the players draw from) only contained letters in the alphabet ('a' to 'z').

### doc Note

Given time constraints, there wasn't enough time to produce a document detailing the objects mentioned in the design. However, I hope the information provided here and in the code itself should make the usage and implementation of the objects apparent.

### Rushed Comments in the Code

Again, given the time constraints, many of the comments were a little rushed and only the public methods were commented. Further, if there were some methods that were overridden, only the topmost super class will have a comment (generally speaking, but the only difference should be implementation and the design document should help clarify the differences between the two versions).

Also, any get and set methods are generally inferable, so they won't be commented (including simple methods, like drawing a domino). The exceptions are also an exception (no pun intended) since the exception itself can be inferred from the class name.

### Known Bugs

In the `class WordSovler`, I didn't have enough time to account for the possibility that any given board square can be represented as different anchor squares from DIFFERENT reference word (i.e. more than one word shares the same anchor square, though not necessarily the same type). So, there are words that the solver probably doesn't account for...

### Afternotes

- The
```java
boolean activeMultiplier
```
member variable in the `class BoardSquare` wasn't needed in the end since I hardcoded all the multipliers for the different types of board squares on the board (in the `enum BoardSquareType`)

- In the `checkWordAlongDirection()` method in the `class WordSovler`, I couldn't find a better way to create a "dummy" reference for the next `BoardSquare`. So, I just ended up created a "dummy" `List` that held the reference to some `BoardSquare` and was cleared each time before I needed to hold another `BoardSquare` reference.   
Edit: I faced a similar issue in other methods as well (for example,  `firstPartCrossCheck()` and `secondPartCrossCheck()` methods in the `class WordSovler` as well)

- In the `class BoardSquare`, the input of characters from the file/console is a little wonky since I used the `next()` method of the `class Scanner` to get the next "square" in the row of the input &mdash; this means that only one character was read for that tile (since the character before the letter, if the square contained a letter, is just a space and ignored by `next()`). So, the member variable `char twoChar` to hold the raw input had to be modified specifically for a letter square (had to put a space before the letter so the board could be printed out correctly).

- When dealing with different input formats, I found that I needed to only use one `Scanner` object for the entire main (for any given main). So, there was a little inconsistency with how the `try with resources` was used inside the classes that needed input from files (i.e. `class Board`) and a `Scanner` object was used as a constructor parameter for those same classes if the input was read from the console.

- There were some inconsistencies with the naming of methods, specifically pertaining to the split of the "left" and "right" parts mentioned in the Appel and Jacobson paper.

### Resources

The World’s Fastest Scrabble Program by Andrew W. Appel AND Guy J. Jacobson
(the pdf of the paper will be included in the `resources` directory in the root directory named `scrabbleAppelJacobson.pdf`)
