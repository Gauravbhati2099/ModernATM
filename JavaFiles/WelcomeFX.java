package ModernATM;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class WelcomeFX extends VBox {
    public WelcomeFX(ATMSimulatorFX app) {
        setAlignment(Pos.CENTER);
        setSpacing(40);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        Label title = new Label("BOM");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 120));
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        Label subtitle = new Label("Welcome to the Bank of Money ATM");
        subtitle.setFont(Font.font("SansSerif", FontWeight.NORMAL, 36));
        subtitle.setTextFill(Color.WHITE);
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);
        getChildren().add(subtitle);

        Button startBtn = new Button("Insert Card");
        startBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));
        startBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 20 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;");
        startBtn.setMaxWidth(400);
        startBtn.setPrefWidth(400);
        startBtn.setPrefHeight(80);
        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 20 32; -fx-effect: dropshadow(gaussian, #fff, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 20 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
        VBox.setMargin(startBtn, new Insets(0, 40, 40, 40));
        startBtn.setOnAction(_ -> app.showPinEntry());
        getChildren().add(startBtn);

        // Keyboard navigation for Start button
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    startBtn.fire();
                    break;
                case LEFT:
                case RIGHT:
                case UP:
                case DOWN:
                    event.consume();
                    break;
                default:
                    break;
            }
        });
        this.setFocusTraversable(true);
        this.requestFocus();
    }
} 