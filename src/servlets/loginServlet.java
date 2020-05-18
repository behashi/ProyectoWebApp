package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;



/**
 * Servlet implementation class loginServlet
 * Este servlet se utiliza en la implementación del primer hito del proyecto;
 * en él se debe permitir logearse y ofrecer ciertas funcionalidades a un profesor.
 */
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
    }
	
	//TODO Crear objeto json con dni y contraseña(?)
	//TODO Almacenar la cookie y enviarla a la API 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		/**En caso de que sean correctas las credenciales se debe mostrar el html con la lista de asignaturas 
		 * Cada asignatura es un href a la lista de alumnos de esa asignatura en caso de que la consulta a dicha 
		 * lista sea correcta. 
		 * Cada alumno de este segundo html es un href a los detalles de ese mismo alumno en caso de que la consulta
		 * sea correcta.
		 */
    	
    	//Paso 1 - Pillar usuario y contraseña del profesor 
    	String usu = request.getParameter("user");
    	String pass = request.getParameter("pass");
    	
    	response.setContentType("text/html");
    	 
    	//Paso 2 - Comprobar si existe
    	
    	JSONObject profesor =new JSONObject();
    	
		try {    
    	  profesor.put("dni", usu);    
    	  profesor.put("password", pass);
		} catch (Exception e) { System.out.print(e.getStackTrace()); }
    	
		
		//debug
		System.out.println("El json que se le va a mandar a la api es: " + profesor);
		
		
		//En la URL donde pone swe se debe sustituir por el user de cada uno de la upv.
		URL url = new URL ("http://dew-jomangas-1920.dsic.cloud:9090/CentroEducativo/login");
		
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setDoOutput(true);
		
		try { 
			OutputStream os = con.getOutputStream();
		    byte[] input = profesor.toString().getBytes("utf-8");
		    os.write(input, 0, input.length);           
		} catch (Exception e) { System.out.print(e.getStackTrace()); } 
		
		int responseCode = con.getResponseCode();
		System.out.println("Respuesta del POST:  " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //Existe la combinación en la api.
			/*
			 
			//capturo la cookie de la conexión para confirmar que estamos loggeados
			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL); //desconozco si es del todo necesario
			
			//recorro la lista de cookies de la conexión
			List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
			
			//debug para comprobar el length de cookies
			System.out.println("El length de cookies es: " + cookies.size()); 
			
			*/
			
			//llegados a este punto supongo que el login ha funcionado y por tanto muestro la lista de asignaturas del profesor(otro html)
			ServletContext sc = this.getServletContext();
	        RequestDispatcher rd = sc.getRequestDispatcher("/gato.html"); //TODO cambiar por el html de la lista de asignaturas 
	        response.setContentType("text/html");
	        rd.include(request, response);
			
			
		} else {
			System.out.println("POST fallido o credenciales incorrectas");
			
			//debug
			System.out.println("El responsecode de la conexión es: " + responseCode);
			
			PrintWriter out = response .getWriter();
			//TODO imprimir una pagina html con un párrafo homólogo
			out.println("Autenticación incorrecta");
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
