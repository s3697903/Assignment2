package Helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Common {

    public static final int NOT_FOUND = -1;
    public static final String EMPTY_STR = "";

    /**
     * waiting for users input
     * @param instruction an instruction message
     * @return the users input
     */
    public static String waitUsersChoice(String instruction){
        // if the instruction string is null then print an empty string instead
        System.out.print(instruction == null ? "" : instruction);
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public static boolean isTextNullOrEmpty(String strValue){
        return strValue == null || strValue.isEmpty();
    }

    /**
     * convert string to int
     * @param strValue a string which need to be converted
     * @param forceSanitized if force sanitizing the string by removing the zeros at leading and trailing.
     * @return a Tuple object in which
     *          the first item stands for conversion successful or not,
     *          the second item stands for  int value
     */
    public static Tuple<Boolean, Integer> convertStr2Int(String strValue, boolean forceSanitized){
        int value = 0;
        boolean success;
        try{
            if(forceSanitized){
                strValue = sanitizeString(strValue);
            }
            value = Integer.parseInt(strValue);
            success = true;
        }
        catch (Exception ex){
            success = false;
        }

        return new Tuple<Boolean, Integer>(success, value);
    }

    /**
     * sanitize the string by removing all the zeros at leading and trailing.
     * @param strValue the string which needs to be processed.
     * @return the sanitized string.
     */
    private static String sanitizeString(String strValue){
        if(Common.isTextNullOrEmpty(strValue)){
            return strValue;
        }

        // remove leading zeros.
        String text = removeLeadingZeros(strValue);
        if(text.length() <= 0){
            return text; // nothing in the string, no need to process further.
        }

        int indexOfPoint = text.indexOf('.');

        // indexOfPoint == 0 stands for the format like .23
        // indexOfPoint == NOT_FOUND stands for 10
        // no need to process for trailing zeros.
        if(indexOfPoint == 0 || indexOfPoint == Common.NOT_FOUND){
            return text;
        }

        // remove trailing zeros.
        String reversedStr = new StringBuilder(text).reverse().toString();
        text = removeLeadingZeros(reversedStr);

        // check if the first char is '.'
        int index = text.indexOf('.');
        if(index == 0 && index + 1 < text.length()){
            text = text.substring(index + 1);
        }

        if(text.length() == 1){
            return text; // no need to reverse
        }

        // reverse back
        return new StringBuilder(text).reverse().toString();
    }

    /**
     * remvoe the leading zeros from a string.
     * @param strValue the string which need to be processed.
     * @return a string which removed all the leainding zeros already.
     */
    private static String removeLeadingZeros(String strValue){
        if(Common.isTextNullOrEmpty(strValue)){
            return strValue;
        }

        StringBuilder sb = new StringBuilder(strValue);

        // remove leading zeros
        while (sb.length() > 0 && sb.charAt(0) == '0'){
            sb.deleteCharAt(0);
        }

        String newValue = sb.toString();
        if(Common.isTextNullOrEmpty(newValue)){
            return Common.EMPTY_STR;
        }

        return newValue;
    }
}
