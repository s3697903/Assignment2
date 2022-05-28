package main.java;

import main.java.services.MyTiService;

public class MyTiSystem {

    public static void main(String[] args) {
        MyTiService myTiService = new MyTiService();
        myTiService.start();
    }
}
