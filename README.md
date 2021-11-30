# 2D_Array_AI_Game
Array-based game against an artificial intelligence.

Project was built to have a rudimentary artificial intelligence find the best moves possible in an array-based tile game where the object was to 
cause the opponent to run out of moves. AI was then put up against other AI's by typing in the moves determined by the other AI's manually. Should 
also be able to play against a human player. 


Game features two players. User is able to choose whether they or the computer go first

Select abritrary number for time limit

Starting player will start in top left of board. Secondary player will start on bottom right of board

X = computer
0 = User
# = Used space/Wall

Players can move in the same movement pattern that a Queen in chess can move (any straight line/diagonal)

Every time a player leaves a space that they were on, it will form a "wall" and that space cannot be moved to again

Players cannot move through walls, or spaces that have already been landed on

Objective is to leave other player with no remaining moves

Output running on console can be seen in accompanying png

Main serves as the driver class

Game operates the mechanics

State oversees the board, as well as supplying possible board states for the AI to make decisions
