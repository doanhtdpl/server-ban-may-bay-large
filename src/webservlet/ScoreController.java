/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author LinhTA
 */
public class ScoreController extends ServerServlet{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try {
			doProcess(req, resp);
		} catch (Exception ex) {
			//log.error(LogUtil.stackTrace(ex));
		}
    
    }

    private void doProcess(HttpServletRequest req, HttpServletResponse resp) {
        
        System.out.print(req.getRequestURI());
        System.out.print(req);
    }
    
    
}
