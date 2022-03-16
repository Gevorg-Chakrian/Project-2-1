package AI;

import Chessboard.Board;
import Chessboard.Square;
import Utilities.GlobalVariables;

public class EvaluationFunction {
    private  double score = 0;

    //TODO
    // King Tropism sometimes gives NaNs,fucks up whole system, figure out why

    public  double getScore(State s) {
        double score1 = 0;
        if(!checkKings(s.getBoard()))
        {
            return 0;
        }
        if(!GlobalVariables.mcts_turn && GlobalVariables.mcts_noeval) {

            //todo increase piece eateble value
            score1 += KingSafety.getEnemySafetyScore(s.getBoard()/*copy()*/, s.playerNo) - KingSafety.getEnemySafetyScore(s.getBoard()/*copy()*/, s.getOpponent());
            score1 += Material.getMaterialScore(s.getBoard()/*copy()*/, s.playerNo) - Material.getMaterialScore(s.getBoard()/*copy()*/, s.getOpponent());
            score1 += TrappedPieces.getTrappedPiecesScore(s.getBoard()/*copy()*/, s.playerNo) - TrappedPieces.getTrappedPiecesScore(s.getBoard()/*copy()*/, s.getOpponent());
            score1 += Mobility.getMobilityScore(s.getBoard(), s.playerNo) - Mobility.getMobilityScore(s.getBoard(), s.getOpponent());
            // System.out.println(new PieceEatable(s.getBoard(), s.playerNo).getScore()- new PieceEatable(s.getBoard(),s.getOpponent()).getScore());
            score1 += (new PieceEatable(s.getBoard(), s.playerNo).getScore() - new PieceEatable(s.getBoard(), s.getOpponent()).getScore());
            score1 += (new SuicideRate(s.getBoard(), s.playerNo).getScore() - new SuicideRate(s.getBoard(), s.getOpponent()).getScore());
            score1 += new CenterControl().CalcScore(s.getBoard(), s.playerNo) - new CenterControl().CalcScore(s.getBoard(), s.getOpponent());
            double king_tropism = new KingTropism(s.getBoard(), s.playerNo, false).getOutput() - new KingTropism(s.getBoard(), s.getOpponent(), false).getOutput();

/*
        System.out.println("VALUES IN ORDER:");
        System.out.println((new PieceEatable(s.getBoard(), s.playerNo).getScore() - new PieceEatable(s.getBoard(),s.getOpponent()).getScore()));
        System.out.println((new SuicideRate(s.getBoard(),s.playerNo).getScore() - new SuicideRate(s.getBoard(),s.getOpponent()).getScore()));
        System.out.println(king_tropism);
*/

            int tropism = 0;
            if (!Double.isNaN(king_tropism)) {
                tropism++;
                score1 += king_tropism;
            }

            s.setScore(s.getScore() + score1 / 7 + tropism * 1.2);
            if (GlobalVariables.PRINTLOG) System.out.println("SCORE=" + s.getScore());

            if (GlobalVariables.PRINTLOG)
                System.out.println("Score evaluated = " + score + " of which suicide rate was " + (new SuicideRate(s.getBoard(), s.playerNo).getScore() - new SuicideRate(s.getBoard(), s.getOpponent()).getScore()));
        }


        return s.getScore();
    }



    private static boolean checkKings(Board board)
    {
        Square[][] squares = board.getSquares();

        boolean isBlackKing = false;
        boolean isWhiteKing = false;

        for(Square[] squareV : squares)
        {
            for(Square s : squareV)
            {
                if(s.isOccupied() && s.getPiece().getColor()==1&& s.getPiece().toString().equals("King"))
                {
                    isWhiteKing=true;
                }
                else if(s.isOccupied() && s.getPiece().getColor()==-1&& s.getPiece().toString().equals("King"))
                {
                    isBlackKing=true;
                }
            }
        }

        if(!isBlackKing || !isWhiteKing)
            if(GlobalVariables.PRINTLOG) System.out.println("One King MISSING!!");



        return isWhiteKing && isBlackKing;
    }


}
