package ModernATM;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BalanceFX extends VBox {
    public BalanceFX(ATMSimulatorFX app, ATMBackend backend) {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        // Top bar with Cancel
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        cancelBtn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 16; -fx-padding: 8 16; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2);");
        cancelBtn.setOnAction(_ -> app.showScreen(new TransactionSelectionFX(app, backend)));
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setMargin(cancelBtn, new Insets(0, 0, 0, 24));
        topBar.getChildren().add(cancelBtn);
        getChildren().add(topBar);

        // Title
        Label title = new Label("BOM");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 96));
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        // Account number
        Label accLabel = new Label("Primary Checking: " + backend.getAccountNumber());
        accLabel.setFont(Font.font("SansSerif", FontWeight.NORMAL, 32));
        accLabel.setTextFill(Color.WHITE);
        accLabel.setMaxWidth(Double.MAX_VALUE);
        accLabel.setAlignment(Pos.CENTER);
        getChildren().add(accLabel);

        // Balance
        Label balLabel = new Label("₹" + String.format("%.2f", backend.getBalance()));
        balLabel.setFont(Font.font("SansSerif", FontWeight.BOLD, 48));
        balLabel.setTextFill(Color.WHITE);
        balLabel.setMaxWidth(Double.MAX_VALUE);
        balLabel.setAlignment(Pos.CENTER);
        getChildren().add(balLabel);

        VBox.setMargin(topBar, new Insets(0, 40, 0, 40)); // left/right padding
        VBox.setMargin(title, new Insets(0, 40, 0, 40)); // left/right padding
        VBox.setMargin(accLabel, new Insets(0, 40, 0, 40)); // left/right padding
        VBox.setMargin(balLabel, new Insets(0, 40, 40, 40)); // left/right/bottom padding

        // Keyboard navigation for Cancel button
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    cancelBtn.fire();
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