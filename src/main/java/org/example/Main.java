package org.example;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import ui.LoginScreen;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // FlatLaf temasını uyguluyoruz
        } catch (Exception ex) {
            System.err.println("FlatLaf teması yüklenemedi: " + ex);
        }

        new LoginScreen().setVisible(true); // Login ekranı açılıyor
    }
}
