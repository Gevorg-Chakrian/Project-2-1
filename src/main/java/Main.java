import Chessboard.*;
import Pieces.Piece;
import Utilities.GlobalVariables;
import Utilities.SettingLoader;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {


    final boolean PRINT = false;
    public boolean dontWrite;
    private final boolean ISAION = true; //turns on random ai
    private long starttime;
    private long endtime;
    int gamesPlayed=0;


    public static final ConditionalFeature SCENE3D = ConditionalFeature.SCENE3D;
    @Override
    public void start(Stage primaryStage) throws IOException {
        SettingLoader loader = new SettingLoader();
        int games;
        if(GlobalVariables.isExperiment){
            games=405;
        }else games=0;

        for (int k = 0; k <=games; k++) {
            GlobalVariables.isGameDone=false;
            dontWrite=false;
            gamesPlayed++;
        try {

                Board.isTest = false;
                Game game = new Game();
                game.start_game();


                final int SCALE = 1;
                BackGround bg = new BackGround(SCALE);

                GridPane mainMenuRoot = new GridPane();
                GridPane settingRoot = new GridPane();
                GridPane ckerboardRoot = FXMLLoader.load(getClass().getClassLoader().getResource("base.fxml"));
                GridPane winningRoot = new GridPane();


                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        Board.getInstance().placeboard(i, j);
                    }
                }
                ckerboardRoot.getChildren().addAll(bg, Board.getInstance());
                Scene mainMenuScene = new Scene(mainMenuRoot, 16 * 60 * 0.6, 16 * 60 * 0.6);
                Scene settingScene = new Scene(settingRoot, 16 * 60 * 0.6, 16 * 60 * 0.6);
                Scene checkerScene = new Scene(ckerboardRoot, 16 * 60 * 0.6, 16 * 60 * 0.6);
                Scene winningScene = new Scene(winningRoot, 16 * 60 * 0.6, 16 * 60 * 0.6);


                Rectangle indicatorP1 = new Rectangle();
                indicatorP1.setWidth(1152 * 0.6);
                indicatorP1.setHeight(8);
                indicatorP1.setFill(Color.SALMON);
                indicatorP1.setTranslateY(-160 * 0.6);
                indicatorP1.setStyle(" -fx-opacity: 100%");

                Rectangle indicatorP2 = new Rectangle();
                indicatorP2.setWidth(1152 * 0.6);
                indicatorP2.setHeight(8);
                indicatorP2.setFill(Color.SALMON);
                indicatorP2.setTranslateY(800 * 0.6);
                indicatorP2.setStyle(" -fx-opacity: 0%");

                Button button = new Button();
                button.setMinSize(576 * 0.6, 168 * 0.6);
                button.setMaxSize(576 * 0.6, 168 * 0.6);
                button.setTranslateX(200 * 0.6);
                button.setTranslateY(490 * 0.6);
                button.setStyle("-fx-opacity: 0%");

                Button buttonExit = new Button();
                buttonExit.setMinSize(576 * 0.6, 168 * 0.6);
                buttonExit.setMaxSize(576 * 0.6, 168 * 0.6);
                buttonExit.setTranslateX(200 * 0.6);
                buttonExit.setTranslateY(658 * 0.6);
                buttonExit.setStyle("-fx-opacity: 0%");

                Button buttonExitBoard = new Button();
                buttonExitBoard.setMinSize(30 * 0.6, 30 * 0.6);
                buttonExitBoard.setMaxSize(30 * 0.6, 30 * 0.6);
                buttonExitBoard.setTranslateX(920 * 0.6);
                buttonExitBoard.setTranslateY(-135 * 0.6);
                buttonExitBoard.setStyle("-fx-background-color: transparent; -fx-background-image: url(images/exit.png);-fx-background-size: 18 18;");
                buttonExitBoard.setOnAction(e -> System.exit(0));

                Button quitWinning = new Button();
                quitWinning.setMinSize(576, 576);
                quitWinning.setMaxSize(576, 576);
                quitWinning.setStyle("-fx-opacity: 0%");
                quitWinning.setOnAction(e -> System.exit(0));

                button.setOnAction(e -> primaryStage.setScene(checkerScene));
                buttonExit.setOnAction(e -> System.exit(0));
                //mainMenuRoot.setAlignment(Pos.CENTER);
                mainMenuRoot.setStyle("-fx-background-image: url(images/MAINmenu.png);-fx-background-size: 576 576;");
                settingRoot.setStyle("-fx-background-image: url(images/Setting.png);-fx-background-size: 576 576;");
                ckerboardRoot.setStyle("-fx-background-image: url(images/game.png);-fx-background-size: 576 576;");
                winningRoot.setStyle("-fx-background-image: url(images/win.png);-fx-background-size: 576 576;");
                //promotionRoot.setStyle("-fx-background-image: url(images/MAINmenu.png);-fx-background-size: 576 576;"); //promotion

                TextArea area = new TextArea();
                area.setMinSize(960 / 2 * 0.6, 240 / 2 * 0.6);
                area.setMaxSize(960 / 2 * 0.6, 240 / 2 * 0.6);
                area.setTranslateX(240 * 0.6);
                area.setTranslateY(-40 * 0.6);
                area.setStyle("-fx-control-inner-background: #2A2A2A; -fx-text-fill: #fff; fx-border-width: 0;-fx-focus-color: transparent; -fx-text-box-border: transparent;");
                area.setEditable(false);

                Piece.setTurnLabel(indicatorP1, indicatorP2);
                ImageView dice = DiceFace.getI();
                dice.setFitHeight(60 * 0.6);
                dice.setFitWidth(60 * 0.6);
                dice.setTranslateX(290 * 0.6);
                dice.setTranslateY(680 * 0.6);
                Board.getInstance().setDice(dice);

                Button rollDice = new Button();
                rollDice.setMinSize(960 / 2 * 0.6, 240 / 2 * 0.6);
                rollDice.setMaxSize(960 / 2 * 0.6, 240 / 2 * 0.6);
                rollDice.setTranslateX(240 * 0.6);
                rollDice.setTranslateY(680 * 0.6);
                rollDice.setStyle("-fx-opacity: 0%");
                rollDice.setOnAction(e -> {
                    if (ISAION && Board.getInstance().getTurn() == -1) {
                        if(GlobalVariables.GOALSIDE==-1)
                        {
                            if(GlobalVariables.ISGOALOREXPECTI) {
                                Dice.throwDice();
                                Board.getInstance().aiTurn();
                            }
                            else
                                Board.getInstance().expectiMiniMaxTurn();
                        }
                        else
                            Board.getInstance().mctsTurn();
                    } else if (Board.getInstance().getTurn() == 1) {
                        if(GlobalVariables.GOALSIDE==1)
                        {
                            if(GlobalVariables.ISGOALOREXPECTI) {
                                Dice.throwDice();
                                Board.getInstance().aiTurn();
                            }
                            else
                                Board.getInstance().expectiMiniMaxTurn();
                        }
                        else
                            Board.getInstance().mctsTurn();
                    }
                   if(!Dice.wasThrown){
                        if (GlobalVariables.PRINTLOG) System.out.println("Now rolling dice");
                        Dice.throwDice();
                    }
                    dice.setImage(DiceFace.getI().getImage());
                });

                checkerScene.setOnKeyPressed(event -> {
                    if(event.getCode() == KeyCode.SPACE)
                    {
                        System.out.println("PRESSED" +
                                "");
                        rollDice.fire();
                    }
                });

                Button settingButtons = new Button();
                ImageView settingsImage = new ImageView("images/settingicon.png");
                settingsImage.setFitHeight(50);
                settingsImage.setPreserveRatio(true);
                settingButtons.setGraphic(settingsImage);
                settingButtons.setStyle("-fx-background-color: #2A2A2A");
                settingButtons.setPrefSize(50,50);
                settingButtons.setTranslateY(-20);
                settingButtons.setTranslateX(5);
                mainMenuRoot.getChildren().add(settingButtons);
                settingButtons.setOnAction(e -> primaryStage.setScene(settingScene));

                //*SETTINGS*
                settingScene.getStylesheets().add(getClass().getResource("settingstyle.css").toExternalForm());
                GridPane leftP = new GridPane();
                GridPane mid = new GridPane();
                GridPane rightP = new GridPane();

                settingRoot.addColumn(0,leftP);
                settingRoot.addColumn(1,mid);
                settingRoot.addColumn(2,rightP);

                leftP.setPrefWidth(250);
                mid.setPrefWidth(100);

                leftP.addRow(0,new Label("Display Log (t/f):"));
                leftP.addRow(1,new Label("Ignore Dice (t/f):"));
                leftP.addRow(2,new Label("MTCS exec time(ms):"));
                leftP.addRow(3,new Label("Play Game (times):"));
                leftP.addRow(4,new Label("Experiment (t/f):"));
                leftP.addRow(5,new Label("ExpectiMiniMax Depth (int):"));
                leftP.addRow(6,new Label("Side:"));
                leftP.addRow(7,new Label("Algorithm:"));

                String style = "-fx-padding: 18;";
                String buttonStyle = "-fx-border-color: #adadad; -fx-border-width: 5px;-fx-background-color: #2A2A2A; -fx-text-fill: white";

                String pressedButtonStyle = "-fx-border-color: red; -fx-border-width: 5px;-fx-background-color: #2A2A2A; -fx-text-fill: white";

                AtomicReference<ArrayList<String>> settings = new AtomicReference<>(loader.getSetting());

                GridPane sliderA = new GridPane();
            GridPane sliderB = new GridPane();
            GridPane sliderC = new GridPane();

                ToggleButton log = new ToggleButton("Log");
                log.setStyle(style);
                log.setStyle(buttonStyle);
                log.setPrefWidth(200);
                log.setPrefHeight(50);

                if(Boolean.parseBoolean(settings.get().get(0))) {
                    log.setSelected(true);
                    log.setStyle(pressedButtonStyle);
                }

                log.setOnAction(e -> {
                    if(log.isSelected())
                        log.setStyle(pressedButtonStyle);
                    else
                        log.setStyle(buttonStyle);
                    if(log.isSelected())
                        settings.get().set(0,"true");
                    else
                        settings.get().set(0,"false");
                });


                Label sliderATxt = new Label();
                Slider mctsTime = new Slider();
                mctsTime.setMax(15000);
                mctsTime.setMin(2000);
                mctsTime.setValue(Double.parseDouble(settings.get().get(2)));
                sliderATxt.setText(String.valueOf(String.format("%.0f",mctsTime.getValue())));
                mctsTime.setStyle(style);
                settings.get().set(2,sliderATxt.getText());

                mctsTime.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        sliderATxt.setText(String.valueOf(String.format("%.0f",mctsTime.getValue())));
                        settings.get().set(2,String.valueOf(String.format("%.0f",mctsTime.getValue())));
                    }
                });

                Label sliderBTxt = new Label();
                Slider gamesSlid = new Slider();
                gamesSlid.setMax(100);
                gamesSlid.setMin(1);
                gamesSlid.setValue(Double.parseDouble(settings.get().get(3)));
            sliderBTxt.setText(String.valueOf(String.format("%.0f",gamesSlid.getValue())));
            gamesSlid.setStyle(style);
            settings.get().set(3,sliderBTxt.getText());

            gamesSlid.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    sliderBTxt.setText(String.valueOf(String.format("%.0f",gamesSlid.getValue())));
                    settings.get().set(3,sliderBTxt.getText());
                }
            });

                ToggleButton igndice = new ToggleButton("Ignore Dice");
            igndice.setStyle(style);
            igndice.setStyle(buttonStyle);
            igndice.setPrefWidth(200);
            igndice.setPrefHeight(50);
            if(Boolean.parseBoolean(settings.get().get(1))) {
                igndice.setStyle(pressedButtonStyle);
                igndice.setSelected(true);
            }
            igndice.setOnAction(e -> {
                if(igndice.isSelected())
                    igndice.setStyle(pressedButtonStyle);
                else
                    igndice.setStyle(buttonStyle);
                if(igndice.isSelected())
                    settings.get().set(1,"true");
                else
                    settings.get().set(1,"false");
            });

                ToggleButton expp = new ToggleButton("Experiment");
            expp.setStyle(style);
            expp.setStyle(buttonStyle);
            expp.setPrefWidth(200);
            expp.setPrefHeight(50);
            if(Boolean.parseBoolean(settings.get().get(4))) {
                expp.setStyle(pressedButtonStyle);
                expp.setSelected(true);
            }
            expp.setOnAction(e -> {
                if(expp.isSelected())
                    expp.setStyle(pressedButtonStyle);
                else
                    expp.setStyle(buttonStyle);
                if(expp.isSelected())
                    settings.get().set(4,"true");
                else
                    settings.get().set(4,"false");
            });

            Label sliderCTxt = new Label();
                Slider depthS = new Slider();
                depthS.setMax(20);
                depthS.setMin(5);
                depthS.setValue(Double.parseDouble(settings.get().get(5)));
            depthS.setStyle(style);
            sliderCTxt.setText(String.valueOf(String.format("%.0f",depthS.getValue())));
            depthS.setStyle(style);
            settings.get().set(5,sliderCTxt.getText());

            depthS.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    sliderCTxt.setText(String.valueOf(String.format("%.0f",depthS.getValue())));
                    settings.get().set(5,sliderCTxt.getText());
                }
            });

                ToggleButton sideB = new ToggleButton("MCTS white");
            sideB.setStyle(style);
            sideB.setStyle(buttonStyle);
            sideB.setPrefWidth(200);
            sideB.setPrefHeight(50);
            if(Integer.parseInt(settings.get().get(6))==-1)
            {
                sideB.setStyle(pressedButtonStyle);
                sideB.setSelected(true);
            }
            sideB.setOnAction(e -> {
                if(sideB.isSelected())
                    sideB.setStyle(pressedButtonStyle);
                else
                    sideB.setStyle(buttonStyle);
                if(sideB.isSelected())
                    settings.get().set(6,"-1");
                else
                    settings.get().set(6,"1");
            });

                ToggleButton goal = new ToggleButton("Goal Agent");
            goal.setStyle(style);
            goal.setStyle(buttonStyle);
            goal.setPrefWidth(200);
            goal.setPrefHeight(50);
            if(Boolean.parseBoolean(settings.get().get(7))) {
                goal.setStyle(pressedButtonStyle);
                goal.setSelected(true);
            }
            goal.setOnAction(e -> {
                if(goal.isSelected())
                    goal.setStyle(pressedButtonStyle);
                else
                    goal.setStyle(buttonStyle);
                if(goal.isSelected())
                    settings.get().set(7,"true");
                else
                    settings.get().set(7,"false");
            });


            Button useless = new Button("Done");
            useless.setStyle("-fx-border-color: #2A2A2A; -fx-border-width: 5px;-fx-background-color: #2A2A2A; -fx-text-fill: #2A2A2A; -fx-padding: 18");
            useless.setPrefWidth(200);
            useless.setPrefHeight(50);
            Button uselessDue = new Button("Done");
            uselessDue.setStyle("-fx-border-color: #2A2A2A; -fx-border-width: 5px;-fx-background-color: #2A2A2A; -fx-text-fill: #2A2A2A; -fx-padding: 18");
            uselessDue.setPrefWidth(200);
            uselessDue.setPrefHeight(50);

            Button ret = new Button("Done");
            ret.setStyle(style);
            ret.setStyle(buttonStyle);
            ret.setPrefWidth(200);
            ret.setPrefHeight(50);
            ret.setOnAction(e -> {
                loader.editSetting(settings.get());
                primaryStage.setScene(mainMenuScene);
            });

            Button resetButtont = new Button("Reset");
            resetButtont.setStyle(style);
            resetButtont.setStyle(buttonStyle);
            resetButtont.setPrefWidth(200);
            resetButtont.setPrefHeight(50);
            resetButtont.setOnAction(e -> {

                System.out.println("Reseting");
                loader.resetSettings();

                settings.set(loader.getSetting());

                log.setSelected(false);
                log.setStyle(buttonStyle);

                igndice.setSelected(false);
                igndice.setStyle(buttonStyle);

                mctsTime.setValue(5000);

                gamesSlid.setValue(1);

                expp.setSelected(false);
                expp.setStyle(buttonStyle);

                depthS.setValue(5);

                sideB.setSelected(true);
                sideB.setStyle(pressedButtonStyle);

                goal.setSelected(false);
                goal.setStyle(buttonStyle);

            });

                rightP.addRow(0,log);
                rightP.addRow(1,igndice);
                rightP.addRow(2,sliderA);
                rightP.addRow(3,sliderB);
                rightP.addRow(4,expp);
                rightP.addRow(5,sliderC);
                rightP.addRow(6,sideB);
                rightP.addRow(7,goal);
                rightP.addRow(8,useless);
                rightP.addRow(9,uselessDue);
                rightP.addRow(10,ret);

                leftP.addRow(8,useless);
                leftP.addRow(9,resetButtont);

                sliderA.addColumn(0,mctsTime);
                sliderA.addColumn(1,sliderATxt);

                sliderB.addColumn(0,gamesSlid);
            sliderB.addColumn(1,sliderBTxt);

                sliderC.addColumn(0,depthS);
            sliderC.addColumn(1,sliderCTxt);


                Board.getInstance().setLogArea(area);
                Board.getInstance().setWin(primaryStage, winningScene);

                mainMenuRoot.getChildren().addAll(button, buttonExit);
                ckerboardRoot.getChildren().addAll(area, buttonExitBoard, dice, rollDice, indicatorP1, indicatorP2);
                winningRoot.getChildren().add(quitWinning);

                if(GlobalVariables.FIRSTGAME) {
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                }
                primaryStage.setScene(mainMenuScene);
                primaryStage.show();

             /*   while(GlobalVariables.WHITEWON==0 || GlobalVariables.BLACKWON==0) {
                    System.out.println("FIRING BUTTONS");
                    button.fire();
                    Board.getInstance().mctsTurn();
                    System.out.println("MCTS MOVE MADE");
                    starttime = System.nanoTime();
                    rollDice.fire();
                    Board.getInstance().aiTurn();
                    System.out.println("AGENT MOVE MADE");
                    rollDice.fire();
                }
              */
            System.out.println("STARTED NEW GAME!");
            if(GlobalVariables.isExperiment && !GlobalVariables.isGameDone) {
                button.fire();
                starttime = System.nanoTime();
                rollDice.fire();
                if(GlobalVariables.isGameDone && GlobalVariables.BLACKWON==0 && GlobalVariables.WHITEWON==0){
                    dontWrite=true;
                }

                System.out.println("\n\n");
                System.out.println(" FINISHED GAME");
                System.out.println("BLACK WON = " + GlobalVariables.BLACKWON);
                System.out.println("WHITE WON = " + GlobalVariables.WHITEWON);
            }

        } catch(Exception e) {
           // dontWrite=true;
            endtime = System.nanoTime();
            e.printStackTrace();
            System.out.println("ERROR CAUGHT " + k);
        }


          //  if (!dontWrite) {  //  System.out.println("Turn " + k + "/100 lasted: " + (endtime-starttime) + "ns for " + Board.getInstance().getTurnCount() + " turns");
            try {
                System.out.println("WRITING ONCE");
                    GlobalVariables.FIRSTGAME = false;
                    System.out.println("\n\n");

                    String rowWrite = GlobalVariables.EXPLORAITONCOEFFICIENT + "," + GlobalVariables.WINSCORE + ", " + GlobalVariables.MCTSTIME + ", " + GlobalVariables.EXPECTIDEPTH + ", " + GlobalVariables.BLACKWON + ", " + GlobalVariables.WHITEWON + " ,"+GlobalVariables.stupid_string +"\n";
                    String s;
                    if (GlobalVariables.withBias) {
                        s = "WithBias";
                    } else s = "WithoutBias";

                    String path = "src\\main\\resources\\GameData\\MCTSvsEXPECTI\\HybridvsExpecti";//ExplorationCoefficient"+GlobalVariables.WINSCORE;
                    File file = new File(path);

                    FileWriter writer = new FileWriter(file, true);

                    // Writes the content to the file
                   // GlobalVariables.WHITEWON = 0;
                   // GlobalVariables.BLACKWON = 0;

                    writer.write(rowWrite);
                    writer.flush();
                    writer.close();
                    Board.getInstance().resetTurnCount();
                    if (gamesPlayed == GlobalVariables.PLAYGAMES) {
                        break;
                    }

                    if(GlobalVariables.PLAYGAMES==50){
                        GlobalVariables.GOALSIDE=GlobalVariables.GOALSIDE*-1;
                    }


                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            GlobalVariables.FIRSTGAME = false;
            GlobalVariables.WHITEWON = 0;
            GlobalVariables.BLACKWON = 0;


        }
     //  }

    public static void main(String[] args) {
        launch(args);
    }
}