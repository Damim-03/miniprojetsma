package com.retima.agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.retima.models.Property;

public class AgentManager {
    private static AgentContainer mainContainer;
    private static Map<String, AgentController> sellerAgents = new HashMap<>();
    private static Map<String, AgentController> buyerAgents = new HashMap<>();

    public static void initializeJADE() {
        // تهيئة JADE Runtime
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true"); // لتمكين واجهة JADE الرسومية

        mainContainer = runtime.createMainContainer(profile);
        System.out.println("تم إنشاء حاوية JADE الرئيسية بنجاح!");
    }

    public static AgentController createSellerAgent(String agentName, Object[] args) {
        try {
            AgentController agent = mainContainer.createNewAgent(agentName, SellerAgent.class.getName(), args);
            agent.start();
            sellerAgents.put(agentName, agent);
            System.out.println("تم إنشاء وتشغيل وكيل البائع: " + agentName);
            return agent;
        } catch (StaleProxyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AgentController createBuyerAgent(String agentName, Object[] args) {
        try {
            AgentController agent = mainContainer.createNewAgent(agentName, BuyerAgent.class.getName(), args);
            agent.start();
            buyerAgents.put(agentName, agent);
            System.out.println("تم إنشاء وتشغيل وكيل المشتري: " + agentName);
            return agent;
        } catch (StaleProxyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addPropertyToSellerAgent(String agentName, String propertyId, Property property) {
        try {
            AgentController agent = sellerAgents.get(agentName);
            if (agent != null) {
                agent.putO2AObject(new Object[] { propertyId, property }, AgentController.ASYNC);
                System.out.println("تمت إضافة العقار إلى وكيل البائع: " + agentName);
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static void startNegotiation(String buyerAgentName, String propertyId, String sellerAgentName,
            BigDecimal initialOffer, int duration) {
        try {
            AgentController buyerAgent = buyerAgents.get(buyerAgentName);
            if (buyerAgent != null) {
                Object[] args = new Object[] { propertyId, sellerAgentName, initialOffer, duration };
                buyerAgent.putO2AObject(args, AgentController.ASYNC);
                System.out.println("تم بدء التفاوض بين " + buyerAgentName + " و " + sellerAgentName);
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static void removeAgent(String agentName) {
        try {
            if (sellerAgents.containsKey(agentName)) {
                AgentController agent = sellerAgents.get(agentName);
                agent.kill();
                sellerAgents.remove(agentName);
                System.out.println("تم إزالة وكيل البائع: " + agentName);
            } else if (buyerAgents.containsKey(agentName)) {
                AgentController agent = buyerAgents.get(agentName);
                agent.kill();
                buyerAgents.remove(agentName);
                System.out.println("تم إزالة وكيل المشتري: " + agentName);
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static AgentContainer getMainContainer() {
        return mainContainer;
    }
}
