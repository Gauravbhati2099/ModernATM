package ModernATM;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PinEntryFX extends VBox {
    private StringBuilder pin = new StringBuilder();
    private Label pinDisplay;
    private Label errorLabel;
    private String lastEnteredPin = "";

    public PinEntryFX(ATMSimulatorFX app, ATMBackend backend, String nextAction) {
        setSpacing(16);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        // Top bar with Cancel
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button cancelBtn = new Button("Cancel");
        ButtonStyles.styleCancelButton(cancelBtn);
        cancelBtn.setOnAction(_ -> app.showScreen(new TransactionSelectionFX(app, backend)));
        HBox.setMargin(cancelBtn, new Insets(0, 0, 0, 16));
        topBar.getChildren().add(cancelBtn);
        getChildren().add(topBar);

        // --- BEGIN NEW LAYOUT ---
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setFillWidth(true);

        // Title
        Label title = new Label("BOM");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 80)); // bigger
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(24, 0, 24, 0)); // top and bottom padding
        contentBox.getChildren().add(title);

        // Prompt
        Label prompt = new Label("Enter your PIN number");
        prompt.setFont(Font.font("SansSerif", FontWeight.NORMAL, 36));
        prompt.setTextFill(Color.WHITE);
        prompt.setMaxWidth(Double.MAX_VALUE);
        prompt.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(prompt);

        // PIN input (PasswordField for keyboard entry)
        HBox pinBox = new HBox(0);
        pinBox.setAlignment(Pos.CENTER);
        pinBox.setMaxWidth(260);
        pinBox.setPrefWidth(260);
        pinBox.setMinWidth(260);
        PasswordField pinField = new PasswordField();
        pinField.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        pinField.setMaxWidth(260);
        pinField.setAlignment(Pos.CENTER);
        pinField.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 8 12 0 12; -fx-effect: dropshadow(gaussian, #fff, 2, 0, 0, 1);");
        pinField.setFocusTraversable(true);
        pinField.setOnMouseEntered(e -> pinField.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-text-fill: white; -fx-border-color: #ffb74d; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 8 12; -fx-effect: dropshadow(gaussian, #fff, 2, 0, 0, 1);"));
        pinField.setOnMouseExited(e -> pinField.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 8 12; -fx-effect: dropshadow(gaussian, #fff, 2, 0, 0, 1);"));
        // Add X button (styled like numpad/clear buttons, acts as backspace)
        Button okBtn = new Button("X");
        okBtn.setFont(Font.font("SansSerif", FontWeight.EXTRA_BOLD, 36));
        okBtn.setStyle("-fx-background-color: white; -fx-text-fill: #3a1fa4; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;");
        okBtn.setPrefHeight(48);
        okBtn.setPrefWidth(64);
        okBtn.setMinHeight(48);
        okBtn.setMinWidth(64);
        okBtn.setMaxHeight(48);
        okBtn.setMaxWidth(64);
        okBtn.setFocusTraversable(false);
        okBtn.setOnMouseEntered(e -> okBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #fff, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
        okBtn.setOnMouseExited(e -> okBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
        okBtn.setOnMousePressed(e -> { okBtn.setScaleX(0.92); okBtn.setScaleY(0.92); });
        okBtn.setOnMouseReleased(e -> { okBtn.setScaleX(1.0); okBtn.setScaleY(1.0); });
        okBtn.setOnAction(e -> {
            String text = pinField.getText();
            if (!text.isEmpty()) {
                pinField.setText(text.substring(0, text.length() - 1));
            }
        });
        pinBox.getChildren().addAll(pinField, okBtn);
        HBox.setMargin(okBtn, new Insets(0, 0, 0, 6));
        VBox.setMargin(pinBox, new Insets(0, 0, 2, 0));
        contentBox.getChildren().add(pinBox);

        // PIN display (for keypad entry)
        pinDisplay = new Label("");
        pinDisplay.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        pinDisplay.setTextFill(Color.WHITE);
        pinDisplay.setVisible(false);
        contentBox.getChildren().add(pinDisplay);

        // Error label
        errorLabel = new Label("");
        errorLabel.setFont(Font.font("SansSerif", FontWeight.NORMAL, 14));
        errorLabel.setTextFill(Color.PINK);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(errorLabel);

        // Number pad (responsive)
        GridPane pad = new GridPane();
        pad.setAlignment(Pos.CENTER);
        pad.setHgap(12);
        pad.setVgap(12);
        pad.setMaxWidth(340);
        VBox.setMargin(pad, new Insets(0, 0, 0, 0));
        String[] keys = {"1","2","3","4","5","6","7","8","9","","0",""};
        int idx = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                String key = keys[idx++];
                if (key.isEmpty()) continue;
                Button btn = new Button(key);
                btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));
                btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;");
                btn.setPrefHeight(80);
                btn.setPrefWidth(80);
                btn.setMinHeight(80);
                btn.setMinWidth(80);
                btn.setMaxHeight(80);
                btn.setMaxWidth(80);
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #fff, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
                btn.setOnMousePressed(e -> { btn.setScaleX(0.92); btn.setScaleY(0.92); });
                btn.setOnMouseReleased(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
                btn.setOnAction(_ -> {
                    if (pinField.getText().length() < 4) {
                        pinField.setText(pinField.getText() + key);
                        errorLabel.setText("");
                    }
                });
                pad.add(btn, col, row);
                GridPane.setFillWidth(btn, true);
            }
        }
        pad.maxWidthProperty().bind(Bindings.min(widthProperty().subtract(40), 340));
        VBox.setVgrow(pad, javafx.scene.layout.Priority.ALWAYS);
        contentBox.getChildren().add(pad);

        // Confirm button (responsive)
        Button submit = new Button("Confirm");
        ButtonStyles.styleMainButton(submit);
        submit.setMaxWidth(260);
        submit.setPrefWidth(260);
        VBox.setMargin(submit, new Insets(8, 0, 8, 0));
        VBox.setVgrow(submit, javafx.scene.layout.Priority.NEVER);
        contentBox.getChildren().add(submit);

        scrollPane.setContent(contentBox);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        getChildren().clear();
        getChildren().addAll(topBar, scrollPane);

        submit.setOnAction(_ -> {
            String enteredPin = pinField.getText();
            if (enteredPin.length() != 4) {
                errorLabel.setText("PIN must be 4 digits");
                return;
            }
            if (backend.validatePin(enteredPin)) {
                if (nextAction != null) {
                    app.showAmount(nextAction);
                } else {
                    app.showTransactionSelection();
                }
            } else {
                errorLabel.setText("Incorrect PIN");
                pinField.clear();
            }
        });

        // Allow Enter key to submit PIN when pinField is focused
        pinField.setOnAction(submit.getOnAction());

        // Bind Enter key and number keys globally for input and confirmation
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    submit.fire();
                    break;
                case DIGIT0: case NUMPAD0:
                case DIGIT1: case NUMPAD1:
                case DIGIT2: case NUMPAD2:
                case DIGIT3: case NUMPAD3:
                case DIGIT4: case NUMPAD4:
                case DIGIT5: case NUMPAD5:
                case DIGIT6: case NUMPAD6:
                case DIGIT7: case NUMPAD7:
                case DIGIT8: case NUMPAD8:
                case DIGIT9: case NUMPAD9:
                    if (pinField.getText().length() < 4) {
                        String digit = event.getText();
                        if (digit.matches("\\d")) {
                            pinField.setText(pinField.getText() + digit);
                            errorLabel.setText("");
                        }
                    }
                    break;
                case LEFT:
                    // Move caret left
                    int leftPos = pinField.getCaretPosition();
                    if (leftPos > 0) pinField.positionCaret(leftPos - 1);
                    break;
                case RIGHT:
                    // Move caret right
                    int rightPos = pinField.getCaretPosition();
                    if (rightPos < pinField.getText().length()) pinField.positionCaret(rightPos + 1);
                    break;
                case UP:
                case DOWN:
                    // Prevent default scrolling
                    event.consume();
                    break;
                default:
                    break;
            }
        });
        // Request focus so key events are captured
        this.setFocusTraversable(true);
        this.requestFocus();
    }
} 