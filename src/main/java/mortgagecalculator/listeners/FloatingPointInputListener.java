package mortgagecalculator.listeners;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FloatingPointInputListener extends NumberInputListener {
    public FloatingPointInputListener(TextField textField, double min, double max) {
        super(textField, min, max);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.isBlank()) {
            return;
        } else if (newValue.charAt(0) == '.') {
            this.textField.setText(oldValue);
            return;
        }

        final boolean isFloatingNumber = newValue.matches("\\d*(\\.\\d*)?");
        if (!isFloatingNumber) {
            this.textField.setText(oldValue);
            return;
        }

        final double value = Double.parseDouble(newValue);
        if (value < (double)this.min || value > (double)this.max) {
            this.textField.setText(oldValue);
        }
    }
}