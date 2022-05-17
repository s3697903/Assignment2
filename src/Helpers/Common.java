package Helpers;

import java.util.Scanner;

public class Common {

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
}
