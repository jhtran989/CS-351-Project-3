# README
## CS 351 - 002
### Project 3: Scrabble
#### John Tran

### Introduction

All `List` objects were implemented as an `ArrayList`.

### Known Bugs

In the `class WordSovler`, I didn't have enough time to account for the possibility that any given board square can be represented as different anchor squares from DIFFERENT reference word (i.e. more than one word shares the same anchor square, though not necessarily the same type). So, there are words that the solver probably doesn't account for...

### Afternotes

- The
```java
boolean activeMultiplier
```
wasn't needed in the end since I hardcoded all the multipliers for the different types of board squares on the board (in the `enum BoardSquareType`)

- In the `checkWordAlongDirection()` method in the `class WordSovler`, I couldn't find a better way to create a "dummy" reference for the next `BoardSquare`. So, I just ended up created a "dummy" `List` that held the reference to some `BoardSquare` and was cleared each time before I needed to hold another `BoardSquare` reference.   
Edit: I faced a similar issue in other methods as well (for example,  `firstPartCrossCheck()` and `secondPartCrossCheck()` methods in the `class WordSovler` as well)
