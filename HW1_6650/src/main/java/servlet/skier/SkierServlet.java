package servlet.skier;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SeasonsList;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import servlet.channelPool.ChannelPool;

@WebServlet(name = "servlet.skier.SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String urlPath = request.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing parameters");
    }
    String[] urlParts = urlPath.split("/");
    if(!isUrlValid(urlParts)){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("url is not valid");
    }



   PrintWriter out = response.getWriter();
   //out.println("<h1>" + "msg" + "</h1>");
    out.println("1");
   // response.getWriter().write(request.getPathInfo());

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      String urlPath = request.getPathInfo();
      if (urlPath == null || urlPath.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("missing parameters");
        return;
      }
      String[] urlParts = urlPath.split("/");
      if(!isUrlValid(urlParts)){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("url is not valid");
        return;
      }

      try {
        int resortID=Integer.parseInt(urlParts[1]);
        int seasonID=Integer.parseInt(urlParts[3]);
        int dayID=Integer.parseInt(urlParts[5]);
        int skierID=Integer.parseInt(urlParts[7]);
        ChannelPool channelPool=new ChannelPool();
        channelPool.init();
        Channel channel=channelPool.take();
        channel.queueDeclare("post",false,false,false,null);
        channel.basicPublish("","post",null,postRespone);
        channelPool.add(channel);

        response.setStatus(HttpServletResponse.SC_CREATED);
        //LiftRide liftRide=new Gson().toJson();
      }catch (IllegalArgumentException | InterruptedException e){
        response.getWriter().write("missing parameters");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      }



  }
  private boolean isUrlValid(String[] urlPath){
    if(urlPath.length!=8||urlPath.length!=3){
      return false;
    }
    if(!urlPath[2].equals("seasons")||!urlPath[2].equals("vertical")){
      return false;
    }
    if(urlPath[2].equals("seasons")){
      if(urlPath[4].equals("days")){
        if(!urlPath[6].equals("skiers")){
          return false;
        }
      }else{
        return false;
      }
    }
    return true;
  }

}
