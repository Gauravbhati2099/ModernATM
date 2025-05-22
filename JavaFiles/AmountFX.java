package ModernATM;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AmountFX extends VBox {
    public AmountFX(ATMSimulatorFX app, ATMBackend backend, String type) {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        // Top bar with Cancel
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button cancelBtn = new Button("Cancel");
        ButtonStyles.styleCancelButton(cancelBtn);
        cancelBtn.setOnAction(_ -> app.showScreen(new TransactionSelectionFX(app, backend)));
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

        // Prompt
        Label prompt = new Label("Enter amount to " + type);
        prompt.setFont(Font.font("SansSerif", FontWeight.NORMAL, 32));
        prompt.setTextFill(Color.WHITE);
        prompt.setMaxWidth(Double.MAX_VALUE);
        prompt.setAlignment(Pos.CENTER);
        getChildren().add(prompt);

        // Preset amount buttons (responsive)
        VBox buttonBox = new VBox(24);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(true);
        int[] amounts = {500, 1000, 2000, 5000};
        for (int amt : amounts) {
            Button btn = createAmountButton("  ₹" + amt);
            ButtonStyles.styleMainButton(btn);
            btn.setOnAction(_ -> handleAmount(app, backend, type, amt));
            VBox.setMargin(btn, new Insets(0, 40, 0, 40));
            buttonBox.getChildren().add(btn);
        }
        buttonBox.maxWidthProperty().bind(Bindings.min(widthProperty().subtract(40), 400));
        getChildren().add(buttonBox);

        // Custom amount (keyboard entry, responsive)
        Label customLabel = new Label("Or enter a custom amount:");
        customLabel.setFont(Font.font("SansSerif", FontWeight.NORMAL, 24));
        customLabel.setTextFill(Color.WHITE);
        customLabel.setMaxWidth(Double.MAX_VALUE);
        customLabel.setAlignment(Pos.CENTER);
        VBox.setMargin(customLabel, new Insets(20, 40, 0, 40));
        getChildren().add(customLabel);

        HBox customRow = new HBox(24); // spacing between field and button
        customRow.setAlignment(Pos.CENTER);
        customRow.setMaxWidth(560);
        customRow.setPrefWidth(560);
        customRow.setMinWidth(560);
        Label otherLabel = new Label("₹");
        otherLabel.setFont(Font.font("SansSerif", FontWeight.NORMAL, 28));
        TextField customField = new TextField();
        customField.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        customField.setPrefWidth(400);
        customField.setMinWidth(400);
        customField.setMaxWidth(400);
        customField.setPrefHeight(60);
        customField.setMinHeight(60);
        customField.setMaxHeight(60);
        customField.setPromptText("Enter amount");
        customField.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-text-fill: #222; -fx-border-color: #6336ff; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 12 24 12 36;");
        Button customBtn = createAmountButton("OK");
        ButtonStyles.styleMainButton(customBtn);
        customBtn.setMinWidth(120);
        customBtn.setPrefWidth(120);
        customBtn.setMaxWidth(120);
        customBtn.setOnAction(_ -> {
            try {
                double val = Double.parseDouble(customField.getText());
                if (val > 0) {
                    handleAmount(app, backend, type, val);
                } else {
                    app.showError("Enter a positive amount.");
                }
            } catch (Exception ex) {
                app.showError("Invalid amount.");
            }
        });
        // Allow Enter key to submit custom amount
        customField.setOnAction(customBtn.getOnAction());
        customRow.getChildren().addAll(otherLabel, customField, customBtn);
        VBox.setMargin(customRow, new Insets(0, 40, 40, 40));
        getChildren().add(customRow);

        // Keyboard navigation for custom amount field
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    int leftPos = customField.getCaretPosition();
                    if (leftPos > 0) customField.positionCaret(leftPos - 1);
                    break;
                case RIGHT:
                    int rightPos = customField.getCaretPosition();
                    if (rightPos < customField.getText().length()) customField.positionCaret(rightPos + 1);
                    break;
                case UP:
                case DOWN:
                    event.consume();
                    break;
                case ENTER:
                    if (customField.isFocused()) customBtn.fire();
                    break;
                default:
                    break;
            }
        });
        this.setFocusTraversable(true);
        this.requestFocus();
    }

    private Button createAmountButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        return btn;
    }

    private void handleAmount(ATMSimulatorFX app, ATMBackend backend, String type, double amount) {
        if ("withdraw".equalsIgnoreCase(type)) {
            ATMBackend.TransactionResult result = backend.tryWithdraw(amount);
            if (result == ATMBackend.TransactionResult.SUCCESS) {
                app.showSuccess("Withdrawal successful!");
                app.showTransactionSelection();
            } else if (result == ATMBackend.TransactionResult.INSUFFICIENT_FUNDS) {
                app.showError("Insufficient funds.");
            } else if (result == ATMBackend.TransactionResult.EXCEEDS_LIMIT) {
                app.showError("Amount exceeds transaction limit.");
            } else {
                app.showError("Invalid amount.");
            }
        } else if ("deposit".equalsIgnoreCase(type)) {
            ATMBackend.TransactionResult result = backend.tryDeposit(amount);
            if (result == ATMBackend.TransactionResult.SUCCESS) {
                app.showSuccess("Deposit successful!");
                app.showTransactionSelection();
            } else if (result == ATMBackend.TransactionResult.EXCEEDS_LIMIT) {
                app.showError("Amount exceeds transaction limit.");
            } else {
                app.showError("Invalid amount.");
            }
        }
    }
} 