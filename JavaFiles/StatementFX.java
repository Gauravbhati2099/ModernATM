package ModernATM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class StatementFX extends VBox {
    public StatementFX(ATMSimulatorFX app, ATMBackend backend) {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setFillWidth(true);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6336ff, #ffb74d);");

        // Top bar with Cancel
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        cancelBtn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 16; -fx-padding: 8 16; -fx-effect: dropshadow(gaussian, #ccc, 2, 0, 0, 1);");
        cancelBtn.setOnAction(_ -> app.showTransactionSelection());
        HBox.setMargin(cancelBtn, new Insets(0, 0, 0, 24));
        topBar.getChildren().add(cancelBtn);
        getChildren().add(topBar);

        // Title
        Label title = new Label("Transaction History");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 64));
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        // Transaction list
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox transactionList = new VBox(12);
        transactionList.setAlignment(Pos.CENTER);
        transactionList.setPadding(new Insets(10));
        transactionList.setStyle("-fx-background-color: rgba(255,255,255,0.18); -fx-background-radius: 16;");

        List<ATMBackend.TransactionRecord> transactions = backend.getTransactionHistory();
        if (transactions.isEmpty()) {
            Label noTransactions = new Label("No transactions yet");
            noTransactions.setFont(Font.font("SansSerif", FontWeight.NORMAL, 24));
            noTransactions.setTextFill(Color.WHITE);
            transactionList.getChildren().add(noTransactions);
        } else {
            for (ATMBackend.TransactionRecord t : transactions) {
                HBox transaction = createTransactionRow(t);
                transactionList.getChildren().add(transaction);
            }
        }

        scrollPane.setContent(transactionList);
        scrollPane.setMaxHeight(320);
        scrollPane.setMaxWidth(900);
        VBox.setMargin(scrollPane, new Insets(0, 0, 0, 0));
        getChildren().add(scrollPane);

        // Print button (centered, max width, with animation)
        HBox printBox = new HBox();
        printBox.setAlignment(Pos.CENTER);
        Button printBtn = new Button("Print Statement");
        ButtonStyles.styleMainButton(printBtn);
        printBtn.setMaxWidth(400);
        printBtn.setPrefWidth(400);
        printBtn.setStyle(printBtn.getStyle() + "; transition: all 0.2s;");
        printBtn.setOnMousePressed(e -> { printBtn.setScaleX(0.96); printBtn.setScaleY(0.96); });
        printBtn.setOnMouseReleased(e -> { printBtn.setScaleX(1.0); printBtn.setScaleY(1.0); });
        printBox.getChildren().add(printBtn);
        VBox.setMargin(printBox, new Insets(0, 0, 24, 0));
        getChildren().add(printBox);

        printBtn.setOnAction(_ -> {
            // TODO: Implement print functionality
            // For now, just show a message
            Label printMsg = new Label("Statement printed successfully!");
            printMsg.setFont(Font.font("SansSerif", FontWeight.NORMAL, 22));
            printMsg.setTextFill(Color.LIGHTGREEN);
            printMsg.setMaxWidth(Double.MAX_VALUE);
            printMsg.setAlignment(Pos.CENTER);
            getChildren().add(printMsg);
        });

        // Keyboard navigation for Cancel and Print Statement buttons
        Button[] navButtons = {cancelBtn, printBtn};
        final int[] focusedIndex = {1}; // Start with Print Statement focused
        printBtn.requestFocus();
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                    focusedIndex[0] = 1 - focusedIndex[0];
                    navButtons[focusedIndex[0]].requestFocus();
                    event.consume();
                    break;
                case ENTER:
                    navButtons[focusedIndex[0]].fire();
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

    private HBox createTransactionRow(ATMBackend.TransactionRecord t) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: rgba(255,255,255,0.35); -fx-background-radius: 14; -fx-padding: 14 32;");
        row.setMaxWidth(820);

        // Transaction type
        Label type = new Label(t.type);
        type.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        type.setTextFill(Color.WHITE);
        type.setMinWidth(120);

        // Amount
        Label amount = new Label(String.format("$%.2f", t.amount));
        amount.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        amount.setTextFill(t.type.equals("Withdraw") ? Color.web("#ff4d6d") : Color.web("#4dff88"));
        amount.setMinWidth(100);

        // Balance after
        Label balance = new Label(String.format("Balance: $%.2f", t.balanceAfter));
        balance.setFont(Font.font("SansSerif", FontWeight.NORMAL, 18));
        balance.setTextFill(Color.web("#222").deriveColor(0,1,1,0.7));
        balance.setMinWidth(150);

        // Date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Label date = new Label(sdf.format(new Date(t.timestamp)));
        date.setFont(Font.font("SansSerif", FontWeight.NORMAL, 18));
        date.setTextFill(Color.web("#222").deriveColor(0,1,1,0.7));
        date.setMinWidth(150);

        row.getChildren().addAll(type, amount, balance, date);
        return row;
    }
} 