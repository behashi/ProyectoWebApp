package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/**
 * Servlet implementation class AsignaturaServlet
 */
public class AsignaturaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    boolean mostrarLog = true;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsignaturaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*HttpSession sesion = request.getSession();
		
		//Continuar con invocación al servlet correspondiente
		response.setContentType("text/html");
		
		//Cojo la cookie correspondiente de la petición para obtener el id del profesor
		Cookie[] cookies = request.getCookies();
		Cookie c = null;
		
		if(cookies !=null) {
			for(int i =0; i<cookies.length; i++) {
				c = cookies[i];
				if(cookies[i].getValue().equals("0")/*esto de momento no vale) {
					//hacer cosas
				}
				if(mostrarLog) {
					System.out.print("Name: "+c.getName());
					System.out.print("Valor: "+c.getValue());
				}
			}
		}*/
		
		String dni = request.getParameter("dni");
		
		URL url = new URL ("http://dew-swe-1920.dsic.cloud:9090/CentroEducativo/profesores/"+dni+"/asignaturas");
		
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setDoOutput(true);
		int responsecode = con.getResponseCode();
		if(responsecode == HttpURLConnection.HTTP_OK) {
			try { 
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer res = new StringBuffer();
				
				while ((inputLine = in.readLine()) != null) {
					res.append(inputLine);
				}
				in.close();
				
				ServletContext sc = this.getServletContext();
		        RequestDispatcher rd = sc.getRequestDispatcher("/gato.html"); //TODO cambiar por el html de la lista de asignaturas 
		        response.setContentType("text/html");
		        rd.include(request, response);
				
			}catch(Exception e) { System.out.print(e.getStackTrace()); } 
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
