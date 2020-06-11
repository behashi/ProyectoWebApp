package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	//YA FUNCIONA EL LOGIN
	//TODO Almacenar la cookie y enviarla a la API 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		//En caso de que sean correctas las credenciales se debe mostrar el html con la lista de asignaturas 
    	
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
		URL url = new URL ("http://dew-algrlo-1920.dsic.cloud:9090/CentroEducativo/login");
		
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

		if (responseCode == HttpURLConnection.HTTP_OK) { //Existe el dni en la api*

			//Capturo la cookie de la sesión httpurl para comprobar si ha fallado la contraseña 
			String  cookieVal = con.getHeaderField("Set-Cookie");
			String p;
			StringBuffer content;
			String key ="";
			try {
				BufferedReader in = new BufferedReader(
						  new InputStreamReader(con.getInputStream()));
				String inputLine;
				content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				    
				}
				key = content.toString();
				in.close();
				con.disconnect();
			}catch(Exception e) {
				System.out.print(e.getStackTrace());
			} 
			
			
			
			
			//si no hay header de set-cookie la contraseña esta mal
			if(cookieVal == null) {
				PrintWriter outAux = response.getWriter();
				//TODO imprimir una pagina html con un párrafo homólogo
				outAux.println("Autenticación incorrecta");
				
			} else {
			
		    //sesion sobre la que ir pasando informacion a traves de servlets
			HttpSession session=request.getSession(true);  
			session.setAttribute("dni", usu);
			session.setAttribute("cookie", cookieVal);
			session.setAttribute("key", key);

			
			//llegados a este punto supongo que el login ha funcionado y por tanto llamo al servlet de la lista de asignaturas
			
			/*Si esto no funciona copypasteo la otra opcion menos limpia, cambiad por vuestros usuarios porfa
			 * URL url = new URL("//dew-jomangas-1920.dsic.cloud:8080/NOB_Project/listaAsigServlet");
			   HttpURLConnection conn = (HttpURLConnection)url.openConnection();
               conn.setRequestMethod("GET");
               conn.connect(); */
			
			//RequestDispatcher rd = request.getRequestDispatcher("/AsignaturaServlet");
			RequestDispatcher rd = request.getRequestDispatcher("/Asignaturas.html");
		    rd.forward(request, response);
			
			}
		} else {
			System.out.println("POST fallido o credenciales incorrectas");
			
			//debug
			System.out.println("El responsecode de la conexión es: " + responseCode);
			
			PrintWriter out = response.getWriter();
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
