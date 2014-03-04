/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpservice;

import libCore.Config;
import org.apache.log4j.Logger;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * @author ngaht
 */
public class WebServer {

    private static Logger logger_ = Logger.getLogger(WebServer.class);

    public void start() throws Exception {
        Server server = new Server();

        //setup JMX
        MBeanContainer mbContainer = new MBeanContainer(java.lang.management.ManagementFactory.getPlatformMBeanServer());
        server.getContainer().addEventListener(mbContainer);
        server.addBean(mbContainer);
        mbContainer.addBean(Log.getLog());

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(100);
        threadPool.setMaxThreads(2000);
        server.setThreadPool(threadPool);
        System.out.println(Config.getHomePath());
        String port = Config.getParam("rest", "port_listen");
        int port_listen = Integer.parseInt(port);

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port_listen);
        connector.setMaxIdleTime(30000);
        connector.setConfidentialPort(8443);
        connector.setStatsOn(false);
        connector.setLowResourcesConnections(20000);
        connector.setLowResourcesMaxIdleTime(5000);

        server.setConnectors(new Connector[]{connector});


        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        logger_.info(" ************************** ");

        handler.addServletWithMapping("webservlet.ScoreController", "/");
        handler.addServletWithMapping("webservlet.ScoreController", "/*");
        
//        handler.addServletWithMapping("webservlet.BlogComController", "/");
//        handler.addServletWithMapping("webservlet.BlogComController", "/*");
//        handler.addServletWithMapping("webservlet.CategoryController", "/cate/");
//        handler.addServletWithMapping("webservlet.CategoryController", "/cate/*");
//        handler.addServletWithMapping("webservlet.CampainController", "/cate/su-kien/");
//        handler.addServletWithMapping("webservlet.CampainController", "/cate/su-kien/*");
//        handler.addServletWithMapping("webservlet.CateBigEventController", "/cate/nu-sinh-duyen-dang-viet-nam/");
//        handler.addServletWithMapping("webservlet.CateBigEventController", "/cate/nu-sinh-duyen-dang-viet-nam/*");
//        handler.addServletWithMapping("webservlet.AjaxController", "/ajx/");
//        handler.addServletWithMapping("webservlet.AjaxController", "/ajx/*");
//        handler.addServletWithMapping("webservlet.AjaxCateController", "/ajxcate/");
//        handler.addServletWithMapping("webservlet.AjaxCateController", "/ajxcate/*");
//         handler.addServletWithMapping("webservlet.AjaxBigCateController", "/ajxbigcate/");
//        handler.addServletWithMapping("webservlet.AjaxBigCateController", "/ajxbigcate/*");
//        handler.addServletWithMapping("webservlet.AjaxCampainController", "/ajxcampain/");
//        handler.addServletWithMapping("webservlet.AjaxCampainController", "/ajxcampain/*");
//        handler.addServletWithMapping("webservlet.AjaxStatusController", "/ajxstatus/");
//        handler.addServletWithMapping("webservlet.AjaxStatusController", "/ajxstatus/*");
//        
        server.setStopAtShutdown(true);
        server.setSendServerVersion(true);
       

//        String zk_hosts = Config.getParam("servicemap", "zk_hosts");
//        String servicename = Config.getParam("servicemap", "services");
//        ConfigServiceMap servicemap = ConfigServiceMap.getInstance();
//        servicemap.init(zk_hosts, servicename);

        logger_.info("==============================");
	logger_.info("Webserver is starting to listen");
        
        server.start();
        server.join();
    }
}
