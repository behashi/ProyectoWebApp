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

import java.net.HttpURLConnection;
import java.net.URL;

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
    	
		URL url = new URL ("https://reqres.in/api/users");
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
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //T
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer res = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				res.append(inputLine);
			}
			in.close();

			// printeo el resultado para debuggear (?)
			System.out.println(res.toString());
			
			//en "res" deberia tener el token(cookie) que autoriza el login
			
			String cookie = res.toString();
			
			
			
		} else {
			//System.out.println("POST fallido");
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
