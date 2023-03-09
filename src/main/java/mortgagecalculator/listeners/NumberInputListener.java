package mortgagecalculator.listeners;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public abstract class NumberInputListener implements ChangeListener<String> {
    protected final TextField textField;
    protected final Number min;
    protected final Number max;

    public NumberInputListener(TextField textField, Number min, Number max) {
        this.textField = textField;
        this.min = min;
        this.max = max;
    }
}
