package filtroLogs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import java.time.LocalDateTime;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import filtroLogs.logger;


public class logsAcceso implements Filter {
	
	
	
    public logsAcceso() {}

	public void init(FilterConfig fConfig) throws ServletException {
	    

		
		
	}
	

		 

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//Sol1
		HttpServletRequest peticion = (HttpServletRequest) request;
		
		logger logObject;
		Logger log = null;
		
		try {
			logObject = new logger("");
			log = logObject.getLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Obtenemos la fecha
		LocalDateTime fecha = LocalDateTime.now();
		
		//Obtenemos el nombre de usuario
		HttpSession sesion=((HttpServletRequest) request).getSession(false); 
		String user = sesion.getAttribute("user").toString();
		
		//Obtenemos el nombre del servlet
		String servlet = peticion.getServletPath();
				
		log.info("Fecha: "+fecha+"   "+"Usuario: "+user+"   "+"Servlet: "+servlet);

	        
	        chain.doFilter(request, response);

		
	}
	
	
	
	public void destroy() {}

}
