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
public class DatoAlumnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    boolean mostrarLog = true;
    String cookie = "";
    String key = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatoAlumnoServlet() {
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
		String dni = sesion.getAttribute("dniAl").toString();
		key = sesion.getAttribute("key").toString();
		cookie = sesion.getAttribute("cookie").toString();
			
		//Creo la llamada HTTP para el REST
		

		URL url = new URL ("http://dew-jomangas-1920.dsic.cloud:9090/CentroEducativo/alumnos/"+ dni + "?key=" + key.toLowerCase());

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
		
		response.getWriter().write(p);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");

        //Paso 1 - Coger el dni del profesor necesario para la petición rest
        String petition = "";
        PrintWriter out = response.getWriter();

        //Paso 2 - Obtener los datos de sesion
        HttpSession sesion = request.getSession(false);
        String dni = sesion.getAttribute("dniAl").toString();
        key = sesion.getAttribute("key").toString();
        cookie = sesion.getAttribute("cookie").toString();

        //Paso 3 - Crear la llamada HTTP para el REST

        URL url = new URL ("http://dew-jomangas-1920.dsic.cloud:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="+key.toLowerCase()) ;
    
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Cookie",cookie);
        connection.setDoOutput(false);

        int responseCode = connection.getResponseCode();

        try {

            //Leer el "GET" que se le hace a la URL y capturar el JSON
            BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                   content.append(inputLine);
            }
            petition=content.toString();
            in.close();
            connection.disconnect();


        }
        catch(Exception e) {
            System.out.print(e.getStackTrace());
        }

        System.out.println("Respuesta del POST:  " + responseCode+"\n");
        response.getWriter().write(petition);
    }
}
