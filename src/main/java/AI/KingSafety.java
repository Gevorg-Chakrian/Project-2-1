package AI;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Pieces.Piece;
import Utilities.GlobalVariables;
import Pieces.Piece_Pruning;

import java.util.LinkedList;

import static Utilities.Normalization.normalizeScore;

public class KingSafety {


    private static int turn;
    private static Board board;
    /**
     * not really safety score, but smth more like how unsafe enemy king is,
     * did what I found from literature : https://www.chessprogramming.org/King_Safety
     */
    public static double getEnemySafetyScore(Board board1, int setTurn){
        board=board1;

        turn = setTurn;
        DiceFace.testThrow = true;
        double score= getKingScore(getEnemyKingZone());
        DiceFace.testThrow = false;
        return normalizeScore(0.001, score);
    }


    // note, maybe i need to remove from movable sqaure if it is legal( checklegal returns true) but
    // its legal since king can capture a black piece, cause that kinda fucks with the scoring ig
    private static LinkedList<Square> getEnemyKingZone() {
        Square[][] squares = board.getSquares();
        LinkedList<Square> enemyKingZone = new LinkedList<>();
        Square enemyKingSquare;
        if (turn == 1) {
            enemyKingSquare = getEnemyKingSquare(squares, -1);
        } else enemyKingSquare = getEnemyKingSquare(squares, 1);
        enemyKingZone.add(enemyKingSquare);
        if(GlobalVariables.PRINTLOG) System.out.println("Enemy king Square: "+ enemyKingSquare);
        if(enemyKingSquare==null)
        {
            return null;
        }
        getEnemyKingMovableSquares(enemyKingZone,squares, enemyKingSquare.getRow(), enemyKingSquare.getColumn());

        if(GlobalVariables.PRINTLOG) System.out.println("Enemy king Zone "+ enemyKingZone);
        return enemyKingZone;

    }

    private static Square getEnemyKingSquare(Square[][] squares, int color) {
        for (int i = 0; i < squares.length;i ++) {
            for (int k = 0; k < squares.length; k++) {
                if (squares[i][k].isOccupied()) {
                    if (squares[i][k].getPiece().toString().equals("King")
                            && squares[i][k].getPiece().getColor() == color) {
                        return squares[i][k];
                    }
                }
            }
        }
        return null;
    }

    public static double getKingScore(LinkedList<Square> enemyKingZone){
        if(enemyKingZone == null)
            return 0;
        double[] attackWeight = {0,0,50,75,88,94,97,99,99,99,99,99,99,99,99,99};

        LinkedList<Piece> pieces;
        LinkedList<Piece> attackingPieces = new LinkedList<>();
        int valueOfAttacks=0;
        int nrOfAttackers=0;
        int nrOfAttackedSquares = 0;

        if(turn==1){pieces = board.getWhitePieces();}
        else {pieces = board.getBlackPieces();}


        /*
        for(int i = 0; i < board.getSquares().length;i++){
            for(int j = 0 ; j < board.getSquares().length;j++){
                if(board.getSquares()[i][j].isOccupied()&& board.getSquares()[i][j].getPiece().getColor()==turn){
                    Piece piece = board.getSquares()[i][j].getPiece();
                    if (Piece_Pruning.move_pruning(piece, board)) {
                        continue;
                    }
                    for(Square square: enemyKingZone){
                        if(piece.checkLegal(square,board)){
                            nrOfAttackedSquares+=1;

                            if(!attackingPieces.contains(piece)){
                                attackingPieces.add(piece);
                                nrOfAttackers+=1;
                            }
                        }
                    }
                    valueOfAttacks+=nrOfAttackedSquares*getPieceWeight(piece);
                    nrOfAttackedSquares=0;


                }
            }
        }

         */

        for(Piece piece : pieces){
            if(Piece_Pruning.move_pruning(piece,board)){
                continue;
            }
            for(Square square: enemyKingZone){
                if(piece.checkLegal(square,board)){
                    nrOfAttackedSquares+=1;

                    if(!attackingPieces.contains(piece)){
                        attackingPieces.add(piece);
                        if(GlobalVariables.PRINTLOG) System.out.println(piece + " ADDED TO ATTACKING PIECES;");
                        nrOfAttackers+=1;
                    }
                }
            }
            valueOfAttacks+=nrOfAttackedSquares*getPieceWeight(piece);
            nrOfAttackedSquares=0;

        }
        if(GlobalVariables.PRINTLOG) System.out.println("VALUE OF ATTACKS:" + valueOfAttacks);
        if(GlobalVariables.PRINTLOG) System.out.println("ATTACK WEIGHT: "+ attackWeight[nrOfAttackers]);
        return valueOfAttacks*(attackWeight[nrOfAttackers]/100);
    }

