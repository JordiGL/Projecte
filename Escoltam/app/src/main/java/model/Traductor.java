package model;

/**
 * Classe del traductor
 * @author Jordi Gómez lozano
 */
public class Traductor {
    private static final String SERVICE_LOCATION = "westeurope";
    private static final String SERVICE_KEY = "08a345d692964185b09497d593e7fa2b";
    private String subscriptionKey;
    private String location;

    public Traductor(){
        this.subscriptionKey = SERVICE_KEY;
        this.location = SERVICE_LOCATION;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
