package com.retima.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.retima.models.Property;

public class SellerAgent extends Agent {
    private Map<String, Property> listedProperties = new HashMap<>();

    // ✅ معلومات البائع
    private String sellerName;
    private String phone;
    private String address;
    private int age;
    private String maritalStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Override
    protected void setup() {
        // ✅ استخراج المعلومات من getArguments
        Object[] args = getArguments();
        if (args != null && args.length >= 7) {
            sellerName = (String) args[0];
            phone = (String) args[1];
            address = (String) args[2];
            age = (Integer) args[3];
            maritalStatus = (String) args[4];
            minPrice = (BigDecimal) args[5];
            maxPrice = (BigDecimal) args[6];
        }

        // تسجيل الوكيل في الدليل
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("real-estate-seller");
        sd.setName(getLocalName() + "-seller");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println("وكيل البائع " + getLocalName() + " تم تسجيله بنجاح!");
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // إضافة سلوك لاستقبال طلبات الشراء
        addBehaviour(new HandleBuyRequests());
    }

    @Override
    protected void takeDown() {
        // إلغاء تسجيل الوكيل من الدليل
        try {
            DFService.deregister(this);
            System.out.println("وكيل البائع " + getLocalName() + " تم إلغاء تسجيله!");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    // ✅ إضافة عقار للبيع
    public void addProperty(int propertyId, Property property) {
        listedProperties.put(String.valueOf(propertyId), property);
    }

    // ✅ السلوك الداخلي لاستقبال التفاوض
    private class HandleBuyRequests extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                String propertyId = msg.getConversationId();
                String content = msg.getContent();
                BigDecimal offerPrice = new BigDecimal(content.split(",")[0]);
                int offerDuration = Integer.parseInt(content.split(",")[1]);

                ACLMessage reply = msg.createReply();

                if (listedProperties.containsKey(propertyId)) {
                    Property property = listedProperties.get(propertyId);

                    property.incrementNegotiationRounds();

                    if (offerPrice.compareTo(property.getMinPrice()) >= 0 &&
                            offerDuration >= property.getDuration()) {

                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent("تم قبول العرض بسعر " + offerPrice + " لمدة " + offerDuration + " شهر");

                        property.setStatus(Property.Status.RENTED);
                    } else {
                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);

                        BigDecimal counterOffer = property.getMinPrice().add(
                                property.getMaxPrice().subtract(property.getMinPrice())
                                        .multiply(new BigDecimal("0.3")));

                        reply.setContent(counterOffer + "," + property.getDuration());
                    }
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("العقار غير متوفر");
                }

                myAgent.send(reply);
            } else {
                block();
            }
        }
    }

    // ✅ Getters لمعلومات البائع
    public String getSellerName() {
        return sellerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
}
