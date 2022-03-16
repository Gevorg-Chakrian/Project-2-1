package AI.Tree;

import AI.State;
import Chessboard.Board;
import Chessboard.Dice;
import Chessboard.DiceFace;
import Chessboard.Square;
import Pieces.Piece;
import Utilities.Coord2D;
import Utilities.GlobalVariables;

import java.util.LinkedList;
import java.util.List;

//import static AI.Tree.ExpectiMiniMax.ExpectiNode.*;

public class ExpectiMiniMax {
    private final int DEPTH = GlobalVariables.EXPECTIDEPTH; //private/public/static
    public int real_turn = Board.turn;
    private String output;
    private static final String[] piece_names = {"Pawn", "Knight", "Bishop", "Rook", "Queen", "King"};
    public ExpectiMiniMax(){
        //real_board_turn = Board.turn;
        int roll = DiceFace.getN();
        //State s1 = new State(Board.getInstance().copy(), real_turn);
        //LinkedList<Integer> roll2 = getPossibleRolls(s1, real_turn);
        //System.out.println(roll2);
        //System.out.println(roll);

        do{
            State s = new State(Board.getInstance().copy(), real_turn);
            // System.out.println(real_turn + "=" + s.getPlayerNo());
            //System.out.println("expecti");
            State best_State = expectiMove(s, real_turn, DEPTH, roll);
            //System.out.println(roll + " in loop");
            if(best_State==null)
                continue;
            output = best_State.getMoveCoords();
        }while (output == null);
        String fixed = "";
        for(char c : output.toCharArray())
        {
            switch (c)
            {
                case '0':
                    fixed+=c;
                    break;
                case '1':
                    fixed+=c;
                    break;
                case '2':
                    fixed+=c;
                    break;
                case '3':
                    fixed+=c;
                    break;
                case '4':
                    fixed+=c;
                    break;
                case '5':
                    fixed+=c;
                    break;
                case '6':
                    fixed+=c;
                    break;
                case '7':
                    fixed+=c;
                    break;
                case '8':
                    fixed+=c;
                    break;
                case '9':
                    fixed+=c;
                    break;
            }
        }
        output=fixed;
       // System.out.println(output);

    }

    public Coord2D getOriginCoordinate(){
        Coord2D origin = new Coord2D(Character.getNumericValue(output.charAt(0)),Character.getNumericValue(output.charAt(1)));

       // System.out.println("ORIGIN COOOORDS: "  + origin.getX() + " , " + origin.getY());

        return origin;
    }

    public Coord2D getDestinationCoordinate(){
        Coord2D dest = new Coord2D(Character.getNumericValue(output.charAt(2)),Character.getNumericValue(output.charAt(3)));

       // System.out.println("DESTINATION COOOORDS: "  + dest.getX() + " , " + dest.getY());

        return dest;
    }

    private double expecti(State s, int depth, int roll, int player, boolean chance, int starting_player){ //depth //is int
        double alpha = 0;
        s.setPlayerNo(player);


        if (isKingDead(s,player)|| depth == 0){
           // System.out.println("Expecti score =" +s.getScore());
            return s.getScore();//getScore??? //eval(s)
        }
        if (chance) {
            alpha = 0; //remove?
            //get list of possible rolls
            for (int rolls : getPossibleRolls(s, player)) { //TODO: here we need to access the available rolls and probability is 1/nbof available rolls
                alpha = alpha + ((1/(double) getPossibleRolls(s, player).size())*expecti(s, depth-1, rolls, player, false, starting_player)); //TODO: need to get the parent so that we know the next play is min or max
            }
        }
        else{
            if (player == -starting_player) {
                //force code dice roll
                alpha = Integer.MAX_VALUE;
                for (State nextState : s.getAllPossibleStates(roll)) { //TODO: get all possible moves for max player List<Moves>=availableactions(s,max)
                    // make sure its only moves with specific dice roll
                    alpha = Math.min(alpha, expecti(nextState, depth - 1, roll, -player, true, starting_player));
                }
            } else if (player == starting_player) { //max player see if should be min
                alpha = Integer.MIN_VALUE;
                for (State nextState : s.getAllPossibleStates(roll)) { //TODO: get all possible moves for min player
                    alpha = Math.max(alpha, expecti(nextState, depth - 1, roll, -player, true, starting_player));
                }
            }
        }
        return alpha;
    }