    private static int getPieceWeight(Piece piece){
        if(piece.toString().equals("Pawn"))
            return 1;
        if(piece.toString().equals("Knight"))
            return 20;
        if(piece.toString().equals("Rook"))
            return 40;
        if(piece.toString().equals("Bishop"))
            return 20;
        if(piece.toString().equals("Queen"))
            return 80;

        return 0;
    }




    /**
     * @param s squares
     * @param r row of king
     * @param c column of king
     * @return squares where king can move
     */
    private static void getEnemyKingMovableSquares(LinkedList<Square> enemyKingZone,Square[][] s, int r, int c) {

        int rowLowerBound,rowUpperBound,columnLowerBound,columnUpperBound;

        if(r>0){rowLowerBound=r-1;}else rowLowerBound=r;

        if(r<7){rowUpperBound=r+1;}else rowUpperBound=r;

        if(c>0){columnLowerBound=c-1;}else columnLowerBound=c;

        if(c<7){columnUpperBound=c+1;} else columnUpperBound=c;

        loopSquares(s,enemyKingZone,r,c,rowLowerBound,rowUpperBound,columnLowerBound,columnUpperBound);


      //  return enemyKingZone;
    }
    private static void additionalSquares(Square[][] s,LinkedList<Square> enemyKingZone,Square movableSquare){
        int r = movableSquare.getRow();
        int c = movableSquare.getColumn();
        if(turn==-1) {
            if(r<=6) {
                if(!enemyKingZone.contains(s[r + 1][c])) enemyKingZone.add(s[r + 1][c]);
            }
            if(r<=5) {
                if(!enemyKingZone.contains(s[r + 2][c])) enemyKingZone.add(s[r + 2][c]);
            }
        }
        if(turn==1){
            if(r>=1) {
                if(!enemyKingZone.contains(s[r - 1][c]))  enemyKingZone.add(s[r - 1][c]);
            }
            if(r>=2) {
                if(!enemyKingZone.contains(s[r - 2][c]))  enemyKingZone.add(s[r - 2][c]);
            }

        }
    }

    /**
     * @param s   squares
     * @param r   row of king
     * @param c   column of king
     * @param rlb row lower-bound
     * @param rub row upper-bound
     * @param clb column lower-bound
     * @param cub column upper-bound
     */
    private static void loopSquares(Square[][] s, LinkedList<Square> enemyKingZone, int r, int c, int rlb, int rub, int clb, int cub) {

        for (int i = rlb; i <= rub; i++) {
            for (int j = clb; j <= cub; j++) {
                if (s[r][c].getPiece().checkLegal(s[i][j],board)) {
                    if(!enemyKingZone.contains(s[i][j])){enemyKingZone.add(s[i][j]);}
                    additionalSquares(s,enemyKingZone,s[i][j]);

                }
            }
        }

    }
    
    public double normalize(double score){
        double newValue = (1 / (1+Math.exp(-0.001*score)));
        newValue = (newValue-0.5)*2;
        return newValue;
    }

}
