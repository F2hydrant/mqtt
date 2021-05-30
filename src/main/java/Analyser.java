import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Analyser {

    public static void main(String[] args) {

        String broker       = "ssl://f34f1ad511494c44bbc3187a510e5359.s1.eu.hivemq.cloud";
        String clientId     = "3310-dcx";
        String username     = "student";
        String password     = "ANu75738";
        int qos             = 0;
        int delay           = 0;
        int counter         = 0;
        String topic        = "counter/" + qos + "/" + delay;





        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client =new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setPassword(password.toCharArray());
            connOpts.setUserName(username);
            System.out.println("Connecting to broker: "+broker);
            client.connect(connOpts);
            System.out.println("Connected");


            client.subscribe("counter/#",qos);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("connection lost");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("you received " + new String(mqttMessage.getPayload()) + " from " + s);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("delivery complete");
                }
            });



//            client.disconnect();
//            System.out.println("Disconnected");
//            System.exit(0);


        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }


}
