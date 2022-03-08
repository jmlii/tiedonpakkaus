package tiedonpakkaus;

import java.io.IOException;
import tiedonpakkaus.ui.UserInterface;

public class Main {

    /**
     * Käynnistää ohjelman käyttöliittymän.
     * @param args 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException {      
        new UserInterface().start();
    }    
}