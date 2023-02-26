/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kantorwalut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa która odpowiada za zapisywanie informacji o transakcjach do pliku csv
 * @author matys
 */
public class SaveToFile {
    /**
     * Funkcja zapisuje ostatnią transakcję do pliku csv w tym przypadku w trakcie zakupu waluty
     * @param money Ilość pieniędzy w złotówkach
     * @param currencyVal Ilość pienieniędzy w wybranej walucie
     * @param currencyName Nazwa wybranej waluty
     */
    public void saveToFileBuy(int money,double currencyVal, String currencyName){
        File csvFile = new File("history.csv");
        try {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true));
            currencyVal = Math.floor(currencyVal * 100) / 100;
            String currencyString = String.valueOf(currencyVal);
            currencyString = currencyString.replace('.', ',');
            writer.write("PLN;"+currencyName+";"+money+";"+currencyString+";"+formatter.format(date));
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SaveToFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Funkcja zapisuje ostatnią transakcję do pliku csv w tym przypadku w trakcie sprzedaży waluty
     * @param money Ilość pieniędzy w złotówkach
     * @param currencyVal Ilość pienieniędzy w wybranej walucie
     * @param currencyName Nazwa wybranej waluty
     */
    public void saveToFileSell(int money,double currencyVal,String currencyName){
        File csvFile = new File("history.csv");
        try {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true));
            currencyVal = Math.floor(currencyVal * 100) / 100;
            String currencyString = String.valueOf(currencyVal);
            currencyString = currencyString.replace('.', ',');
            writer.write(currencyName+";"+"PLN"+";"+money+";"+currencyString+";"+formatter.format(date));
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SaveToFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
