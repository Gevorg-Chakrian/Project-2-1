package Chessboard;

import AI.RandomAgent;
import AI.State;
import AI.Tree.ExpectiMiniMax;
import AI.WeightCalculator;
import MCTSAttempt.MonteCarloTreeSearch;
import Pieces.Piece;
import Utilities.Coord2D;
import Utilities.GlobalVariables;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.LinkedList;


public class Board extends Pane {
    public static boolean vsAI = true;
    public final static int IN_PROGRESS = 0;
    public static int turn = 1;
    public static boolean isTest = true;


    public static Board singleton = null;
    public static double scale = 0.6;

    private final boolean isAIon = true;

    public int turnCount = 0;


    private final LinkedList<String> log = new LinkedList<>();
    private LinkedList<Piece> WhitePieces = new LinkedList<>();
    private LinkedList<Piece> BlackPieces = new LinkedList<>();

    private Square[][] squares = new Square[8][8];

    private TextArea logArea;
    private Button diceB;
    private Stage main;
    private Scene win;
    private ImageView dice;


    public LinkedList<Piece> getWhitePieces() {
        return WhitePieces;
    }

    public LinkedList<Piece> getBlackPieces() {
        return BlackPieces;
    }


    public Square[][] getSquares() {
        return squares;
    }

    public static Board getInstance() {
        if (singleton == null) {
            singleton = new Board(scale);
        }
        return singleton;

    }

    public static void setInstance(Board tmp) {
        singleton = tmp;
    }

    public void setWhitePieces(LinkedList<Piece> whitePieces) {
        WhitePieces = whitePieces;
    }

    public void setBlackPieces(LinkedList<Piece> blackPieces) {
        BlackPieces = blackPieces;
    }

    public void setSquares(Square[][] squares) {
        this.squares = squares;
    }

    // copy from main to sim
    public Board copy() {
        Board simulationBoard = new Board();

        copy_squares(simulationBoard);
        copy_pieces(simulationBoard);

        return simulationBoard;
    }

    private void copy_squares(Board simulationBoard) {
        for (int i = 0; i < this.getSquares().length; i++) {
            for (int j = 0; j < this.getSquares().length; j++) {
                simulationBoard.getSquares()[i][j] = this.getSquares()[i][j].copy(simulationBoard);
            }
        }
    }

    private void copy_pieces(Board simulationBoard) {
        for (Piece piece : this.getWhitePieces()) {
            simulationBoard.getWhitePieces().add(piece.copy());
        }
        for (Piece piece : this.getBlackPieces()) {
            simulationBoard.getBlackPieces().add(piece.copy());
        }
    }

    public Board(double scale) {
        this.setDepthTest(DepthTest.ENABLE);
        Board.scale = scale;
        //start_game();
    }

    // special constructor for Simulations of Current Board
    private Board() {

    }

    public void updateGraphics() {
        for (Square[] sA : squares) {
            for (Square s : sA) {
                s.update();
            }
        }
        //  if(GlobalVariables.PRINTLOG) System.out.println("GRAPHIC UPDATED");
      //  if(GlobalVariables.PRINTLOG) System.out.println("GRAPHIC UPDATED");

    }

    public void addToLog(String s) {
        log.add(s);
    }

    public void placeboard(final int i, final int j) {
        getChildren().add(squares[i][j]);
    }

