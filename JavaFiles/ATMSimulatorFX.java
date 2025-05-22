package ModernATM;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ATMSimulatorFX extends Application {
    private StackPane root;
    private ATMBackend backend;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        backend = new ATMBackend();
        root = new StackPane();
        showWelcome();
        Scene scene = new Scene(root, 900, 650);
        // scene.getStylesheets().add(getClass().getResource("atmfx.css").toExternalForm());
        primaryStage.setTitle("BOM - Bank of Money ATM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showScreen(javafx.scene.Node node) {
        root.getChildren().setAll(node);
    }

    public void showWelcome() {
        showScreen(new WelcomeFX(this));
    }

    public void showTransactionSelection() {
        showScreen(new TransactionSelectionFX(this, backend));
    }

    public void showPinEntry(String nextAction) {
        showScreen(new PinEntryFX(this, backend, nextAction));
    }

    public void showPinEntry() {
        showScreen(new PinEntryFX(this, backend, null));
    }

    public void showAmount(String type) {
        showScreen(new AmountFX(this, backend, type));
    }

    public void showBalance() {
        showScreen(new BalanceFX(this, backend));
    }

    public void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public void showChangePin() {
        showScreen(new ChangePinFX(this, backend));
    }

    public void showStatement() {
        showScreen(new StatementFX(this, backend));
    }

    public static void main(String[] args) {
        launch(args);
    }
} 