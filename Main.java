package com.retima;

import javax.swing.*;

import com.retima.agents.AgentManager;
import com.retima.ui.MainMenu;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import javax.swing.*;
import java.awt.*;
import jade.core.Profile;
import jade.wrapper.AgentContainer;

public class Main {
     private static AgentContainer mainContainer;

    public static void main(String[] args) {
        // إعداد JADE
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        mainContainer = Runtime.instance().createMainContainer(profile);

        // تهيئة نظام الوكلاء JADE
        AgentManager.initializeJADE();

        // بدء واجهة المستخدم
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        });
         
    }
}