    public Square getSquareByNotation(String notation) {
        int row;
        int column = -1;
        char char_column = notation.charAt(0);

        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == char_column) {
                column = i;
                break;
            }

        }
        row = Integer.parseInt(String.valueOf(notation.charAt(1)))-1;

        return squares[row][column];

    }
    public void expectiMiniMaxTurn(){
        turnCount++;
        Dice.throwDice();
        ExpectiMiniMax expectiMiniMax = new ExpectiMiniMax();
        int real_board_turn = Board.turn;
        int roll = DiceFace.getN();
        State s = new State(Board.getInstance().copy(), real_board_turn);
        System.out.println(real_board_turn + "=" + s.getPlayerNo());
        Dice.wasThrown=true;
        Coord2D origin_coords = expectiMiniMax.getOriginCoordinate();
        Square origin = getSpecificSquare(origin_coords.getX(), origin_coords.getY());
        Coord2D dest_coords = expectiMiniMax.getDestinationCoordinate();
        Square destination = getSpecificSquare(dest_coords.getX(), dest_coords.getY());
        System.out.println("~~~~~~~~~~~~~~~~~~~EXPECTI MOVE~~~~~~~~~~~~~~~~~~~");
        System.out.println(origin.getPiece() +" FROM " + origin + " TO "+ destination);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        boolean kingWasTaken =false;
        if(destination.isOccupied()) {
            kingWasTaken = destination.getPiece().toString().equals("King");
        }

        origin.getPiece().move(destination, this);
        singleton.updateGraphics();
        System.out.println("KING WAS TAKEN : " + kingWasTaken);
        if(kingWasTaken){//(isKingDead()){

            this.finishGame();
        }else if(GlobalVariables.isExperiment) {
            mctsTurn();
        }
    }

    public void aiTurn() {
        turnCount++;
        if (GlobalVariables.PRINTLOG) System.out.println("--RNDM AGENT");
        Dice.throwDice();
        if (GlobalVariables.PRINTLOG) System.out.println("AIS");
        RandomAgent randomAgent = new RandomAgent();randomAgent.initialize(singleton.getSquares(),DiceFace.getN(),Board.getInstance(),GlobalVariables.GOALSIDE);
        singleton.getDice().setImage(DiceFace.getI().getImage());
        if (GlobalVariables.PRINTLOG) System.out.println("\n\nMoving start:");
        Square destination = randomAgent.getRandomSquare();
        System.out.println("~~~~~~~~~~~~~~~~~~~AI MOVE~~~~~~~~~~~~~~~~~~~");
        System.out.println(randomAgent.getStartingSquare().getPiece() +" FROM " + randomAgent.getStartingSquare() + " TO "+ destination);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        randomAgent.getStartingSquare().getPiece().move(destination,this);

        randomAgent.extPrint(singleton.getSquares());
        singleton.updateGraphics();
       // System.out.println(this.checkStatus(true));
        //if (kingWasTaken) {

        //System.out.println(this.checkStatus(true));
        if(isKingDead()) {
            this.finishGame();
        }
        if(GlobalVariables.isExperiment) {
            mctsTurn();
        }
        //}

    }

    public void mctsTurn()
    {


        GlobalVariables.mcts_turn=true;
        turnCount++;
        singleton.getDice().setImage(DiceFace.getI().getImage());
        Dice.throwDice();
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();

        String mv = mcts.findNextMove(this,GlobalVariables.GOALSIDE);
        Dice.wasThrown=true;
        singleton.getDice().setImage(DiceFace.getI().getImage());Square origin = getSpecificSquare(Character.getNumericValue(mv.charAt(0)),Character.getNumericValue(mv.charAt(1)));
        Square destination = getSpecificSquare(Character.getNumericValue(mv.charAt(2)),Character.getNumericValue(mv.charAt(3)));
     //   System.out.println("MCTS--");



        //origin.getPiece().togglePrint();
        // DiceFace.setN(getSquareValue(origin));
        if (GlobalVariables.PRINTLOG) System.out.println("Legal: " + origin.getPiece().checkLegal(destination, this));
        if (!origin.getPiece().checkLegal(destination, this))
            mctsTurn();
        if (GlobalVariables.PRINTLOG) System.out.println("Turn " + turn);
        if (GlobalVariables.PRINTLOG) System.out.println("DiceStatus: " + Dice.wasThrown);
        //origin.getPiece().togglePrint();


        if(GlobalVariables.isExperiment) {
            System.out.println("~~~~~~~~~~~~~~~~~~~MCTS MOVE~~~~~~~~~~~~~~~~~~~");
            System.out.println(origin.getPiece() + " FROM " + origin + " TO " + destination);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        boolean kingWasTaken =false;
        if(destination.isOccupied()) {
             kingWasTaken = destination.getPiece().toString().equals("King");
        }

        origin.getPiece().move(destination, this);

        printMat(this.getSquares(), origin, destination);
        if (GlobalVariables.PRINTLOG) System.out.println("DiceStatus: " + Dice.wasThrown);
        if (GlobalVariables.PRINTLOG) System.out.println("ORGN " + origin);
        if (GlobalVariables.PRINTLOG) System.out.println("DST " + destination);
        if (GlobalVariables.PRINTLOG) System.out.println("MCTS DONE");
        printMat(this.getSquares(),origin,destination);

        singleton.updateGraphics();
        GlobalVariables.mcts_turn=false;


        System.out.println("KING WAS TAKEN : "+ kingWasTaken);
        if (kingWasTaken){//(isKingDead()) {
            System.out.println("GAME WAS FINISHED");
            this.finishGame();

        } else if(GlobalVariables.isExperiment) {
            if (GlobalVariables.ISGOALOREXPECTI) {
                aiTurn();
            }else {
                expectiMiniMaxTurn();
            }
        }
    }

    /**
     * Seems graphical but it's to avoid the need of the press of the button everytime to execute every single turn.
     * Considering that this method is here to lose some time it won't of course work on a simulation, but it's very useful in normal play.
     */
    private boolean moveAnimAtion(Square origin, Square destination)
    {
        System.out.println("Move Animation engaged");
        boolean out = false;
        float stepSize = 10;

        Coord2D originCoords = getSquareCoords(origin);
        Coord2D destinationCoords = getSquareCoords(destination);
        int deltaX = destinationCoords.getX()-originCoords.getX();
        int deltaY = destinationCoords.getY()-originCoords.getY();

        for (int i = 0; i < (int )stepSize; i++) {
            origin.relocate(origin.getTranslateX()+(deltaX/stepSize),origin.getTranslateY()+(deltaY/stepSize));

            try {
                synchronized(main){
                    System.out.println("Step - " + System.currentTimeMillis());
                    main.wait((long) (1000 / stepSize));

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            updateGraphics();
            if(i ==( (int) stepSize - 1))
                out=true;
        }

        return out;
    }

    private Coord2D getSquareCoords(Square target)
    {
        int x=0;
        int y=0;
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                if(squares[i][j].equals(target))
                    x=i;
                    y=j;
            }
        }
        x= 240 + ((x-1) * 60) + 30;
        y= 240 + ((y-1) * 60) + 30;

        return new Coord2D(x,y);
    }


    public Integer[][] getState() {
        Integer[][] state = new Integer[8][8];

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[i][j].isOccupied()) {
                    state[i][j] = WeightCalculator.calcWeightMultiplicator(squares[i][j]);
                } else state[i][j] = 0;
            }
        }
        return state;
    }




    public int checkStatus(boolean finalCheck)
    {
        Square[][] squares =this.squares;

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
            if(GlobalVariables.PRINTLOG) System.out.println("One KinG MISSING!!");



        if(isBlackKing && !isWhiteKing){
            if(GlobalVariables.PRINTLOG) System.out.println("BLACK WON");

            if(finalCheck) {

                for (Piece p : this.getBlackPieces()){
                    System.out.println(p);
                }
                GlobalVariables.stupid_string="BLACK WON";

                GlobalVariables.isGameDone = true;
                GlobalVariables.BLACKWON = 1;
                System.out.println("BLACK WON");
            }
            return 1; // TEMPORARY
            //return -1; //the winner is black
        }
        if(!isBlackKing && isWhiteKing){
            GlobalVariables.stupid_string="WHITE WON";
            if(GlobalVariables.PRINTLOG) System.out.println("WHITE WON");
            if(finalCheck){
                for (Piece p : this.getWhitePieces()){
                    System.out.println(p);
                }
                GlobalVariables.isGameDone = true;
                GlobalVariables.WHITEWON=1;
                System.out.println("WHITE WON");
            }
            return -1;
            //return 1; //the winner is white
        }
        return 0; // no winner yet

    }

    public void setLogArea(TextArea logArea) {
        this.logArea = logArea;
    }

    public void setDiceB(Button diceB) {
        this.diceB = diceB;
    }

    public TextArea getLogArea() {
        return logArea;
    }

    public void finishGame()
    {
        GlobalVariables.FIRSTGAME=false;

        this.checkStatus(true);
        main.setScene(win);

    }

    public static void setTurn(int turn) {
        Board.turn = turn;
    }

    public void setDice(ImageView dice) {
        this.dice = dice;
    }

    public ImageView getDice() {
        return dice;
    }

    public void setWin(Stage stageMain, Scene toChange) //game
    {
        this.main=stageMain;
        this.win=toChange;
    }
    public double getScale(){return scale;}

    public int getTurn() {
        return turn;
    }

    public void printMat(Square[][] squares,Square origin, Square dest)
    {
        Square[][] squaress = squares.clone();
        if(GlobalVariables.PRINTLOG) System.out.println("Matrix after move:");

        if(GlobalVariables.PRINTLOG) System.out.println("Printing squares of size- " + squaress.length + "x" + squaress[0].length);
        for (Square[] sv : squaress){
            for (Square s : sv)
                if(s.equals(origin))
                {
                    if(GlobalVariables.PRINTLOG) System.out.print("|"+"O"+"|");
                }
                else if(s.equals(dest))
                {
                    if(GlobalVariables.PRINTLOG) System.out.print("|"+ "D" +"|");
                }
                else {
                    if(GlobalVariables.PRINTLOG) System.out.print("|"+ getSquareValue(s)+"|");
                }

            if(GlobalVariables.PRINTLOG) System.out.println();
        }
    }

    private int getSquareValue(Square s)
    {
        if(!s.isOccupied())
            return 0;

        if(s.getPiece().toString().equals("Pawn"))
            return 1;
        if(s.getPiece().toString().equals("Knight"))
            return 2;
        if(s.getPiece().toString().equals("Rook"))
            return 4;
        if(s.getPiece().toString().equals("Bishop"))
            return 3;
        if(s.getPiece().toString().equals("Queen"))
            return 5;
        if(s.getPiece().toString().equals("King"))
            return 6;

        return 0;
    }

    public Square getSpecificSquare(int x, int y)
    {
        if(GlobalVariables.PRINTLOG) System.out.println("Specific Square required for x: " + x + "and y: " + y);
        return squares[x][y];
    }

    public int getTurnCount() {
        return turnCount;
    }
    public void resetTurnCount()
    {
        turnCount=0;
    }

    private boolean isKingDead()
    {
        boolean kingWhite = false;
        boolean kingBlack = false;


        for(Square[] sV : this.getSquares())
        {
            for (Square sq : sV)
            {
                if(sq.isOccupied()) {
                    if (sq.getPiece().toString().equals("King") && sq.getPiece().getColor() == GlobalVariables.GOALSIDE) {
                        kingWhite = true;
                        System.out.println("WHITE KING ALIVE");
                    } else if (sq.getPiece().toString().equals("King") && sq.getPiece().getColor() == (GlobalVariables.GOALSIDE*-1)) {
                        kingBlack = true;
                        System.out.println("BLACK KING ALIVE");
                    }
                }
            }
        }
        return !(kingBlack && kingWhite);
    }
}
