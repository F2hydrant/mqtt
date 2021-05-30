
    import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

    public class PahoStart {

        public static void main(String[] args) {

            String topic        = "MQTT Examples";
            String content      = "Message";
            int qos             = 0;
            String broker       = "ssl://f34f1ad511494c44bbc3187a510e5359.s1.eu.hivemq.cloud";
            String clientId     = "3310-dcx";
            String username     = "student";
            String password     = "ANu75738";

            MemoryPersistence persistence = new MemoryPersistence();

            try {
                MqttClient sampleClient =new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                connOpts.setPassword(password.toCharArray());
                connOpts.setUserName(username);
                System.out.println("Connecting to broker: "+broker);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                System.out.println("Publishing message: "+content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                sampleClient.publish(topic, message);
                System.out.println("Message published");
                sampleClient.disconnect();
                System.out.println("Disconnected");
                System.exit(0);
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

