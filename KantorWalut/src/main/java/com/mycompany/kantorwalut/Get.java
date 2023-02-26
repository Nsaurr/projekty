/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kantorwalut;

import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Klasa która odpowiada za pobieranie przelicznika za api NBP
 * @author matys
 */
public class Get {
    private String link;
    private float currencyvalue;
    private String currency;
    /**
     * Nadaje odpowiednią wartość waluty do linku
     * @param currency Wybrana waluta
     */
    public void category(String currency){
        this.link = "https://api.nbp.pl/api/exchangerates/rates/c/"+currency+"?format=XML";
    }
    /**
     * Nadaje odpowiedni kurs waluty w tym przypadku do zakupu danej waluty
     */
    public void getCurrencyvalueBuy(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(link).openStream());
            this.currencyvalue = Float.valueOf(doc.getElementsByTagName("Bid").item(0).getTextContent());
	} catch (Exception e) {
            System.out.println(e);
	}
    }
     /**
     * Nadaje odpowiedni kurs waluty w tym przypadku do sprzedaży danej waluty
     */
    public void getCurrencyvalueSell(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(link).openStream());
            this.currencyvalue = Float.valueOf(doc.getElementsByTagName("Ask").item(0).getTextContent());
	} catch (Exception e) {
            System.out.println(e);
	}
    }
    /**
     * 
     * @return Zwraca przelicznik waluty do klasy main
     */
    public float getCurrency(){
        return currencyvalue;
    }
}
