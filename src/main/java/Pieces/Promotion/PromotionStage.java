package Pieces.Promotion;

import Chessboard.Board;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PromotionStage extends Stage {


    VBox promotionRoot = new PromotionGUI(Board.turn); //promotion
    Scene promotionScene = new Scene(promotionRoot); //promotion

    public PromotionStage(){
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UNDECORATED);
        this.setScene(promotionScene);
        //this.setScene(new Scene(new PromotionGUI(Board.turn)));
        this.showAndWait();

    }
}
