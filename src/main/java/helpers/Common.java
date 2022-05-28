package main.java.helpers;

import main.java.models.*;

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

    /**
     * convert string to locatedatetime.
     * @param formatTime the formated datetime string.
     * @return the localdatetime
     */
    public static LocalDateTime getLocalDateTimeFromString(String formatTime) {
        LocalDateTime dateTime = null;

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dateTime = LocalDateTime.parse(formatTime, formatter);
        } catch (Exception ex){
            dateTime = null;
            System.out.print(ex.toString());
        }

        return dateTime;
    }

    /**
     * convert localdatetime to string
     * @param dateTime a local datetime object
     * @return a datetime string
     */
    public static String convertLocalDateTimeToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * check if the string is null or empty
     * @param strValue a string which need to check
     * @return if the string is null or empty
     */
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


    public static String printOutTiReceipt(TiReceipt receipt, String userId) {
        if(receipt.getNoEnoughBlance()) {
            return "You don't have enough balance.";
        }

        if(receipt.getNewTicket()) {

            String strPassType = Common.getPassTypeText(receipt.getPassType());
            String strZone = Common.getZoneTypeText(receipt.getZoneType());
            String strConcession = receipt.getConcession()? "(Concession)" : "";

            String message = String.format("%s %s %s Travel Pass purchased for %s for $%.2f", strPassType, strZone, strConcession, userId, receipt.getCost());
            if(receipt.getExpireTime() != null) {
                message = message + "\n" + "Valid until " + Common.convertLocalDateTimeToString(receipt.getExpireTime());
            }

            return message;
        } else {

            String strPassType = Common.getPassTypeText(receipt.getPassType());
            String strZone = Common.getZoneTypeText(receipt.getZoneType());
            String message = String.format("Current %s %s Travel Pass still valid", strPassType, strZone);
            return message;
        }
    }

    public static PassengerType getPassengerTypeFromText(String strPassengerType) {

        PassengerType passType = PassengerType.ADULT;
        if(strPassengerType.equals("Senior")){
            passType = PassengerType.SENIOR;
        } else if(strPassengerType.equals("Junior")) {
            passType = PassengerType.JUNIOR;
        } else if(strPassengerType.equals("Adult")) {
            passType = PassengerType.ADULT;
        } else {
            throw new IllegalArgumentException("Invalid passenger type: " + strPassengerType);
        }

        return passType;
    }

    private static String getPassTypeText(TravelPassType passType){
        String strPassType = "";
        if(passType == TravelPassType.TwoHour) {
            strPassType = "2 Hours";
        } else if(passType == TravelPassType.AllDay) {
            strPassType = "All day";
        }

        return strPassType;
    }

    private static String getZoneTypeText(ZoneType zoneType){
        String strZone = "";
        if(zoneType == ZoneType.ZONE1) {
            strZone = "Zone 1";
        } else if(zoneType == ZoneType.ZONE1_2) {
            strZone = "Zone 1&2";
        }

        return strZone;
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
