package Chessboard;

import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Piece_Pruning;

import java.util.*;

//should throw dice return new Object or new
public class Dice {

    private static final Random rand = new Random();
    private static final LinkedList<Integer> Random_Set = new LinkedList<>();
    public static boolean wasThrown = false;

    private static final String[] names = {"Pawn", "Knight", "Bishop", "Rook", "Queen", "King"};
    private static final int[] numbers = {1, 2, 3, 4, 5, 6}; //private/public


    public static int throwDice() {
        DiceFace.testThrow = true;
        //System.out.println(" Threw Dice!");
        Random_Set.clear();
        if (Board.turn == 1) {
            checkPieces(Board.getInstance().getWhitePieces());
        } else checkPieces(Board.getInstance().getBlackPieces());
        DiceFace.setN(Random_Set.get(rand.nextInt(Random_Set.size())));
        DiceFace.testThrow = false;

        wasThrown = true;
        return DiceFace.getN();
    }

    public static LinkedList<Integer> getRolls(LinkedList<Piece> Pieces){ //static?
        checkPieces(Pieces);
        return Random_Set; //Check if need to deep copy


       /* int [] rolls;
        for (Piece piece : Pieces) {
            if (Piece_Pruning.move_pruning(piece, Board.getInstance())) {
                continue;
            }
            for (int i = 0; i < numbers.length; i++) {
                if(checkIfCanMove(piece)) {
                    checkByName(piece, names[i], numbers[i]);
                    rolls[i]=
                }
            }
        }*/
    }
    public static void checkPieces(LinkedList<Piece> Pieces) { //TODO: public to private check
        for (Piece piece : Pieces) {
            if (Piece_Pruning.move_pruning(piece, Board.getInstance())) {
                continue;
            }
            for (int i = 0; i < numbers.length; i++) {
                if(checkIfCanMove(piece)) checkByName(piece, names[i], numbers[i]);
            }
        }
    }

    private static void checkByName(Piece Piece, String name, int number) {
        if (Piece.toString().equals(name)) {
            if (!Random_Set.contains(number)) {
                Random_Set.add(number);
            }

        }
    }

    public static boolean checkIfCanMove(Piece piece) {

        for (int i = 0; i < Board.getInstance().getSquares().length; i++) {
            for (int k = 0; k < Board.getInstance().getSquares().length; k++) {
                if (piece.checkLegal(Board.getInstance().getSquares()[i][k],Board.getInstance())) {
                    if(Objects.equals(piece.toString(), "Pawn") &&(piece.getRow()==1 || piece.getRow()==6)){
                            Pawn p = (Pawn) piece;
                            p.hasMoved(false);
                    }

                    return true;
                }
            }
        }
        return false;
    }


}
