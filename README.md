# README
## CS 351 - 002
### Project 3: Scrabble
#### John Tran

### Introduction

UPDATE: The due date was extended to Sunday, 4/4, and the jar file was just updated just recently so the resources was packaged properly. The GUI version development will now resume (disregard the comments made below about it being scrapped)...

Throughout the `README`, I'll reference the paper by Appel and Jacobson (more information provided in the "Resources" section at the end). For brevity, though, I'll just refer to it as the "paper" and will refer to some of the big ideas presented in it.

For this project, I suspect that two late extension days (in addition to the extension to the original due date) will be needed.

### Instructions

Fortunately, I was able to make it so that the word solver could exact any number of valid inputs. However, it's important that there are NO next lines before or after each input (copying all four test cases into the word solver should work, at least, at the time of this writing). Also, the word solver will pretty much be running on an infinite loop, so the program would have to be manually closed (force quit or enter end of file character). It might throw an exception, but the program should work otherwise (forced the scanner to look for a next line with each new input...). For best performance (so that the output isn't mixed with the input), maybe put in one input case at a time (with the program still running in between inputs, making sure to follow the guidline mentioned above).

There is one thing to note about the fourth test case. My solution provided a word with the same number of points, but with a slightly different word (same orientation on the board, but looking at the output solution yourself should clear things up) -- "Podgeist" instead of "Bodgeist" in the example output (when you let `PRINT_LEGAL_SQUARES` be true, you see that there are a few letters that has the "-odgeist" ending, especially since the first letter is a blank, which could satisfy all those words with that ending).

Also, given time constraints and how much time it took to debug the solver (quite a long time...), the GUI will probably the barebones with no checking from the user for legal moves, etc.

The `.jar` files are configured with the debug messages ON. I might have left some vital checking inside the debug parts, so I didn't want to break the program by turning them off (and lack of time to comb through and edit the debug messages).

The `.jar` files are found at the root and can be run with the usual
```
java -jar JAR_NAME
```
where `JAR_NAME` is the name of the jar file. The `.jar` file for the word solver is `wordSolver.jar` and the one for the Scrabble GUI is `scrabbleGUI.jar`.

EDIT: Due to time constraints, I wasn't able to start the GUI version, but I hope the word solver should work as intended.

For the input into the word solver, I decided place it in an infinite (while) loop and it will hopefully remove any new line characters in between inputs (`\n`). However, keeping to the format of the input, it assumes all the info for a given input (dimension of board, board itself, and rack) will be grouped together with no blank lines in between the info.

### Design Overview

A few sketches will be provided alongside the diagrams (same directory: `/doc` at the root of the project) to showcase some of the brainstorming I came up with while coding up the project. It is especially enlightening in terms of how the cross check sets were handled for the applicable anchor squares and how I broke up the types of anchor squares and how to actually find them on a given board. Below, I'll give a summary of my overall design and any brief notes about them.

There were some files and methods scattered throughout that were never used, but were planned to for the GUI version (scrapped due to time constraints, as mentioned above). In the end, I left there as "placeholders" to when I might come back and implement it for fun...

