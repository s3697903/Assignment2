package main.java.gui;

import main.java.helpers.Common;
import main.java.models.Passenger;
import main.java.models.PassengerType;
import main.java.models.TravelPassType;
import main.java.models.ZoneType;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileAccessor {

    public static String SECTION_STARTER = "#";
    public static String USER_SECTION_START = "users";
    public static String PRICES_SECTION_START = "prices";
    public static String DELIMITER = ":";

    public List<Passenger> passengerList = new ArrayList();

    public float[][] prices = new float[][]{{2.5F, 3.5F}, {4.9F, 6.8F}};

    private enum SectionType {
        userSection,
        priceSection,
        unknownSection,
    }

    private String dataFileName = "data/data.txt";
    public boolean writeFile(String content) {
        boolean success = false;

        try{
            FileWriter fw = new FileWriter(this.getFilePath());
            fw.write(content);
            fw.close();
            success = true;
        }catch (Exception ex){
            success = false;
            System.out.println(ex.toString());
        }

        return success;
    }
    public boolean readFile() {
        try{
            FileReader fr = new FileReader(this.getFilePath());
            BufferedReader br = new BufferedReader(fr);

            boolean sectionDetected = false;
            SectionType sectionType = SectionType.unknownSection;

            String line = br.readLine();
            while (line != null) {

                boolean curLineIsSectionHeader = false;

                if(line.startsWith(SECTION_STARTER + USER_SECTION_START)){
                    sectionType = SectionType.userSection;
                    curLineIsSectionHeader = true;
                } else if(line.startsWith(SECTION_STARTER + PRICES_SECTION_START)){
                    sectionType = SectionType.priceSection;
                    curLineIsSectionHeader = true;
                }

                if(curLineIsSectionHeader){
                    line = br.readLine();
                    continue;
                }

                switch (sectionType) {
                    case userSection:
                        Passenger passenger = this.parseUserContent(line);
                        if(passenger != null){
                            this.passengerList.add(passenger);
                        }
                        break;
                    case priceSection:
                        this.parsePriceContent(line);
                        break;
                    default:
                        break;
                }

                line = br.readLine();
            }

            br.close();
        }
        catch (Exception ex){
            System.out.print(ex.toString());
        }

        return true;
    }

    private String getFilePath() {
        String path = Paths.get("").toAbsolutePath().toString();
        return path + "/" + dataFileName;
    }

    private Passenger parseUserContent(String content) {
        if(content.length() == 0) {
            return null;
        }

        String userMeta[] = content.split(DELIMITER);
        if(userMeta.length != 5){
            return null;
        }

        String userId = userMeta[0];
        String userType = userMeta[1];
        String fullName = userMeta[2];
        String email = userMeta[3];

        String firstName = fullName;
        String lastName = Common.EMPTY_STR;
        PassengerType passengerType = PassengerType.ADULT;

        float balance = Float.parseFloat(userMeta[4]);

        if(userType.equals("senior")){
            passengerType = PassengerType.SENIOR;
        } else if(userType.equals("adult")) {
            passengerType = PassengerType.ADULT;
        } else if(userType.equals("junior")) {
            passengerType = PassengerType.JUNIOR;
        } else {
            throw new IllegalArgumentException("Unknow user type: " + userType);
        }

        String[] userNames = fullName.split(" ");

        if(userNames.length >= 2) {
            firstName = userNames[0];
            lastName = userNames[1];
        } else if(userNames.length > 0) {
            firstName = fullName;
        }

        return new Passenger(userId, firstName, lastName, email, passengerType, balance);
    }

    private void parsePriceContent(String content){
        if(content.length() == 0){
            return;
        }

        String[] compoments = content.split(DELIMITER);
        if(compoments.length != 3){
            return;
        }

        String timeType = compoments[0];
        String strZoneType = compoments[1];
        Float price = Float.parseFloat(compoments[2]);

        Integer rowIndex = 0;
        Integer colIndex = 0;

        ZoneType zoneType = ZoneType.ZONE1;
        if(strZoneType.equals("Zone1")) {
            zoneType = ZoneType.ZONE1;
            colIndex = 0;
        } else if(strZoneType.equals("Zone12")) {
            zoneType = ZoneType.ZONE1_2;
            colIndex = 1;
        } else {
            throw new IllegalArgumentException("Unknown zone type: " + strZoneType);
        }

        TravelPassType passType = TravelPassType.TwoHour;
        if(timeType.equals("2Hour")) {
            passType = TravelPassType.TwoHour;
            rowIndex = 0;
        } else if(timeType.equals("AllDay")) {
            passType = TravelPassType.AllDay;
            rowIndex = 1;
        } else {
            throw new IllegalArgumentException("Unknown travel pass type: " + timeType);
        }

        this.prices[rowIndex][colIndex] = price;
    }
}
