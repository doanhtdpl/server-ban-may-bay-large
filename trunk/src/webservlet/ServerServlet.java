/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservlet;

/**
 *
 * @author LinhTA
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class ServerServlet extends HttpServlet {

    private static Logger logger_ = Logger.getLogger(ServerServlet.class);

    protected void echo(Object text, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            out.print(text);
        } catch (IOException ex) {
            logger_.error(ex.getMessage());
        } finally {
            out.close();
        }
    }

}
