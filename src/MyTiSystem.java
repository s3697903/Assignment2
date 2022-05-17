import java.util.Scanner;

public class MyTiSystem {

    public static void main(String[] args) {
        String[] tiMainMenus = new String[]{ "1.Buy a Journey for a User", "2.Recharge a MyTi card for a User", "3.Show remaining credit for a User", "4.Print User Reports", "5.Update pricing", "6.Show Station statistics", "7.Add a new User", "8.Quit"};
        MyTiSystem.printMenu("Options:", tiMainMenus);
        boolean quite = false;
        while (!quite) {
            String userInput = waitUsersChoice("What is your selection: ");
            if(userInput.equals("1")){

            } else if(userInput.equals("2")){

            } else if(userInput.equals("3")){

            } else if(userInput.equals("4")){

            } else if(userInput.equals("5")) {

            } else if(userInput.equals("6")) {

            } else if(userInput.equals("7")) {

            } else if(userInput.equals("8")){
                quite = true;
            } else{
                System.out.println("Sorry, that is an invalid option!");
            }
        }

        System.out.println("Goodbye!");
    }

    /**
     * waiting for users input
     * @param instruction an instruction message
     * @return the users input
     */
    private static String waitUsersChoice(String instruction){
        // if the instruction string is null then print an empty string instead
        System.out.print(instruction == null ? "" : instruction);
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    /**
     * print menu
     * @param instruction an instruction message
     * @param menus the menus
     */
    private static void printMenu(String instruction, String[] menus){
        System.out.println(instruction);
        for (String menu : menus) {
            System.out.println(menu);
        }
    }
}
