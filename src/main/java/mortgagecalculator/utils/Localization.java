package mortgagecalculator.utils;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {
    private static Localization instance;
    private ResourceBundle bundle;

    public static ResourceBundle getBundle(String localeLanguage) {
        if (instance == null) {
            instance = new Localization();
            setBundle(localeLanguage);
        }

        return instance.bundle;
    }

    public static String getMessage(String key) {
        return getBundle(null).getString(key);
    }

    public static String getCurrencyFormat() {
        return String.format("###,##0.00 %s", getMessage("currency"));
    }

    public static String getFormattedMoney(double amount) {
        DecimalFormat currencyFormat = new DecimalFormat(getCurrencyFormat());
        return currencyFormat.format(amount);
    }

    private static void setBundle(String localeLanguage) {
        Locale locale = new Locale(localeLanguage);
        instance.bundle = ResourceBundle.getBundle("localization.messages", locale);
    }
}
