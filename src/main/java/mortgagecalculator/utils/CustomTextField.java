package mortgagecalculator.utils;

import mortgagecalculator.listeners.FloatingPointInputListener;
import mortgagecalculator.listeners.FocusLostPrefillListener;
import mortgagecalculator.listeners.IntegerInputListener;
import javafx.scene.control.TextField;

public class CustomTextField {
    public static void initializeNumbered(TextField textField, int minValue, int maxValue) {
        textField.setText(Integer.toString(minValue));
        textField.textProperty().addListener(
                new IntegerInputListener(textField, minValue, maxValue)
        );
        textField.focusedProperty().addListener(
                new FocusLostPrefillListener(textField, Integer.toString(minValue))
        );
    }

    public static void initializeNumbered(TextField textField, double minValue, double maxValue) {
        textField.setText(Double.toString(minValue));
        textField.textProperty().addListener(
                new FloatingPointInputListener(textField, minValue, maxValue)
        );
        textField.focusedProperty().addListener(
                new FocusLostPrefillListener(textField, Double.toString(minValue))
        );
    }
}
