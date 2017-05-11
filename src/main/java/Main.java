import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Main extends HttpServlet {
  enum HttpHeader {
    X_FORWARDED_FOR("X-Forwarded-For");

    private String key;

    private HttpHeader(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    for (HttpHeader header : HttpHeader.values()) {
      String v = req.getHeader(header.key);
      resp.getWriter().print(header.key + ": " + v);
    }
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
