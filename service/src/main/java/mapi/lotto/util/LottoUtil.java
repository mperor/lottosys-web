package mapi.lotto.util;

import java.time.Year;
import java.time.ZoneId;

public class LottoUtil {

    private LottoUtil() {
    }

    public static int getCurrentYear() {
	return Year.now(ZoneId.of("Poland")).getValue();
    }

    public static boolean isDateFormatCorrect(String date) {
	return date.matches("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*");
    }

    public static boolean isNaturalNumber(String value) {
	return value.matches("\\d+");
    }

    public static boolean isUnique(int[] array) {
	for (int i = 0; i < array.length; i++) {
	    for (int j = i + 1; j < array.length; j++) {
		if (array[i] == array[j]) {
		    return false;
		}
	    }
	}
	return true;
    }

    public static boolean containDrawNumbers(int[] array) {
	for (int i : array) {
	    if (i < 1 || i > 49) {
		return false;
	    }
	}
	return true;
    }

}
