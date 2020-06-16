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
		
		String dni = request.getParameter("AlumnoDni").toString();
		sesion.setAttribute("dniAl", dni);
		key = sesion.getAttribute("key").toString();
        cookie = sesion.getAttribute("cookie").toString();
        String acr = sesion.getAttribute("acr").toString();
        String nota = request.getParameter("modificar").toString();
        
        try {
        URL url = new URL ("http://localhost:9090/CentroEducativo/alumnos/"+dni+"/asignatura/"+acr+"?key="+key.toLowerCase());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Cookie",cookie);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		OutputStreamWriter ow = new OutputStreamWriter(con.getOutputStream());
		ow.write(nota);
		ow.flush();
		int responseCode = con.getResponseCode();
		System.out.println("Respuesta del PUT:  " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			response.getWriter().write("Se ha insertado la nota");
			RequestDispatcher rd = request.getRequestDispatcher("/DatosAlumno.html");
		    rd.forward(request, response);
			
		}
        
        }catch (Exception e){
    	   PrintWriter oute = response.getWriter();
    	   response.setContentType("text/html");
    	   oute.println(e);
       }  
        
         
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
