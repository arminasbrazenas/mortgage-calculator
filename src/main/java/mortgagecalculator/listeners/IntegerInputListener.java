package mortgagecalculator.listeners;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class IntegerInputListener extends NumberInputListener {
    public IntegerInputListener(TextField textField, int min, int max) {
        super(textField, min, max);
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (newValue.isBlank()) {
            return;
        }

        final boolean isInteger = newValue.matches("[0-9]*");
        if (!isInteger) {
            this.textField.setText(oldValue);
            return;
        }

        final int value = Integer.parseInt(newValue);
        if (value < (int)this.min || value > (int)this.max) {
            this.textField.setText(oldValue);
        }
    }
}
