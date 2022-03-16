
package Chessboard;

import Pieces.Piece;
import Utilities.GlobalVariables;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Square extends StackPane {

    private boolean isOccupied;
    private Piece OccupyingPiece;
    private final int row;
    private int column;
    private double scale = 0.6;
    private Board board;


    public Square(boolean isOccupied,String notation){
        this.isOccupied = isOccupied;
        char char_column = notation.charAt(0);

        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (int i = 0 ; i < letters.length; i++){
            if(letters[i]==char_column){
                column = i;
            }

        }
         row = notation.charAt(1)-1;
    }

    public Square(boolean isOccupied,int row,int column, double scale, Board board) {
        this.isOccupied=isOccupied;
        this.row=row;
        this.column=column;
        this.scale=scale;
        this.board=board;
        this.setScaleX(scale);
        this.setScaleY(scale);
    }
    // for testing
    public Square(boolean isOccupied,int row, int column){
        this.isOccupied = isOccupied;
        this.row = row;
        this.column = column;
    }
    // for testing
    public Square(Piece OccupyingPiece){
        this.OccupyingPiece=OccupyingPiece;
        this.isOccupied=true;
        this.row=OccupyingPiece.getRow();
        this.column= OccupyingPiece.getColumn();
    }


    public Square(Piece OccupyingPiece, Board board){
        this.OccupyingPiece=OccupyingPiece;
        this.isOccupied=true;
        this.row=OccupyingPiece.getRow();
        this.column= OccupyingPiece.getColumn();
        this.board=board;
        this.scale=board.getScale();
        this.setScaleX(scale);
        this.setScaleY(scale);
        if(!Board.isTest) {
            setPiece(OccupyingPiece);
        }
    }

    private void setPiece(Piece OccupyingPiece){
        ImageView icon = OccupyingPiece.getIcon();
        this.setTranslateX(column * 60 * scale+220*0.6);
        this.setTranslateY(row * 60 * scale+220*0.6);
        this.getChildren().addAll(icon);
        enableDragging(this);
    }


    public Square(Piece OccupyingPiece, boolean isNotGUI) {
        this.OccupyingPiece = OccupyingPiece;
        this.isOccupied = true;
        this.row = OccupyingPiece.getRow();
        this.column = OccupyingPiece.getColumn();
    }




    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Piece getPiece() {
        return OccupyingPiece;
    }

    public void setOccupyingPiece(Piece occupyingPiece) {
        if(Board.isTest){
            isOccupied = true;
            OccupyingPiece = occupyingPiece;
        }else {
            if (OccupyingPiece != null && OccupyingPiece.getLog() != null)
                board.getLogArea().setText(OccupyingPiece.getLog() + "\n" + board.getLogArea().getText());
            if (occupyingPiece != null)
                this.getChildren().remove(occupyingPiece);
            isOccupied = true;
            OccupyingPiece = occupyingPiece;
            if (!Board.isTest && occupyingPiece != null) {
                setPiece(occupyingPiece);
            }
        }
    }
    private boolean isInDrag = false;
    private void enableDragging(Node node) {

        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        node.setOnMouseDragged( event -> {
            isInDrag =true;
            this.setStyle("-fx-border-width: 0px;-fx-border-color: gray");
            double deltaX = event.getSceneX() - mouseAnchor.get().getX();
            double deltaY = event.getSceneY() - mouseAnchor.get().getY();
            node.relocate(node.getLayoutX()+deltaX, node.getLayoutY()+deltaY);
            mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY()));
        });
        node.setOnMousePressed(event -> {
            mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY()));

        });
        node.setOnMouseReleased(event -> {
            double deltaX = event.getSceneX() - mouseAnchor.get().getX();
            double deltaY = event.getSceneY() - mouseAnchor.get().getY();
            Square destination = getSquare(mouseAnchor.get().getX()-240*0.6,mouseAnchor.get().getY()-240*0.6);

            if(destination == null)
            {
                if(GlobalVariables.PRINTLOG) System.out.println("Outside Bounds destination = " + destination + "\n Resetting piece.");
                node.relocate(deltaX, deltaY);
                return;
            }

            if(destination.getPiece() != null && destination.getPiece().toString().equals("King") && OccupyingPiece.move(destination,board))
            {
                board.finishGame();
            }
            OccupyingPiece.move(destination,board);
            node.relocate(deltaX, deltaY);
            isInDrag =false;
            board.updateGraphics();
        });
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && !isInDrag) {
                this.setStyle("-fx-border-width: 0.6px;-fx-border-color: gray; -fx-stroke-line-cap: butt;");
            } else {
                this.setStyle("-fx-border-width: 0px;-fx-border-color: gray");
            }

        });
    }

    private Square getSquare(double x, double y){
        int column = (int) ((int) x/(60*scale));
        int row = (int) ((int) y/(60*scale));


        if(column >= 8 || row >= 8 || column < 0 || row < 0)
        {
            return null;
        }



        Square[][] squares = Board.getInstance().getSquares();
        return squares[row][column];
    }

    public Square copy(Board board) {
        if (isOccupied) {
            return new Square(OccupyingPiece.copy(),board);
        } else return new Square(false, row, column,scale,board);

    }


    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        if(!occupied)
        {
            this.getChildren().clear();
        }
        isOccupied = occupied;
    }

    public void unOccupy() {
        this.setOccupyingPiece(null);
        this.setOccupied(false);
    }

    public String getNotation(){
        char[] letters = {'a','b','c','d','e','f','g','h'};
        return letters[this.getColumn()]+String.valueOf(this.getRow()+1);
    }

    public void update(){
        if(isOccupied){
            this.getChildren().clear();
            setPiece(getPiece());
        }
    }


    @Override
    public String toString() {
        String s ="";
        if(this.isOccupied){
            if(this.getPiece().getColor()==1){
                s= ",Color=White";
            }else s=",Color=Black";
        }

        return "Square{" +
                ", row=" + row +
                ", column=" + column +
                ", OccupyingPiece=" + OccupyingPiece +s+
                '}';
    }


}
