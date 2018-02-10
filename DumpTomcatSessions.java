//  DumpTomcatSessions.java

import java.util.HashMap;
import javax.management.remote.*;
import javax.management.*;

public class DumpTomcatSessions {

  public static void main(String[] args) {

    if(args.length < 2 || args.length > 4) {
      System.err.println("\nUsage: java DumpTomcatSessions server port [user password]\n");
      System.exit(1);
    }

    String server = args[0];
    String port = args[1];

    if(args.length == 4) {
      String user = args[2];
      String password = args[3];
      dumpSessions(server,port,user,password);
    }
    else {
      dumpSessions(server,port,null,null);
    }
  }

  static void dumpSessions(String server, String port, String user, String password) {
    try {
      HashMap<String, String[]> environment = null;
      if(user != null && password != null)
      {
        environment = new HashMap<>();
        String[] credentials = new String[] { user, password };
        environment.put(JMXConnector.CREDENTIALS, credentials);
      }

      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + server + ":" + port + "/jmxrmi");
      JMXConnector conn = JMXConnectorFactory.connect(url,environment);
      MBeanServerConnection mbsc = conn.getMBeanServerConnection();
      ObjectName catman = new ObjectName("Catalina:type=Manager,context=*,host=*");

      for(ObjectInstance x : mbsc.queryMBeans(catman, null)) {
        Integer num_sessions = (Integer)mbsc.getAttribute(x.getObjectName(), "activeSessions");

        if(num_sessions > 0) {
          System.out.println(x.getObjectName().toString() + " total sessions = " + num_sessions);
          Object result = mbsc.invoke(x.getObjectName(), "listSessionIds", null, null);
          String[] sessions = result.toString().split(" ");

          for(int i=0;i < sessions.length;i++) {
            System.out.println(sessions[i]);
          }
        }
      }
      System.exit(0);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