As will be mentioned below, the comment was really rushed this time and only worthwhile methods will be commented (if it can be inferred from the name or is relatively short, then it probably won't be commented due to time constraints).

In the `WordSolver` class, there were a lot of methods that could have been encapsulated to its separate classes, but due to lack of time, was left as is (with placeholders, as mentioned before).

QUICK NOTE: the `leftPart()` updates the NEXT board square (proactively) and the `extendRight()` updates the CURRENT board square (reactively)

#### Data Structure for Word Searching

In the paper, it described of a DAWG (or even a GADDAG) that helped condense the structure (or in the case of the GADDAG, really amplified the structure to allow bidirectional checking). However, as stated numerous times in the lecture, that might be a little overkill (and kind of a pain to keep track of) for this, so I decided to go with a middle solution in the form of a trie. All the relevant classes should be located in the `wordSearch` package.

#### Anchor Types

The anchors mentioned in the paper involved the letter tiles that comprise of a word in play on the board and any adjacent squares to the word (if the square is on the board). However, due to the nature of playing words from top to bottom or left to right, the topmost or leftmost area is a crucial spot for the anchor squares. The sketch will probably illustrate this better, but to avoid redundant searching (and problems occurring in the "left part" of a possible word &mdash; discussed below), a special cases arises with the first square in the given row or column. As such, to generalize the direction of the word and which anchor square I'm referring to in reference to it, I'll use the convention of using the start of the word as the "first" or "left" part (since we're more familiar with words laid out horizontally in our daily reading) and "second" or "right" part to mean the "end" of the word.

There were some inconsistencies with the naming of methods, specifically pertaining to the split of the "left" and "right" parts mentioned in the paper. For instance, sometimes I'd use the prefix `primar-` and `secondary-` together, or `first-` and `second-`, or even `left-` and `right-` in various parts of the code. Generally, though, though should mean the same thing as I was going through different stages and decided to generalize the meaning (regardless if the orientation of the word was "horizontal" or "vertical" on the board). In addition, there were inconsistencies with how instance variables were accessed in the classes that housed them. Specifically, whether the instance variable was accessed directly or via the corresponding `get` method (shouldn't really matter, though).

Now, for the anchor squares themselves, I split them up into `INSIDE` and `OUTSIDE` anchor squares &mdash; `INSIDE` comprising the squares that make up a word, and `OUTSIDE` comprising all the adjacent squares mentioned above.

Furthermore, the anchor squares were broken down further by analyzing what we are necessarily checking with each anchor square for a given word. Using the idea of a "left part" from the paper, an anchor square can extend "left" or in the reverse play direction of a word if there are other anchor squares to the "left" of it (there would be redundant searching since those words would already checked by the anchor squares to the "left" of it). In addition, there are two play directions to consider for every anchor square. However, using a similar approach as before, we can break the anchor squares to which play direction (with respect to the play direction of a given word) the possible words are constructed. So, I loosely refer to them as `PRIMARY` anchors (along the SAME direction as the word) and `SECONDARY` anchors (along the OPPOSITE direction as the word) with subcategories within them, emphasizing the "left part". For the `PRIMARY` anchors, I thought of the `PRIMARY_HEAD` and `PRIMARY_BODY` with the "head" part given if the anchor square is able to have a "left part" without redundant checking. I decided to break up the `PRIMARY_HEAD` into `PRIMARY_CENTER_HEAD` and `PRIMARY_SIDE_HEAD` (not really necessary, but helps differentiate between the anchor square that part of the word or adjacent to the word). Then the `SECONDARY` anchors are split up into `SECONDARY_END` and `SECONDARY_BODY` with a similar justification as above (where the "end" is added as the adjacent anchor squares and represents the ends of the word).

When searching for anchor squares, the `INSIDE` anchor squares are prioritized in that if an anchor square overlaps across different reference word, then the `INSIDE` anchor square is added over the `OUTSIDE` one since the `INSIDE` anchor squares always have a possible left part to check (whereas the `OUTSIDE` only has a left part on the `PRIMARY_HEAD`).

When finding the left limit of an applicable anchor square (not a `PRIMARY_SIDE_BODY`), the paper describes to check up to an anchor square. However, I find that having to keep check of anchor squares of a given word in play is quite tedious, so I found a workaround. Since we know that `OUTSIDE` anchor squares are always on square away from all directions of a given word, I just check if a square is a letter and subtract 1 from the current left limit.

#### Cross Checks

The cross check sets to allow for "trivial" checks when checking anchor squares can drastically reduce the number of checks needed to check a possible word place in the SAME play direction as the word (if placed along the `BODY`, as described above), or the OPPOSITE play direction (if placed on the `END`).

#### IMPORTANT:

All four sample test cases given should work, but there may still be some bugs that break the program for different setups of the board.

### Critical Design choices

All `List` objects were implemented as an `ArrayList`.

Also, when initializing the board, I assumed that we remove tiles already on the board (in the setup) from the `TileBag` object, which instantiates a new object to "refresh" the tiles in the bag so errors doesn't occur with the initialization of the board.

IMPORTANT: If the board configuration is invalid (with the given file to create the `TileBag` object) &mdash; like not having enough tiles in the bag to put them on the board &mdash; then it will most likely break the program.

In the final implementation of the `WordSolver`, there seems to be duplicates that are added as legal words &mdash; seems to only occur when right extend reaches the edge of the board (those words are checked multiple times when going beyond and backtracking back within the bounds of the board). To account for this, a temporary solution was to override both the `equals()` and `hashCode()` methods of the `WordInPlay` class so duplicate words wouldn't be added.

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

### Design Reflection

In truth, the code is quite messy, especially with all the debug statements. There were a lot of times were I'd spend a decent chunk of time just finding a specific part of code, which would been easier if the organization was made with due diligence at the beginning.

There was a lot of dead code when I was debugging, which should all be removed at the time of submission, but still not as nicely organized or even encapsulated as I would have like. For example, the `leftPart()` and `extendRight()` methods in the `WordSolver` class was pretty large in their own right and I even had to break it up with helper methods &mdash; still pretty big after the change.

As a result, the `WordSolver` class was definitely a hefty class with many methods that extended to hundreds of lines of code. Due to time constraints, however, I wasn't able to encapsulate it in the end (like moving the method to find the words in play in one class and moving the one finding all the anchor squares with the cross checks, left limits, etc. in another). So, it may be a little difficult to comb through the code...

### GUI Version

IMPORTANT: The code does not check to see if the use plays a valid word (very little restrictions because of time...). Also, I don't check if the user plays a single letter word...

For the maintenance of the GUI elements, the following website really helped in streamlining the process: https://hendrix-cs.github.io/csci151/. Besides the suggestions mentioned on the website, I downloaded Scene Builder from the Gluon webpage (https://gluonhq.com/products/scene-builder/) that really came in handy in optimizing the pane options with a GUI to create another GUI rather than having to code everything, like in the Dominos Project (integration with the `.fxml` file).

There were some nifty features added (like being able to drag and snap the tile images to the board squares) &mdash; ONLY works with the DEFAULT window size I configured...

EDIT: Actually, resizing the window now shouldn't affect the positioning of the tiles on the board (keeping everything left aligned helped so that the positioning relative to the scene didn't change).