    private boolean isKingDead(State s, int playerNo)
    {
        boolean kingWhite = false;
        boolean kingBlack = false;
        int playerA;
        int playerB;

        if(playerNo==-1) {
            playerA = 1;
            playerB=-1;
        }
        else {
            playerA = -1;
            playerB=1;
        }


        Board b = s.getBoard();
        for(Square[] sV : b.getSquares())
        {
            for (Square sq : sV)
            {
                if(sq.isOccupied()) {
                    if (sq.getPiece().equals("King") && sq.getPiece().getColor() == playerA) {
                        kingWhite = true;
                    } else if (sq.getPiece().equals("King") && sq.getPiece().getColor() == playerB) {
                        kingBlack = true;
                    }
                }
            }
        }
        return !(kingBlack && kingWhite);
    }

    public State expectiMove(State s, int player, int depth, int roll){ //add roll?
        s.setPlayerNo(player);
        //System.out.println(roll+" in expecti");
        List<State> state_list = s.getAllPossibleStates(roll);
        State bestAction = null;
        double bestValue = -100000000;
        double value;

        for (State new_state: state_list) {
            value = expecti(new_state, roll, -player, depth, true, player);
            if(value > bestValue){
                bestAction = new_state;
                bestValue = value;
            }
            String in = new_state.getMoveCoords();
            String fixed = "";
            for(char c : in.toCharArray())
            {
                switch (c)
                {
                    case '0':
                        fixed+=c;
                        break;
                    case '1':
                        fixed+=c;
                        break;
                    case '2':
                        fixed+=c;
                        break;
                    case '3':
                        fixed+=c;
                        break;
                    case '4':
                        fixed+=c;
                        break;
                    case '5':
                        fixed+=c;
                        break;
                    case '6':
                        fixed+=c;
                        break;
                    case '7':
                        fixed+=c;
                        break;
                    case '8':
                        fixed+=c;
                        break;
                    case '9':
                        fixed+=c;
                        break;
                }
            }
            in=fixed;
            if(Board.getInstance().getSquares()[Character.getNumericValue(in.charAt(2))][Character.getNumericValue(in.charAt(3))].isOccupied()
            && Board.getInstance().getSquares()[Character.getNumericValue(in.charAt(2))][Character.getNumericValue(in.charAt(3))].getPiece().toString().equals("King"))
                return new_state;
        }
        return bestAction;
    }
    private LinkedList<Integer> getPossibleRolls(State s, int player){
        LinkedList<Piece> piece_list = player == 1 ? s.getBoard().copy().getWhitePieces() : s.getBoard().copy().getBlackPieces();

        return Dice.getRolls(piece_list);
        /*for (Piece piece_available: piece_list) {
            Dice.checkIfCanMove(piece_available);
            if((piece_available.toString()).equals("pawn")){

            }
        }*/
        //Dice.checkPieces(piece_list);



        //int roll = Dice.roll(state, player);
        //list of states knowing state player roll

    }
    /*private List<State> getStates(State s, int roll, int player){ //player might not be neceaary //TODO: check if Board.turn fucks everything up
        s.setPlayerNo(player);
        LinkedList<Piece> piece_list = player == 1 ? s.getBoard().copy().getWhitePieces() : s.getBoard().getBlackPieces();
        List<State> new_states = new LinkedList<>();
        for (Piece piece: piece_list) {
            for(int i = 0; i<6; i++) {
                if (piece.toString().equals(piece_names[i]) && (roll == i + 1)) {
                    //Board.turn=player;
                    new_states.addAll(s.checkAllStates(piece));//what if no possible moves
                    Board.turn = real_board_turn;
                }
            }
        }
        return new_states;
    }*/
}
//TODO: not stop if chance
//TODO: max/min for chance

//dice setRoll available actions/state