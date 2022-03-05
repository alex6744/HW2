package part2;


import java.io.FileNotFoundException;

public class test {
  public static void main(String[] args) throws InterruptedException, FileNotFoundException {
    String url="http://54.184.180.83:8080/HW1_6650";
    String url1="http://localhost:8080/HW1_6650_war_exploded";
    Client_part2 c=new Client_part2(32,20000,url);
   // c.start();

    Client_part2 c1=new Client_part2(64,20000,url);
    //c1.start();

    Client_part2 c3=new Client_part2(128,20000,url);
    c3.start();
   // Thread.sleep(1000);
    Client_part2 c2=new Client_part2(256,20000,url);
    //c2.start();
  }
}
