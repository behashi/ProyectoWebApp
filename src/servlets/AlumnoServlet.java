package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
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
import javax.servlet.http.Cookie;

import org.json.JSONObject;
import org.json.JSONArray;
/**
 * Servlet implementation class AsignaturaServlet
 */
public class AlumnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    boolean mostrarLog = true;
    String cookie = "";
    String key = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlumnoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		
		//Cojo el acronimo necesario para la peticion REST		
		StringBuilder res = null;
		String p = "";
		PrintWriter writer = response.getWriter();
		
		HttpSession sesion = request.getSession(false);
		String acronimo = sesion.getAttribute("acr").toString();
		key = sesion.getAttribute("key").toString();
		cookie = sesion.getAttribute("cookie").toString();
			
		//Creo la llamada HTTP para el REST
		
		URL url = new URL ("http://localhost:9090/CentroEducativo/asignaturas/"+ acronimo +"/alumnos?key="+key.toLowerCase());

		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Cookie",cookie);
		con.setDoOutput(false);

		int responseCode = con.getResponseCode();
		
		InputStream inputStream ;
		
		try {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			int i = 0;
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			    //p += content;
			}
			p=content.toString();
			in.close();
			con.disconnect();
		}catch(Exception e) {
			System.out.print(e.getStackTrace());
		}
		
		System.out.println("Respuesta del POST:  " + responseCode+"\n");
		response.getWriter().write(p);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String inputLine;
		String dni = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			
			in.close();
		}catch(Exception e) {
			System.out.print(e.getStackTrace());
		}
		HttpSession session=request.getSession(true);
		session.setAttribute("dni", request.getParameter("boton"));
		session.setAttribute("cookie", cookie);
		session.setAttribute("key", key);
		
		ServletContext sc = this.getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher("/Alumnos.html"); 
        response.setContentType("text/html");
        response.getWriter().write(dni);
        rd.include(request, response);

	}	
	
}