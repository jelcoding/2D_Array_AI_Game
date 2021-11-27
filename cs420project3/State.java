package cs420project3;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author James
 */
public class State {
 private String[][]board;
 private int val;
 private State parent;
 private List<State> successors = new ArrayList<State>();
 private int alpha;
 private int beta;
 
 public State(String[][]board, State parent)
 {
     this.board = board;
     this.parent = parent;
     alpha = -999;
     beta = 999;
     val = Val("X") - Val("O");
 }
 
 public List<State> getSuccessors()
 {
     return successors;
 }
 public void addSuccessor(State s)
 {
     successors.add(s);
 }
 public void setAlpha(int a)
 {
     alpha = a;
 }
 public void setBeta(int a)
 {
     beta = a;
 }
  public int getAlpha()
 {
     return alpha;
 }
 public int getBeta()
 {
     return beta;
 }
 public void setVal(int a)
 {
     val = a;
 }
 public int getVal()
 {
     return val;
 }
  public State getParent()
 {
     return parent;
 }
 public String[][]getBoard()
 {
     return board;
 }
 public void changeBoard(int row, int col, String player)
 {
     board[row][col] = player;
 }
 public int Val(String player)
 {
     int v = 0;
     int playerPosRow = 0;
     int playerPosCol = 0;
     
     for(int i = 0; i < board.length; i++)
     {
         for(int j = 0; j < board.length; j++)
         {
             if(board[i][j].equals(player))
             {
                 playerPosRow = i;
                 playerPosCol = j;
                 break;
             }
         }
     }
     
     //possible moves up
     int currRow = playerPosRow - 1;
     int currCol = playerPosCol;
     while(currRow >= 0 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow--;
     }
     
     
     
     //possible moves down
     currRow = playerPosRow + 1;
     while(currRow <= 7 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow++;
     }
     
     
     
     //possible moves left
     currRow = playerPosRow;
     currCol = playerPosCol - 1;
     while(currCol >= 0 && board[currRow][currCol].equals("-"))
     {
         v++;
         currCol--;
     }
     
     
     //possible moves right
     currRow = playerPosRow;
     currCol = playerPosCol + 1;
     while(currCol <= 7 && board[currRow][currCol].equals("-"))
     {
         v++;
         currCol++;
     }
     
     
     //possible moves up-left
     currRow = playerPosRow - 1;
     currCol = playerPosCol - 1;
     while(currRow >= 0 && currCol >= 0 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow--;
         currCol--;
     }
     
     
     //possible moves up-right
     currRow = playerPosRow - 1;
     currCol = playerPosCol + 1;
     while(currRow >= 0 && currCol <= 7 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow--;
         currCol++;
     }
     
     
     //possible moves down-right
     currRow = playerPosRow + 1;
     currCol = playerPosCol + 1;
     while(currRow <= 7 && currCol <= 7 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow++;
         currCol++;
     }
     
     
     //possible moves down-left
     currRow = playerPosRow + 1;
     currCol = playerPosCol - 1;
     while(currRow <= 7 && currCol >= 0 && board[currRow][currCol].equals("-"))
     {
         v++;
         currRow++;
         currCol--;
     }
     
     
    
     
     return v;
 }
 
}
