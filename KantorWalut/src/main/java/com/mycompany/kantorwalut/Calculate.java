/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kantorwalut;

/**
 * Klasa która odpowiada za przeliczenie ile ktoś otrzyma pieniędzy
 * @author matys
 */
public class Calculate {
    /**
     * Funkcja jest stworzona do zwracania ilośći pieniędzy które otrzyma klient w przypadku zakupu
     * @param money ilość podanych pieniędzy w złotówkach
     * @param currency przelicznik wybranej waluty
     * @return zwraca ilość pieniędzy w wybranej walucie
     */
    public float getMoneyBuy(int money, float currency){
        return money/currency;
    }
     /**
     * Funkcja jest stworzona do zwracania ilośći pieniędzy które otrzyma klient w przypadku sprzedaży
     * @param money ilość podanych pieniędzy w złotówkach
     * @param currency przelicznik wybranej waluty
     * @return zwraca ilość pieniędzy w wybranej walucie
     */
    public float getMoneySell(int money, float currency){
        return money*currency;
    }
}
