package ModernATM;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TransactionSelectionFX extends VBox {
    public TransactionSelectionFX(ATMSimulatorFX app, ATMBackend backend) {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        Label title = new Label("BOM");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 96));
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        Label prompt = new Label("What kind of transaction would you like?");
        prompt.setFont(Font.font("SansSerif", FontWeight.NORMAL, 32));
        prompt.setTextFill(Color.WHITE);
        prompt.setMaxWidth(Double.MAX_VALUE);
        prompt.setAlignment(Pos.CENTER);
        getChildren().add(prompt);

        VBox buttonBox = new VBox(24);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(false);
        buttonBox.setMinWidth(400);
        buttonBox.setPrefWidth(400);
        buttonBox.setMaxWidth(400);

        Button depositBtn = createButton("Deposit");
        ButtonStyles.styleMainButton(depositBtn);
        depositBtn.setMinWidth(400);
        depositBtn.setPrefWidth(400);
        depositBtn.setMaxWidth(400);
        depositBtn.setOnAction(_ -> app.showAmount("deposit"));

        Button withdrawBtn = createButton("Withdraw");
        ButtonStyles.styleMainButton(withdrawBtn);
        withdrawBtn.setMinWidth(400);
        withdrawBtn.setPrefWidth(400);
        withdrawBtn.setMaxWidth(400);
        withdrawBtn.setOnAction(_ -> app.showAmount("withdraw"));

        Button balanceBtn = createButton("Check Balance");
        ButtonStyles.styleMainButton(balanceBtn);
        balanceBtn.setMinWidth(400);
        balanceBtn.setPrefWidth(400);
        balanceBtn.setMaxWidth(400);
        balanceBtn.setOnAction(_ -> app.showBalance());

        Button changePinBtn = createButton("Change PIN");
        ButtonStyles.styleMainButton(changePinBtn);
        changePinBtn.setMinWidth(400);
        changePinBtn.setPrefWidth(400);
        changePinBtn.setMaxWidth(400);
        changePinBtn.setOnAction(_ -> app.showChangePin());
        VBox.setMargin(changePinBtn, new Insets(0, 40, 0, 40));

        Button statementBtn = createButton("View Statement");
        ButtonStyles.styleMainButton(statementBtn);
        statementBtn.setMinWidth(400);
        statementBtn.setPrefWidth(400);
        statementBtn.setMaxWidth(400);
        statementBtn.setOnAction(_ -> app.showStatement());
        VBox.setMargin(statementBtn, new Insets(0, 40, 40, 40));

        buttonBox.getChildren().addAll(depositBtn, withdrawBtn, balanceBtn, changePinBtn, statementBtn);
        getChildren().add(buttonBox);

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button cancelBtn = new Button("Cancel");
        ButtonStyles.styleCancelButton(cancelBtn);
        cancelBtn.setOnAction(_ -> System.exit(0));
        HBox.setMargin(cancelBtn, new Insets(0, 0, 0, 24));
        topBar.getChildren().add(cancelBtn);
        getChildren().add(0, topBar);

        // Keyboard navigation for main menu buttons
        Button[] menuButtons = {depositBtn, withdrawBtn, balanceBtn, changePinBtn, statementBtn};
        final int[] focusedIndex = {0};
        menuButtons[0].requestFocus();
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    focusedIndex[0] = (focusedIndex[0] - 1 + menuButtons.length) % menuButtons.length;
                    menuButtons[focusedIndex[0]].requestFocus();
                    event.consume();
                    break;
                case DOWN:
                    focusedIndex[0] = (focusedIndex[0] + 1) % menuButtons.length;
                    menuButtons[focusedIndex[0]].requestFocus();
                    event.consume();
                    break;
                case ENTER:
                    menuButtons[focusedIndex[0]].fire();
                    event.consume();
                    break;
                case LEFT:
                case RIGHT:
                    event.consume();
                    break;
                default:
                    break;
            }
        });
        this.setFocusTraversable(true);
        this.requestFocus();
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 36));
        return btn;
    }
} 