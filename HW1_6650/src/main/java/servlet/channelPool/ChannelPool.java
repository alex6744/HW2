package servlet.channelPool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class ChannelPool {

  private static int CHANNEL=10;
  private static BlockingQueue<Channel> blockingQueue;
  private Connection connection;
  private ConnectionFactory factory;

  public void init(){
    if(blockingQueue!=null){
      return;
    }


    try {
      blockingQueue=new LinkedBlockingDeque<>();
      factory=new ConnectionFactory();
      factory.setHost("ec2-52-25-30-250.us-west-2.compute.amazonaws.com");
      factory.setPort(5672);
      factory.setUsername("admin");
      factory.setPassword("12345");
      connection=factory.newConnection();
      for(int i=0;i<CHANNEL;i++){
        blockingQueue.add(connection.createChannel());
      }


    } catch (TimeoutException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }
  public Channel take() throws InterruptedException {
    return blockingQueue.take();
  }
  public boolean add(Channel channel){
    return blockingQueue.add(channel);
  }


}
