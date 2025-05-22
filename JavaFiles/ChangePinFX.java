package ModernATM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChangePinFX extends VBox {
    public ChangePinFX(ATMSimulatorFX app, ATMBackend backend) {
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
        cancelBtn.setOnAction(_ -> app.showTransactionSelection());
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setMargin(cancelBtn, new Insets(0, 0, 0, 24));
        topBar.getChildren().add(cancelBtn);
        getChildren().add(topBar);

        // Title
        Label title = new Label("Change PIN");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 64));
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        // Error/success label
        Label feedback = new Label("");
        feedback.setFont(Font.font("SansSerif", FontWeight.NORMAL, 22));
        feedback.setTextFill(Color.PINK);
        feedback.setMaxWidth(Double.MAX_VALUE);
        feedback.setAlignment(Pos.CENTER);
        getChildren().add(feedback);

        // PIN fields
        VBox fieldsBox = new VBox(18);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setFillWidth(true);
        fieldsBox.setMaxWidth(400);

        PasswordField currentPin = createPinField();
        PasswordField newPin = createPinField();
        PasswordField confirmPin = createPinField();

        fieldsBox.getChildren().addAll(
            labeledField("Current PIN", currentPin),
            labeledField("New PIN", newPin),
            labeledField("Confirm New PIN", confirmPin)
        );
        getChildren().add(fieldsBox);

        // Change PIN button
        Button changeBtn = new Button("Change PIN");
        changeBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        changeBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;");
        changeBtn.setMinWidth(400);
        changeBtn.setPrefWidth(400);
        changeBtn.setMaxWidth(400);
        changeBtn.setPrefHeight(60);
        changeBtn.setOnMouseEntered(e -> changeBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #fff, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
        changeBtn.setOnMouseExited(e -> changeBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
        changeBtn.setOnMousePressed(e -> { changeBtn.setScaleX(0.96); changeBtn.setScaleY(0.96); });
        changeBtn.setOnMouseReleased(e -> { changeBtn.setScaleX(1.0); changeBtn.setScaleY(1.0); });
        VBox.setMargin(changeBtn, new Insets(0, 40, 40, 40));
        getChildren().add(changeBtn);

        changeBtn.setOnAction(_ -> {
            String oldPin = currentPin.getText();
            String np = newPin.getText();
            String cp = confirmPin.getText();
            if (oldPin.length() != 4 || np.length() != 4 || cp.length() != 4) {
                feedback.setText("All PINs must be 4 digits.");
                feedback.setTextFill(Color.PINK);
                return;
            }
            if (!np.equals(cp)) {
                feedback.setText("New PINs do not match.");
                feedback.setTextFill(Color.PINK);
                return;
            }
            if (oldPin.equals(np)) {
                feedback.setText("New PIN must be different from current PIN.");
                feedback.setTextFill(Color.PINK);
                return;
            }
            if (backend.changePin(oldPin, np)) {
                feedback.setText("PIN changed successfully!");
                feedback.setTextFill(Color.LIGHTGREEN);
                currentPin.clear(); newPin.clear(); confirmPin.clear();
            } else {
                feedback.setText("Incorrect current PIN.");
                feedback.setTextFill(Color.PINK);
            }
        });

        // Keyboard navigation for all PIN fields
        this.setOnKeyPressed(event -> {
            PasswordField[] fields = {currentPin, newPin, confirmPin};
            for (PasswordField pf : fields) {
                switch (event.getCode()) {
                    case LEFT:
                        int leftPos = pf.getCaretPosition();
                        if (leftPos > 0 && pf.isFocused()) pf.positionCaret(leftPos - 1);
                        break;
                    case RIGHT:
                        int rightPos = pf.getCaretPosition();
                        if (rightPos < pf.getText().length() && pf.isFocused()) pf.positionCaret(rightPos + 1);
                        break;
                    case UP:
                    case DOWN:
                        if (pf.isFocused()) event.consume();
                        break;
                    case ENTER:
                        if (pf.isFocused()) changeBtn.fire();
                        break;
                    default:
                        break;
                }
            }
        });
        this.setFocusTraversable(true);
        this.requestFocus();
    }

    private HBox labeledField(String label, PasswordField field) {
        Label l = new Label(label);
        l.setFont(Font.font("SansSerif", FontWeight.NORMAL, 22));
        l.setTextFill(Color.WHITE);
        l.setMinWidth(170);
        l.setAlignment(Pos.CENTER_LEFT);
        l.setMaxWidth(170);
        l.setPrefWidth(170);
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(l, field, createClearBtn(field));
        return box;
    }

    private PasswordField createPinField() {
        PasswordField pf = new PasswordField();
        pf.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        pf.setMaxWidth(120);
        pf.setPrefWidth(120);
        pf.setMinWidth(120);
        pf.setPrefHeight(48);
        pf.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 8 16;");
        pf.setTextFormatter(new javafx.scene.control.TextFormatter<String>(change -> {
            if (change.getControlNewText().length() > 4) return null;
            if (!change.getControlNewText().matches("\\d*")) return null;
            return change;
        }));
        return pf;
    }

    private Button createClearBtn(PasswordField pf) {
        Button clearBtn = new Button("✕");
        clearBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));
        clearBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;");
        clearBtn.setPrefHeight(48);
        clearBtn.setPrefWidth(48);
        clearBtn.setMinHeight(48);
        clearBtn.setMinWidth(48);
        clearBtn.setMaxHeight(48);
        clearBtn.setMaxWidth(48);
        clearBtn.setFocusTraversable(false);
        clearBtn.setOnMouseEntered(e -> clearBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #fff, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
        clearBtn.setOnMouseExited(e -> clearBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
        clearBtn.setOnMousePressed(e -> { clearBtn.setScaleX(0.92); clearBtn.setScaleY(0.92); });
        clearBtn.setOnMouseReleased(e -> { clearBtn.setScaleX(1.0); clearBtn.setScaleY(1.0); });
        clearBtn.setOnAction(e -> pf.clear());
        return clearBtn;
    }
} 