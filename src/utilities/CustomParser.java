package utilities;

import exceptions.InputErrorException;

public class CustomParser {
    public static char parseChar(String input) throws InputErrorException {
        if (input.length() != 1) {
            throw new InputErrorException();
        } else {
            return input.charAt(0);
        }
    }

    public static int parseInt(String input) throws InputErrorException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException numberFormatException) {
            throw new InputErrorException();
        }
    }
}
