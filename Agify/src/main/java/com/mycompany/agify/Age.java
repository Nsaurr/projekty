/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agify;

/**
 *
 * @author matys
 */
public class Age {
    private String imie;
    private int wiek;
    private int count;
    public Age(String imie, int wiek, int count) {
        this.imie = imie;
        this.wiek = wiek;
        this.count = count;
    } 

    public int getWiek() {
        return wiek;
    }

    public String getImie() {
        return imie;
    }

    
}
