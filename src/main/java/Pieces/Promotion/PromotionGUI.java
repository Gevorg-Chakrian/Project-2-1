package Pieces.Promotion;

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PromotionGUI extends VBox {

    public static int promotionChoice;
    public PromotionGUI(int turn){
        setStyle("-fx-background-color: #2A2A2A");
        setPadding(new Insets(20, 20, 20, 20));
        HBox piecesContainer = new HBox();
        //Label promotionLabel = new Label("Choose piece to promote to:");

        VBox knightBox = new VBox();
        VBox bishopBox= new VBox();
        VBox rookBox= new VBox();
        VBox queenBox= new VBox();

        //turn should be opposite 1 is white but the move is already updated after the move
        ImageView knightView = new ImageView((turn == 1) ? "/pieces_images/knight.png" : "/pieces_images/knight_white.png");
        ImageView bishopView = new ImageView((turn == 1) ?"/pieces_images/bishop.png":"/pieces_images/bishop_white.png");
        ImageView rookView = new ImageView((turn == 1) ?"/pieces_images/rook.png":"/pieces_images/rook_white.png");
        ImageView queenView = new ImageView((turn == 1) ?"/pieces_images/queen.png":"/pieces_images/queen_white.png");
        ImageView promotionLabel = new ImageView("/images/promoting.png");

        knightBox.getChildren().add(knightView);
        bishopBox.getChildren().add(bishopView);
        rookBox.getChildren().add(rookView);
        queenBox.getChildren().add(queenView);

        piecesContainer.getChildren().add(knightBox);
        piecesContainer.getChildren().add(bishopBox);
        piecesContainer.getChildren().add(rookBox);
        piecesContainer.getChildren().add(queenBox);

        setPromotionChoice(knightBox, 2);
        setPromotionChoice(bishopBox, 3);
        setPromotionChoice(rookBox, 4);
        setPromotionChoice(queenBox, 5);

        getChildren().add(promotionLabel);
        getChildren().add(piecesContainer);

        //PromotionGUI.promotionChoice for logic promotion or method getPromotionChoice
    }
    public void setPromotionChoice(VBox image, int piece){
        //image.setStyle("-fx-border-width: 1px;-fx-border-color: rgba(255, 255, 255, 0);");
        image.setOnMouseClicked(event -> {
            promotionChoice=piece;
            ((Stage) getScene().getWindow()).close();
        });
        image.setOnMouseEntered(event -> image.setStyle("-fx-border-width: 1px;-fx-border-color: gray; -fx-stroke-line-cap: butt;"));
        image.setOnMouseExited(event -> {
            //image.setStyle("-fx-border-width: 1px;-fx-border-color: rgba(255, 255, 255, 0);");
            image.setStyle("-fx-border-width: 0px;-fx-border-color: gray;");
        });
        //image.setCursor(Cursor.HAND);
    }

}
