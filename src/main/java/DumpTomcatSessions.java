import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public final class DumpTomcatSessions {

    static final String USAGE = "Usage: java -jar DumpTomcatSessions.jar server port [user password]";

    private DumpTomcatSessions() {}

    public static void main(String[] args) {
        System.exit(run(args, System.out, System.err));
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length != 2 && args.length != 4) {
            err.println(USAGE);
            return 1;
        }

        String server = args[0];
        String port = args[1];
        String user = args.length == 4 ? args[2] : null;
        String password = args.length == 4 ? args[3] : null;

        try {
            dumpSessions(server, port, user, password, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace(err);
            return 2;
        }
    }

    static void dumpSessions(String server, String port, String user, String password, PrintStream out)
            throws Exception {
        Map<String, Object> environment = null;
        if (user != null && password != null) {
            environment = new HashMap<>();
            environment.put(JMXConnector.CREDENTIALS, new String[] { user, password });
        }

        JMXServiceURL url = new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://" + server + ":" + port + "/jmxrmi");

        try (JMXConnector conn = JMXConnectorFactory.connect(url, environment)) {
            MBeanServerConnection mbsc = conn.getMBeanServerConnection();
            ObjectName catman = new ObjectName("Catalina:type=Manager,context=*,host=*");

            for (ObjectInstance x : mbsc.queryMBeans(catman, null)) {
                ObjectName name = x.getObjectName();
                Integer numSessions = (Integer) mbsc.getAttribute(name, "activeSessions");

                if (numSessions != null && numSessions > 0) {
                    out.println(name + " total sessions = " + numSessions);
                    Object result = mbsc.invoke(name, "listSessionIds", null, null);
                    for (String session : result.toString().split(" ")) {
                        out.println(session);
                    }
                }
            }
        }
    }
}
