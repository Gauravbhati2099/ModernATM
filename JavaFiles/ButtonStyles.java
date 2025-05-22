package ModernATM;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ButtonStyles {
    // Main action buttons (large, full width)
    public static void styleMainButton(Button btn) {
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(60);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #fff, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-padding: 16 32; -fx-effect: dropshadow(gaussian, #ccc, 8, 0, 0, 4); -fx-border-color: #fff; -fx-border-width: 2;"));
    }

    // Cancel button (smaller, top-left)
    public static void styleCancelButton(Button btn) {
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 16; -fx-padding: 8 16; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2);");
        btn.setMaxWidth(Double.MAX_VALUE);
    }

    // Clear button (small, square)
    public static void styleClearButton(Button btn) {
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 20; -fx-border-radius: 20; -fx-effect: dropshadow(gaussian, #ccc, 2, 0, 0, 1); -fx-border-color: #fff; -fx-border-width: 2; -fx-cursor: hand; -fx-min-width: 40px; -fx-min-height: 40px; -fx-pref-width: 40px; -fx-pref-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-padding: 4 8 4 8;");
        btn.setFocusTraversable(false);
    }

    // Keypad button (medium, square)
    public static void styleKeypadButton(Button btn) {
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;");
        btn.setPrefHeight(60);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-effect: dropshadow(gaussian, #fff, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: #6336ff; -fx-background-radius: 32; -fx-border-radius: 32; -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 2); -fx-border-color: #fff; -fx-border-width: 2;"));
    }
} 