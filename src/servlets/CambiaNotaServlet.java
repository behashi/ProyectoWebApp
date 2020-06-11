package servlets;

import java.io.IOException;
import java.io.OutputStream;
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
		String acr = request.getParameter("modificar");
		String dni = sesion.getAttribute("dniAl").toString();
		String nota = request.getParameter("nota");
		key = sesion.getAttribute("key").toString();
        cookie = sesion.getAttribute("cookie").toString();
        
        URL url = new URL ("http://localhost:9090/CentroEducativo/asignaturas/"+ acr +"/alumnos?key="+key.toLowerCase());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Cookie",cookie);
		con.setDoOutput(true);
		JSONObject notaCambiada =new JSONObject();
    	
		try {    
			notaCambiada.put("nota", nota);
		} catch (Exception e) { System.out.print(e.getStackTrace()); }
		try { 
			OutputStream os = con.getOutputStream();
		    byte[] input = notaCambiada.toString().getBytes("utf-8");
		    os.write(input, 0, input.length);           
		} catch (Exception e) { System.out.print(e.getStackTrace()); } 
        
		int responseCode = con.getResponseCode();
		System.out.println("Respuesta del POST:  " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			ServletContext sc = this.getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/DatoAlumnoServlet");
			rd.include(request, response);
		}
        
        /*
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String pre = "<!DOCTYPE html>\n<html>\n<head>\n"
    			+ "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
        out.println(pre);
		out.println("<h1>"+arc+"</h1>");
		out.println("<h1>"+dni+"</h1>");
		out.println("<h1>"+key+"</h1>");
		out.println("<h1>"+cookie+"</h1>");
		out.println("</body>\n</html>");*/
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
