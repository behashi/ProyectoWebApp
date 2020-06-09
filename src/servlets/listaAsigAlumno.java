package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/**
 * Servlet implementation class listaAsigAlumno
 */
public class listaAsigAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String cookie = "";
    String key = "";
    String dni = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public listaAsigAlumno() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		StringBuilder res = null;
		String p = "";
		PrintWriter writer = response.getWriter();
		
		HttpSession sesion = request.getSession(false);
		String dni = sesion.getAttribute("dniAl").toString();
		key = sesion.getAttribute("key").toString();
		cookie = sesion.getAttribute("cookie").toString();
		
		//Creo la llamada HTTP para el REST
		URL url = new URL ("http://dew-jaipocar-1920.dsic.cloud:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="+key.toLowerCase());

		//URL url = new URL ("http://dew-swe-1920.dsic.cloud:9090/CentroEducativo/login");
		//	response.getWriter().write("URL: "+url.toString()+"\n");

		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("Content-Type", "application/json; utf-8");
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
		//writer.print(res);
		response.getWriter().write(p);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
