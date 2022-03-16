package Utilities;

import AI.State;
import Chessboard.Board;
import Chessboard.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CsvReader {
    public static final char PAWN = 'P';
    private static final char KNIGHT = 'N';
    private static final char BISHOP = 'B';
    private static final char ROOK ='R';
    private static final char QUEEN = 'Q';
    private static final char KING = 'K';


    public static void main(String[] args) throws IOException {
        Logistic logistic = new Logistic(896);

        String row;

            //659 files in fischer
        File folder = new File("D:\\Group19_Project2-1\\src\\main\\resources\\Data\\CSV_ALL");
        File[] listOfFiles = folder.listFiles();



        int i = 0;
        assert listOfFiles != null;
        for(int k=0;k<4;k++) {
            ArrayList<String[]> data = new ArrayList<>();

            for(int l = i;l<5+i;l++){
                System.out.println(listOfFiles[l].getName());



                if (listOfFiles[l].isFile()) {
                    boolean firstLine = true;
                    String pathToCsv = "\\Group19_Project2-1\\src\\main\\resources\\Data\\CSV_ALL\\" + listOfFiles[l].getName();
                    BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));


                    while ((row = csvReader.readLine()) != null) {
                        if (firstLine) {
                            firstLine = false;
                            continue;

                        }
                        String[] datum = row.split(",");


                        if(select_valid_move(datum,-1,KING)) {
                            if (datum[datum.length - 1].equals("True")) {
                                for (int f = 0; f <12; f++) {//12 for others!! 4 for PAWN
                                    data.add(datum);
                                }
                            } else data.add(datum);
                        }

                    }
                    csvReader.close();


                }
            }
            i+=193;
            System.out.println(i);
            //Game g = new Game();
            //g.start_game();
            //Board.getInstance().getSquareByNotation("d2").getPiece().move(Board.getInstance().getSquareByNotation("d4"), Board.getInstance());
          //  DataFrame.convert_to_vector(new State(Board.getInstance(), -1));

            DataFrame df = new DataFrame(data);


            logistic.logisticRegression(df);
            System.exit(0); // THIS IS FOR ONLY ONE BATCH OF FILES TO BE CONSIDERED IN FINDING WEIGHTS
        }

    }

    public static boolean select_valid_move(String[] datum,int role,char C){
        int index = -1;
        for(int i = 64;i <datum.length;i++) {
            if (datum[i].equals("1.0")) {
                index = i;
                break;
            }
        }
        if(role==-1){
            C = Character.toLowerCase(C);
        }


        char c = datum[index-64].charAt(0);

       // System.out.println(c +" : Character");
        if(!Character.isLowerCase(c) && role==1 && c==C) {
            return true;
        } else if(Character.isLowerCase(c) && role==-1 && c==C){
            return true;
        }

       // System.out.println("Returned false");
        return false;
    }







}
