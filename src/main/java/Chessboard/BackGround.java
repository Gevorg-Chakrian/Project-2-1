package Chessboard;

import javafx.scene.DepthTest;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BackGround extends Pane {
    public BackGround(int scale){
        this.setStyle("-fx-background-color: transparent");
        this.setDepthTest(DepthTest.ENABLE);
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                Rectangle rec = new Rectangle();
                rec.setWidth(60 * scale*0.6);
                rec.setHeight(60 * scale*0.6);
                rec.setStroke(Color.TRANSPARENT);
                rec.setStrokeType(StrokeType.INSIDE);
                rec.setStrokeWidth(1);
                rec.setTranslateX(k * 60 * scale*0.6+240*scale*0.6);
                rec.setTranslateY(i * 60 * scale*0.6+240*scale*0.6);
                if((k%2==0 && i%2==1) || (k%2==1 && i%2==0)){
                    rec.setFill(Color.WHITE);
                }
                else if((k%2==0 && i%2==0) || (k%2==1 && i%2==1)){
                    rec.setFill(Color.DARKGRAY);
                }
                this.getChildren().add(rec);
            }
        }
    }
}
