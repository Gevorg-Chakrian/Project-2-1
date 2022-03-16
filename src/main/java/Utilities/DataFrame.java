package Utilities;

import AI.State;
import Chessboard.Board;
import Chessboard.Square;
import Pieces.Piece;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFrame {
    String[][] squares;
    
    
    private static final int  TOTALSQUARES = 64;
    private static final int  TOTALNUMERICSQUARES = 768;
    
    double[][] numerical_squares;
    double[][] starting_position;
    double[][] ending_position;
    int[] label;


    double[][] processed_data;
    double[][] train_set;
    double[][] test_set;

    int[] train_label;
    int[] test_label;
    public DataFrame(ArrayList<String[]> data) {
        System.out.println(data.size()+ " Data Size!");
        processed_data = new double[data.size()][TOTALSQUARES+TOTALSQUARES+TOTALNUMERICSQUARES];

        get_state_vector(data);
        store_positions(data, TOTALSQUARES, 128,TOTALNUMERICSQUARES);
        store_positions(data, 128, 192,TOTALNUMERICSQUARES+64);
        store_labels(data);



        train_set = Arrays.copyOfRange(processed_data, 0, (processed_data.length*2)/3 + 1);
        test_set = Arrays.copyOfRange(processed_data, processed_data.length- (processed_data.length)/3, processed_data.length + 1);

        train_label = Arrays.copyOfRange(label, 0, (processed_data.length*2)/3 + 1);
        test_label = Arrays.copyOfRange(label, processed_data.length- (processed_data.length)/3, processed_data.length + 1);

        int pos_train = 0;
        int pos_test = 0;


        for(int i = 0; i < train_label.length;i++){
            if(train_label[i]==1){
                pos_train++;
            }
        }
        for(int i = 0; i < test_label.length;i++){
            if(test_label[i]==1){
                pos_test++;
            }
        }
       // print_processed_data();
        System.out.println("Positive Train: "+ pos_train + "; Negative Train : " + (train_label.length-pos_train));

        System.out.println("Positive Test: "+ pos_test + "; Negative Test : " + (test_label.length-pos_test));



        System.out.println(label[label.length-1]+ " Label"); // LABEL IS 1


    }
    public void print_processed_data(){
        boolean print = false;
        System.out.println(" FILE CONVERT VECTOR");
        for(int i = 0 ; i<processed_data.length;i++){

            if(i == 0){
                print=true;
            }

            if(print) {
                System.out.println("SQUARES");

                for(int j = 0; j<TOTALNUMERICSQUARES;j++){
                    System.out.print(processed_data[i][j]+ ",");
                }
                System.out.println();
                System.out.println("POSITIONS");
                for (int j = 0; j < processed_data[0].length; j++) {
                    if(j<=TOTALNUMERICSQUARES){
                        continue;
                    }
                    System.out.print(processed_data[i][j] + ",");
                }
                print=false;
                //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }


        }
        System.out.println(processed_data[0].length + ": Length of processed_data");
        System.exit(0);
    }

    public void get_state_vector(List<String[]> data) {
        int current_index;
      //  numerical_squares = new double[squares.length][TOTALNUMERICSQUARES];
        for (int i = 0; i < data.size()/*squares.length*/; i++) {
            //System.out.println("I =" + i);
            current_index=0;
            for (int j = 0; j < TOTALSQUARES /*squares[0].length*/; j++) {
                int number = classify_figure(data.get(i)[j]);
                if (number != 0) {
                  //  numerical_squares[i][current_index + number - 1] = 1;
                    processed_data[i][current_index+number-1] = 1;
                }


                if(i==0) {
                    //System.out.println();
                    //System.out.println("For square " + data.get(i)[j]);
                    for (int l = current_index; l < current_index + 12; l++) {
                      //  System.out.print(processed_data[i][l] + ",");
                    }
                }

                current_index += 12;
            }
        }
    }

    public void store_squares(List<String[]> data) {
        squares = new String[data.size()][TOTALSQUARES];
        for (int i = 0; i < data.size(); i++) {
            System.arraycopy(data.get(i), 0, squares[i], 0, TOTALSQUARES);
        }
    }


    public int classify_figure(String s) {
        switch (s) {
            case "None":
                return 0;
            case "P":
                return 1;
            case "N":
                return 2;
            case "B":
                return 3;
            case "R":
                return 4;
            case "Q":
                return 5;
            case "K":
                return 6;
            case "p":
                return 7;
            case "n":
                return 8;
            case "b":
                return 9;
            case "r":
                return 10;
            case "q":
                return 11;
            case "k":
                return 12;
        }
        return 0;
    }
    public static int classify_board_figure(Square s) {
        if(!s.isOccupied()){
            return 0;
        }
        Piece p  = s.getPiece();

        if(p.toString().equals("Pawn")){
            if(p.getColor()==1){
                return 1;
            }else return 7;
        }
        else if(p.toString().equals("Knight")){
            if(p.getColor()==1){
                return 2;
            }else return 8;
        }
        else if(p.toString().equals("Bishop")){
            if(p.getColor()==1){
                return 3;
            }else return 9;
        }
        else if(p.toString().equals("Rook")){
            if(p.getColor()==1){
                return 4;
            }else return 10;
        }
        else if(p.toString().equals("Queen")){
            if(p.getColor()==1){
                return 5;
            }else return 11;
        }
        else if(p.toString().equals("King")){
            if(p.getColor()==1){
                return 6;
            }else return 12;
        }

        return 0;
    }


    //TODO process the data so it's in one vector, then try to do stuff with the Logistic Class
    public double[][] getDataVector(){
        System.out.println("Data length: "+ processed_data.length);
        return processed_data;
    }

    public double[][] getTrainData(){
        int train_index = (processed_data.length*2)/3;
        System.out.println("Train Index: " + train_index);
        return Arrays.copyOfRange(processed_data, 0, (processed_data.length*2)/3 + 1);
    }
    public double[][] getTestData(){
        int test_index = processed_data.length- (processed_data.length*2)/3;
        System.out.println("Test Index: " + test_index);
        return Arrays.copyOfRange(processed_data, processed_data.length- (processed_data.length*2)/3, processed_data.length + 1);
    }

    public int[] getLabel() {
        return label;
    }

    public void store_labels(List<String[]> data) {
        label = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            label[i] = Boolean.parseBoolean(data.get(i)[data.get(0).length - 1].toLowerCase()) ? 1:0;
        }
        /*
        for(int i = 0 ; i < data.size();i++){
            processed_data[i][processed_data[0].length-1]=Boolean.parseBoolean(data.get(i)[data.get(0).length - 1].toLowerCase()) ? 1:0;
        }
         */
    }



    public void store_positions(List<String[]> data, int start_index, int end_index, int processed_index) {
        for (int i = 0; i < data.size(); i++) {
            String[] datum = Arrays.copyOfRange(data.get(i), start_index, end_index);
          //  if (datum[0].contains("f") || datum[0].contains("t")) {
         //       continue;
         //   }
            for (int j = 0; j < end_index - start_index; j++) {
              //  position[i][j] = Double.parseDouble(datum[j]);
                processed_data[i][processed_index+j] = Double.parseDouble(datum[j]);
            }
        }
    }

    public static double[] convert_to_vector(State s,String move){
        double[] vector = new double[TOTALSQUARES+TOTALSQUARES+TOTALNUMERICSQUARES];
      //  System.out.println("PRINTING GAME");
        Board board = s.getBoard();
       // String move = "6775";//s.getMoveCoords().replaceAll("[^0-9]", "").substring(0,4);
                    //6564

        int column_from = Character.getNumericValue(move.charAt(0));
        int row_from = Character.getNumericValue(move.charAt(1));

        int column_to = Character.getNumericValue(move.charAt(2));
        int row_to = Character.getNumericValue(move.charAt(3));

        Square[][] squares = board.getSquares();

        int current_index=0;
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[0].length; j++) {
                int number = classify_board_figure(squares[i][j]);
                if (number != 0) {
                    vector[current_index+number-1] = 1;
                }
                current_index += 12;
            }
        }

        vector[current_index+((row_from)*8)+column_from] = 1;
        vector[current_index+TOTALSQUARES+((row_to)*8)+column_to] = 1;
        //print_board_data(vector);
        return vector;

    }

    public static void print_board_data(double[] vector){
        System.out.println(" GAME TO VECTOR");


        System.out.println(" SQUARES :");
        for(int i = 0 ; i < TOTALNUMERICSQUARES;i++){
            System.out.print(vector[i] + ",");
        }
        System.out.println();

        System.out.println(" POSITIONS");
        for(int i = 0 ; i < vector.length;i++){
            if(i<=TOTALNUMERICSQUARES){
                continue;
            }
            System.out.print(vector[i] + ",");
        }
        System.out.println(vector.length + " Length of Game Vector");

    }

        //12   ---  (1+1)*7 + 2

    public double[][] getTrain_set() {
        return train_set;
    }

    public double[][] getTest_set() {
        return test_set;
    }

    public int[] getTrain_label() {
        return train_label;
    }

    public int[] getTest_label() {
        return test_label;
    }
}
