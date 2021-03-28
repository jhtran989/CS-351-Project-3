# README
## CS 351 - 002
### Project 3: Scrabble
#### John Tran

### Introduction



### Instructions

The `.jar` files are configured with the debug messages ON. I might have left some vital checking inside the debug parts, so I didn't want to break the program by turning them off (and lack of time to comb through and edit the debug messages).

The `.jar` files are found at the root and can be run with the usual
```
java -jar JAR_NAME
```
where `JAR_NAME` is the name of the jar file. The `.jar` for the console is `dominosConsole.jar` and the one for the GUI is `dominosGUI.jar`.

For the input into the word solver, I decided place it in an infinite (while) loop and it will hopefully remove any new line characters in between inputs (`\n`). However, keeping to the format of the input, it assumes all the info for a given input (dimension of board, board itself, and rack) will be grouped together with no blank lines in between the info.

### Design Overview



### Critical Design choices

All `List` objects were implemented as an `ArrayList`.

(x2 of stuff)

The `class Tile` will contain the `public static final` variable for the character representation of the blank tile in the file to load the tiles used in the game (in this case, `scrabble_tiles.txt` is just used and set in the constructor of the `class TileBag`). Assuming the character for a blank character is an asterisk '*', which
will also be referenced in the `WordSolver class` to execute the special
check for a blank tile (specifically, the `leftPart()` and `extendRight()` methods).
Edit: a `static` method `isBlankTile()` will be used instead since there's no way to know beforehand what character will be used to represent the blank tile (hence, why there's a `static` field that can be changed as needed).

Furthermore, the `Tile` objects in the `TileBag` and `Rack` classes will be stored in a map for easy retrieval and convenience. For `TileBag`, a sort of frequency map is used where the `Tile` object represents the key and the frequency of those tiles (with the same letter) represents the value (decremented each time one of those tiles is removed and drawn by a player). However, `Rack` uses the actual character representation of the `Tile` object as its value to allow for easy searching of a tile given a character.

Below are some instance variables in the `class WordSolver` where the prefix `current-` was used in the variable name that holds key information during the move generation...

```java
private WordInPlay currentLeftPart;
```
There were problems with object references and recursion in the `leftPart()` and `extendRight()` methods in the `class WordSovler`, so a new constructor for `WordInPlay` had to be created and a copy of the left part during each call of `leftPart()` in the stack had to be created using the constructor mentioned above.

