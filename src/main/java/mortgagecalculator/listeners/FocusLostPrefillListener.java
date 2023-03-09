package mortgagecalculator.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FocusLostPrefillListener implements ChangeListener<Boolean> {
    private TextField textField;
    private String prefillValue;

    public FocusLostPrefillListener(TextField textField, String value) {
        this.textField = textField;
        this.prefillValue = value;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue && this.textField.getText().isBlank()) {
            this.textField.setText(prefillValue);
        }
    }
}
