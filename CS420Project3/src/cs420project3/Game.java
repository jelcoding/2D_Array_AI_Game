package cs420project3;


import java.util.Random;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author James
 */
public class Game
{

    private int currDepth;
    private char[] legend;
    private State[] tiebreaker;
    private int timeLimit;

    public void SetBoard()
    {
        legend = new char[8];
        char q = 'A';
        for (int i = 0; i < legend.length; i++)
        {
            legend[i] = q;
            q++;
        }
        String[][] board = new String[8][8];
        currDepth = 0;
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                board[i][j] = "-";
            }
        }

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Time limit: ");
        timeLimit = keyboard.nextInt();

        System.out.println("Time limit established as " + timeLimit + " seconds");
        System.out.println("Who goes first?");
        System.out.println("1. Computer");
        System.out.println("2. User");

        int ans = keyboard.nextInt();
        while (ans < 1 || ans > 2)
        {
            System.out.println("Invalid");
            System.out.println("Who goes first?");
            System.out.println("1. Computer");
            System.out.println("2. User");
            ans = keyboard.nextInt();
        }

        System.out.println();

        if (ans == 1)
        {
            board[0][0] = "X";
            board[7][7] = "O";
            Display(board);
            System.out.println();

            while (true)
            {
                //System.out.println("Computer move");
                Copy(board, Iteration(board));
                if (GameDone(board, "O"))
                {
                    System.exit(0);
                }
                PlayLoop(board);
                if (GameDone(board, "X"))
                {
                    System.exit(0);
                }
            }
        }
        else if (ans == 2)
        {
            board[0][0] = "O";
            board[7][7] = "X";
            Display(board);

            //System.out.println();
            while (true)
            {
                PlayLoop(board);
                if (GameDone(board, "X"))
                {
                    System.exit(0);
                }
                //System.out.println("Computer move");
                Copy(board, Iteration(board));
                if (GameDone(board, "O"))
                {
                    System.exit(0);
                }

            }
        }

    }

    public void PlayLoop(String[][] board)
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("\nEnter opponent's move: ");
        String input = keyboard.nextLine();

        int row = Character.getNumericValue(input.charAt(0)) - 10;
        //System.out.println(row);
        int col = Character.getNumericValue(input.charAt(1)) - 1;
        //System.out.println(col);

        while (!Validate(row, col, board))
        {
            System.out.println("Invalid input");
            System.out.print("\nEnter opponent's move: ");
            input = keyboard.nextLine();
            row = Character.getNumericValue(input.charAt(0)) - 10;
            //System.out.println(row);
            col = Character.getNumericValue(input.charAt(1)) - 1;
            //System.out.println(col);

        }
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j].equals("O"))
                {
                    board[i][j] = "#";
                    break;
                }
            }
        }
        board[row][col] = "O";

        //Display(board);
        //System.out.println("Player chose " + input.charAt(0) + (col + 1));
    }

    public String[][] Iteration(String[][] board)
    {
        //Max level
        State A = new State(board, null);
        currDepth++;

        //Min level
        //No loop needed because it's just one state to start (the root)
        GenerateSuccessors(A, "X", currDepth);
        currDepth++;

        //Max level
        State c;
        State d;
        State e;
        for (int i = 0; i < A.getSuccessors().size(); i++)
        {
            c = A.getSuccessors().get(i);
            GenerateSuccessors(c, "O", currDepth);

            //Max level leaves to choose Alpha from
            for (int j = 0; j < c.getSuccessors().size(); j++)
            {
                d = c.getSuccessors().get(j);
                GenerateSuccessors(d, "X", currDepth);
            }

        }

        String[][] chosenMove = new String[7][7];
        for (int k = 0; k < A.getSuccessors().size(); k++)
        {
            //Min level
            c = A.getSuccessors().get(k);
            for (int l = 0; l < c.getSuccessors().size(); l++)
            {
                //Max level
                d = c.getSuccessors().get(l);
                for (int m = 0; m < d.getSuccessors().size(); m++)
                {
                    //leaves
                    e = d.getSuccessors().get(m);
                    if (e.getVal() > d.getAlpha())
                    {
                        //choose largest leaf for max

                        d.setAlpha(e.getVal());
                    }

                }
                //set max node's value equal to largest leaf
                d.setVal(d.getAlpha());

                //if node is smaller than current smallest value for min
                if (d.getVal() < c.getBeta())
                {
                    //set equal to smallest value for min
                    c.setBeta(d.getVal());
                }
                else
                {
                    //else break out of loop to save time
                    continue;
                }
            }
            //set Min val equal to smallest value it could get
            c.setVal(c.getBeta());

            //if returned value is larger than current largest value for max
            if (c.getVal() > A.getAlpha())
            {
                A.setAlpha(c.getVal());
                chosenMove = c.getBoard();
            }
            //Random possibility in case of tiebreaker;
            else if (c.getVal() == A.getAlpha())
            {
                Random coinflip = new Random();
                int coin = coinflip.nextInt(2);
                if (coin == 1)
                {
                    A.setAlpha(c.getVal());
                    chosenMove = c.getBoard();
                }
            }
            else
            {
                continue;
            }

        }
        //Set value equal to largest value it could get in tree
        A.setVal(A.getAlpha());
        int newRow = 0;
        int newCol = 0;

        for (int a = 0; a < A.getBoard().length; a++)
        {
            for (int b = 0; b < A.getBoard().length; b++)
            {
                if (A.getBoard()[a][b].equals("X"))
                {
                    A.changeBoard(a, b, "#");
                }
            }
        }
        for (int x = 0; x < chosenMove.length; x++)
        {
            for (int y = 0; y < chosenMove.length; y++)
            {
                if (chosenMove[x][y].equals("X"))
                {
                    A.changeBoard(x, y, "X");
                }
            }
        }
        int m = 0;
        int n = 0;
        for (int x = 0; x < chosenMove.length; x++)
        {
            for (int y = 0; y < chosenMove.length; y++)
            {
                if (A.getBoard()[x][y] == "X")
                {
                    m = x;
                    n = y;
                    break;
                }
            }
        }
        char v = legend[m];

        System.out.println();
        
        Display(A.getBoard());

        System.out.println("\nComputer chose " + v + (n + 1));
        return A.getBoard();

    }

    public void Display(String[][] board)
    {
        String[][] b = new String[9][9];
        char legend = 'A';
        for (int a = 0; a < 9; a++)
        {
            if (a == 0)
            {
                System.out.print("  ");
            }
            else
            {
                System.out.print(a + " ");

            }
        }
        System.out.println();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (j == 0)
                {
                    System.out.print(legend + " ");
                    legend++;
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void Copy(String[][] boardA, String[][] boardB)
    {
        for (int i = 0; i < boardA.length; i++)
        {
            for (int j = 0; j < boardA.length; j++)
            {
                boardA[i][j] = boardB[i][j];
            }

        }
    }

    public void GenerateSuccessors(State s, String player, int depth)
    {
        int playerPosRow = 0;
        int playerPosCol = 0;

        for (int i = 0; i < s.getBoard().length; i++)
        {
            for (int j = 0; j < s.getBoard().length; j++)
            {
                if (s.getBoard()[i][j].equals(player))
                {
                    playerPosRow = i;
                    playerPosCol = j;
                    break;
                }
            }
        }

        //possible states up
        int currRow = playerPosRow - 1;
        int currCol = playerPosCol;

        while (currRow >= 0 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow--;

        }

        //possible states down
        currRow = playerPosRow + 1;
        while (currRow <= 7 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow++;

        }

        //possible states left
        currRow = playerPosRow;
        currCol = playerPosCol - 1;
        while (currCol >= 0 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currCol--;

        }

        //possible states right
        currRow = playerPosRow;
        currCol = playerPosCol + 1;
        while (currCol <= 7 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currCol++;

        }

        //possible states up-left
        currRow = playerPosRow - 1;
        currCol = playerPosCol - 1;
        while (currRow >= 0 && currCol >= 0 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow--;
            currCol--;

        }

        //possible states up-right
        currRow = playerPosRow - 1;
        currCol = playerPosCol + 1;
        while (currRow >= 0 && currCol <= 7 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow--;
            currCol++;

        }

        //possible states down-right
        currRow = playerPosRow + 1;
        currCol = playerPosCol + 1;
        while (currRow <= 7 && currCol <= 7 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow++;
            currCol++;

        }

        //possible states down-left
        currRow = playerPosRow + 1;
        currCol = playerPosCol - 1;
        while (currRow <= 7 && currCol >= 0 && s.getBoard()[currRow][currCol].equals("-"))
        {
            String[][] repBoard = new String[8][8];
            Copy(repBoard, s.getBoard());
            repBoard[playerPosRow][playerPosCol] = "-";
            repBoard[currRow][currCol] = player;
            s.addSuccessor(new State(repBoard, s));
            currRow++;
            currCol--;

        }

    }

    public boolean Validate(int newRow, int newCol, String[][] board)
    {

        if (newRow > 7 || newRow < 0 || newCol > 7 || newCol < 0)
        {
            return false;
        }
        int oldRow = 0;
        int oldCol = 0;
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j].equals("O"))
                {
                    oldRow = i;
                    oldCol = j;
                    break;
                }
            }
        }
        boolean p = true;
        if (oldRow == newRow || oldCol == newCol || Math.abs(oldRow - newRow) == Math.abs(oldCol - newCol))
        {
            if (board[newRow][newCol].equals("-"))
            {

                while (p)
                {
                    if (oldRow < newRow)
                    {
                        oldRow++;
                    }
                    else if (oldRow > newRow)
                    {
                        oldRow--;
                    }

                    if (oldCol < newCol)
                    {
                        oldCol++;
                    }
                    else if (oldCol > newCol)
                    {
                        oldCol--;
                    }

                    if (!board[oldRow][oldCol].equals("-"))
                    {
                        return false;
                    }
                    if (oldRow == newRow && oldCol == newCol)
                    {
                        p = false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean GameDone(String[][] board, String player)
    {
        int xPos = 0;
        int yPos = 0;

        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j].equals(player))
                {
                    xPos = i;
                    yPos = j;
                    break;
                }

            }
        }

        if (xPos - 1 >= 0 && yPos - 1 >= 0)
        {
            if (board[xPos - 1][yPos - 1] == "-")
            {
                return false;
            }
        }
        if (xPos - 1 >= 0)
        {
            if (board[xPos - 1][yPos] == "-")
            {
                return false;
            }
        }
        if (xPos - 1 >= 0 && yPos + 1 <= 7)
        {
            if (board[xPos - 1][yPos + 1] == "-")
            {
                return false;
            }
        }

        if (yPos - 1 >= 0)
        {
            if (board[xPos][yPos - 1] == "-")
            {
                return false;
            }
        }

        if (yPos + 1 <= 7)
        {
            if (board[xPos][yPos + 1] == "-")
            {

                return false;
            }
        }

        if (xPos + 1 <= 7 && yPos + 1 <= 7)
        {
            if (board[xPos + 1][yPos + 1] == "-")
            {
                return false;
            }
        }

        if (xPos + 1 <= 7)
        {
            if (board[xPos + 1][yPos] == "-")
            {
                return false;
            }
        }

        if (xPos + 1 <= 7 && yPos - 1 <= 0)
        {
            if (board[xPos + 1][yPos - 1] == "-")
            {

                return false;
            }
        }

        System.out.println();
        System.out.println(player + " is out of moves!");
        return true;

    }
}