```java
private AnchorType currentAnchorType;
```
Since I decided to split up the kinds of anchors, I needed a way to access the current anchor type (i.e. in the `extendRight()` method in the `class WordSovler`, I had to make sure that current words in play (or even parts of those words) aren't added to the legal words)


```java
private BoardSquare currentLeftBoardSquare;
```
I decided to include a `List` of `BoardSquare` objects when finding the possible words since that was the only way (with the way I designed it) for me to find any additional words that were formed (i.e. the two or so letter words formed when the possible word is placed adjacent along the same play direction of a word already in play)

### Assumptions

This would have been an easy fix, but I assumed any dictionary passed into the word trie will only contain lowercase words. Also, I assumed the tiles file used for the game has lowercase letters as well (otherwise, those letters wouldn't be added to the bag of tiles).

For the GUI version, I decided that the human player would always go first (so the word solver doesn't go checking the entire board...). Also, I assumed the tile bag (for any file to initialize the tiles in the bag the players draw from) only contained letters in the alphabet ('a' to 'z').

### doc Note

Given time constraints, there wasn't enough time to produce a document detailing the objects mentioned in the design. However, I hope the information provided here and in the code itself should make the usage and implementation of the objects apparent.

### Rushed Comments in the Code

Again, given the time constraints, many of the comments were a little rushed and only the public methods were commented. Further, if there were some methods that were overridden, only the topmost super class will have a comment (generally speaking, but the only difference should be implementation and the design document should help clarify the differences between the two versions).

Also, any get and set methods are generally inferable, so they won't be commented (including simple methods, like drawing a domino). The exceptions are also an exception (no pun intended) since the exception itself can be inferred from the class name.

### Known Bugs

In the `class WordSovler`, I didn't have enough time to account for the possibility that any given board square can be represented as different anchor squares from DIFFERENT reference word (i.e. more than one word shares the same anchor square, though not necessarily the same type). So, there are words that the solver probably doesn't account for...

### Afternotes

- Any static methods will generally be at the top of the class, but they are few and far in between (mostly in the `utilities` package mentioned below).

- There were a few types of `public static final boolean` "DEBUG" variables in various "Main" classes that contain the `main()` method used for debugging purposes (as opposed to just one or so in the previous project) and this time, I made sure that the code should be fully functional even without the debug "flags" turned on.

- As was the case in the previous project, there is a `utilities` package that housed classes with all `static` methods for utility purposes (like parsing input).

- The
```java
boolean activeMultiplier
```
member variable in the `class BoardSquare` wasn't needed in the end since I hardcoded all the multipliers for the different types of board squares on the board (in the `enum BoardSquareType`)

- In the `checkWordAlongDirection()` method in the `class WordSovler`, I couldn't find a better way to create a "dummy" reference for the next `BoardSquare`. So, I just ended up created a "dummy" `List` that held the reference to some `BoardSquare` and was cleared each time before I needed to hold another `BoardSquare` reference (pointers would probably make it a bit simpler...).   
Edit: I faced a similar issue in other methods as well (for example,  `firstPartCrossCheck()` and `secondPartCrossCheck()` methods in the `class WordSovler` as well)

- In the `class BoardSquare`, the input of characters from the file/console is a little wonky since I used the `next()` method of the `class Scanner` to get the next "square" in the row of the input &mdash; this means that only one character was read for that tile (since the character before the letter, if the square contained a letter, is just a space and ignored by `next()`). So, the member variable `char twoChar` to hold the raw input had to be modified specifically for a letter square (had to put a space before the letter so the board could be printed out correctly).

- When dealing with different input formats, I found that I needed to only use one `Scanner` object for the entire main (for any given main). So, there was a little inconsistency with how the `try with resources` was used inside the classes that needed input from files (i.e. `class Board`) and a `Scanner` object was used as a constructor parameter for those same classes if the input was read from the console.

- There were some inconsistencies with the naming of methods, specifically pertaining to the split of the "left" and "right" parts mentioned in the Appel and Jacobson paper. For instance, sometimes I'd use the prefix `primar-` and `secondary-` together, or `first-` and `second-`, or even `left-` and `right-` in various parts of the code. Generally, though, though should mean the same thing as I was going through different stages and decided to generalize the meaning (regardless if the orientation of the word was "horizontal" or "vertical" on the board). In addition, there were inconsistencies with how instance variables were accessed in the classes that housed them. Specifically, whether the instance variable was accessed directly or via the corresponding `get` method (shouldn't really matter, though).

- When initializing the cross check `Set` for the `OUTSIDE` type anchors and various data structures (`List`, `Set`, `Map`, etc.) to store information about the tiles, children nodes of the trie, etc., I decided to rely on Java's autoboxing of `char` into `Character` objects. This is because of the essentially static object that is created from a primitive type and this allowed me to create a static set of letters to use &mdash; really useful for using them as the `key` for a `Map` and allows for really quick searches.

- This next point follows from the previous, but when creating the cross check sets, I wanted to remove any `Character` objects from the set as I iterated through it (checking to see if there are nearby `Letter` board squares that can form words with the current anchor square being checked). So, the easiest option was to use an `Iterator` object of the set that avoided any concurrent exceptions when iterating through the set.

### Resources

The World’s Fastest Scrabble Program by Andrew W. Appel AND Guy J. Jacobson
(the pdf of the paper will be included in the `resources` directory in the root directory named `scrabbleAppelJacobson.pdf`)