There were some pieces of code that were altered slightly to account for the human player's turn. So, I decided to split up the procedure of the word solver by moving the initial preliminary search with the human player's turn so easy calculations could be done with their turn and leaving the actual word generation to the computer player's turn.

Internally, the tile images are stored in an `HBox` object, but considering that the images have to stay in the `HBox` object after they have been "played" on the board, the size of images contained in the `HBox` is not quite the same as the actual number of tiles in the human player's rack...(furthermore, the computer player's played tiles will be added to the same `HBox` &mdash; only way for me to get them to show without having to create another container).

The right side of the window will have all the necessary information (score info and words and play button).

EDIT: I realized that I forgot to "clear" and "reset" everything in between turns of the human and computer player (there still might be errors, though).

Edit 2: Now the tiles pieces from the human player have started to move...(need a better way than just putting them in a `HBox` or `VBox`...)

Edit 3: There were a few situations where the computer player wasn't able to play a valid word...(an exception was thrown)

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

EDIT: That should be fixed now, but there seems to be duplicate words added (only when the end of the board is reached, as mentioned above).

EDIT: With the extension, the GUI version was started and for now, BLANK tiles don't work as expected (only for the human player since there would have to be some option for each blank tile so the user can select which letter it would represent) and can't be fixed due to time constraints.

The computer player can somtimes play the same word twice if both times, the word is the highest scoring move &mdash; there are good number of issues when transferring to and from the GUI version with the internal representation. A prominent issue is the computer player's words shift with each turn (working with a `VBox` instead of an `HBox` might the issue when comparing with the human player...). Also, exception errors might occur after the first turn (again, I didn't occur these issues in the plain word solver).

There are also times when the play button freezes (occasional)...

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

- When initializing the cross check `Set` for the `OUTSIDE` type anchors and various data structures (`List`, `Set`, `Map`, etc.) to store information about the tiles, children nodes of the trie, etc., I decided to rely on Java's autoboxing of `char` into `Character` objects. This is because of the essentially static object that is created from a primitive type and this allowed me to create a static set of letters to use &mdash; really useful for using them as the `key` for a `Map` and allows for really quick searches.

- This next point follows from the previous, but when creating the cross check sets, I wanted to remove any `Character` objects from the set as I iterated through it (checking to see if there are nearby `Letter` board squares that can form words with the current anchor square being checked). So, the easiest option was to use an `Iterator` object of the set that avoided any concurrent exceptions when iterating through the set.

### Resources

The World’s Fastest Scrabble Program by Andrew W. Appel AND Guy J. Jacobson
(the pdf of the paper will be included in the `resources` directory in the root directory named `scrabbleAppelJacobson.pdf`)

GUI inspiration at https://hendrix-cs.github.io/csci151/
