package com.retima.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigDecimal;

public class BuyerAgent extends Agent {
    private String buyerName;
    private String phone;
    private String address;
    private int age;
    private String maritalStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Override
    protected void setup() {
        // استخراج المعلومات من الـ arguments
        Object[] args = getArguments();
        if (args != null && args.length >= 7) {
            buyerName = (String) args[0];
            phone = (String) args[1];
            address = (String) args[2];
            age = (Integer) args[3];
            maritalStatus = (String) args[4];
            minPrice = (BigDecimal) args[5];
            maxPrice = (BigDecimal) args[6];
        }

        // تفعيل استقبال الرسائل من Java عبر O2A
        setEnabledO2ACommunication(true, 0);

        // تسجيل الوكيل في DF
        registerInDF();

        // سلوك دوري لاستقبال طلبات التفاوض من Java
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                Object obj = getO2AObject();
                if (obj != null && obj instanceof Object[]) {
                    Object[] data = (Object[]) obj;
                    if (data.length == 4) {
                        String propertyId = (String) data[0];
                        String sellerAgentName = (String) data[1];
                        BigDecimal initialOffer = (BigDecimal) data[2];
                        int duration = (int) data[3];

                        AID sellerAID = new AID(sellerAgentName, AID.ISLOCALNAME);
                        negotiateProperty(propertyId, sellerAID, initialOffer, duration);
                    }
                } else {
                    block();
                }
            }
        });
    }

    // تسجيل الوكيل في دليل الخدمات
    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("real-estate-buyer");
        sd.setName(getLocalName() + "-buyer");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println("وكيل المشتري " + getLocalName() + " تم تسجيله بنجاح!");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        // إلغاء التسجيل من DF
        deregisterFromDF();
    }

    private void deregisterFromDF() {
        try {
            DFService.deregister(this);
            System.out.println("وكيل المشتري " + getLocalName() + " تم إلغاء تسجيله!");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    // بدء التفاوض
    public void negotiateProperty(final String propertyId, final AID sellerAgent, final BigDecimal initialOffer,
            final int duration) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // إرسال العرض
                ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                msg.addReceiver(sellerAgent);
                msg.setConversationId(propertyId);
                msg.setContent(initialOffer + "," + duration);

                myAgent.send(msg);
                System.out.println(
                        "المشتري " + getLocalName() + " أرسل عرض: " + initialOffer + " لمدة " + duration + " شهر");

                // انتظار الرد من البائع
                MessageTemplate mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(propertyId),
                        MessageTemplate.or(
                                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                                MessageTemplate.or(
                                        MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL),
                                        MessageTemplate.MatchPerformative(ACLMessage.REFUSE))));

                ACLMessage reply = myAgent.blockingReceive(mt, 10000); // 10 ثوانٍ

                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        System.out.println("✅ تم قبول العرض! " + reply.getContent());
                        // يمكنك هنا حفظ النتيجة في قاعدة البيانات مثلاً
                    } else if (reply.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println("❌ تم رفض العرض. عرض مضاد: " + reply.getContent());
                        // يمكنك الرد بعرض جديد أو إنهاء التفاوض
                    } else {
                        System.out.println("🚫 العقار غير متاح أو تم تأجيره.");
                    }
                } else {
                    System.out.println("⚠️ لم يتم الرد في الوقت المحدد.");
                }
            }
        });
    }

    // Getters
    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerPhone() {
        return phone;
    }

    public String getBuyerAddress() {
        return address;
    }

    public int getBuyerAge() {
        return age;
    }

    public String getBuyerMaritalStatus() {
        return maritalStatus;
    }

    public BigDecimal getMinBudget() {
        return minPrice;
    }

    public BigDecimal getMaxBudget() {
        return maxPrice;
    }
}
