import Models.Passenger;
import Services.MyTiService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyTiSystem {

    public static void main(String[] args) {
        MyTiService myTiService = new MyTiService();
        myTiService.start();
    }
}
