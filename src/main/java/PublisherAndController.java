import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublisherAndController {
    public static void main(String[] args) {
        controller(0,500);

    }

    public static void controller(int reqQos, int reqDelay) {

        String broker = "ssl://f34f1ad511494c44bbc3187a510e5359.s1.eu.hivemq.cloud";
        String clientId = "3310-dcx";
        String username = "student";
        String password = "ANu75738";
        int qos = reqQos;
        int delay = reqDelay;
        int counter = 0;
        String topic = "counter/" + qos + "/" + delay;


        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setPassword(password.toCharArray());
            connOpts.setUserName(username);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");


            System.out.println("subscribing");
            client.subscribe("request/+", 2);
            System.out.println("subscribe complete");


            MqttCallback mqttCallback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("connection lost");
                }
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String message = new String(mqttMessage.getPayload());

                    if (s.equals("request/qos")) {

                        System.out.println("you received " + message + " from " + s);
                        controller(Integer.parseInt(message),delay);

                    } else if (s.equals("request/delay")) {
                        System.out.println("you received " + message + " from " + s);
                        controller(qos,Integer.parseInt(message));
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }

            };


            client.setCallback(mqttCallback);

//publisher

            while (true) {
                counter = counter + 1;
                System.out.println("Publishing message to " + "counter/" + qos + "/" + delay + ": " + counter);
                MqttMessage message = new MqttMessage(String.valueOf(counter).getBytes());
                message.setQos(qos);
                client.publish(topic, message);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


//            client.disconnect();
//            System.out.println("Disconnected");
//            System.exit(0);


        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
