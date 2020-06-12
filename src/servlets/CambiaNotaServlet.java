package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;


public class CambiaNotaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String cookie = "";
    String key = "";   

    public CambiaNotaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);
		String dni = sesion.getAttribute("dniAl").toString();
		key = sesion.getAttribute("key").toString();
        cookie = sesion.getAttribute("cookie").toString();
        String acr = sesion.getAttribute("acr").toString();
        String nota = request.getParameter("modificar").toString();
        try {
        URL url = new URL ("http://dew-jaipocar-1920.dsic.cloud:9090/CentroEducativo/alumnos/"+dni+"/asignatura/"+acr+"?key="+key.toLowerCase());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Cookie",cookie);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		/*JSONObject notaCambiada =new JSONObject();
    	
		try {    
			notaCambiada.put("nota", nota);
		} catch (Exception e) { System.out.print(e.getStackTrace()); }
		try { 
			OutputStream os = con.getOutputStream();
		    byte[] input = notaCambiada.toString().getBytes("utf-8");
		    os.write(input, 0, input.length);           
		} catch (Exception e) { System.out.print(e.getStackTrace()); } */
		OutputStreamWriter ow = new OutputStreamWriter(con.getOutputStream());
		ow.write(nota);
		ow.flush();
		int responseCode = con.getResponseCode();
		System.out.println("Respuesta del PUT:  " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			ServletContext sc = this.getServletContext();
			response.getWriter().write("Se ha insertado la nota");
			
		}
       }catch (Exception e){
    	   PrintWriter oute = response.getWriter();
    	   response.setContentType("text/html");
    	   oute.println(e);
       }  
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String pre = "<!DOCTYPE html>\n<html>\n<head>\n"
    			+ "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
        out.println(pre);
		out.println("<h1>"+acr+"</h1>");
		out.println("<h1>"+dni+"</h1>");
		out.println("<h1>"+key+"</h1>");
		out.println("<h1>"+cookie+"</h1>");
		out.println("<h1>"+nota+"</h1>");
		out.println("</body>\n</html>"); 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
