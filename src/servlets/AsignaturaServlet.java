package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
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

import org.json.JSONObject;
import org.json.JSONArray;
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
		response.setContentType("text/html");
		
		//Cojo el dni necesario para la petición restç
		String dni = request.getParameter("user");
		JSONArray listaAsig = new JSONArray();
		
		//Creo la llamada HTTP para el REST
		URL url = new URL ("http://dew-algrlo-1920.dsic.cloud:9090/CentroEducativo/profesores/"+dni+"/asignturas");
		//URL url = new URL ("http://dew-swe-1920.dsic.cloud:9090/CentroEducativo/login");

		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setDoOutput(false);
		int responseCode = con.getResponseCode();
		InputStream inputStream;
		try { 
			//Aqui recogería lo que ha devuelto el GET. Habría que crear un json para ello
			 if (200 <= responseCode && responseCode <= 299) {
			        inputStream = con.getInputStream();
			    } else {
			        inputStream = con.getErrorStream();
			    }

			    BufferedReader in = new BufferedReader(
			        new InputStreamReader(
			            inputStream));
			    
			    StringBuilder res = new StringBuilder();
			    String currentLine;

			    while ((currentLine = in.readLine()) != null) {
			    	res.append(currentLine);
			    	
			    }
			    //listaAsig = (String) res;
			    //Aqui habría que pasar el string a un json para devolverlo
			    //response.add(res);
			    in.close();

			
		} catch (Exception e) { System.out.print(e.getStackTrace()); }
		
		
		
		System.out.println("Respuesta del POST:  " + responseCode);
		
		
		ServletContext sc = this.getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher("/gato.html"); //TODO cambiar por el html de la lista de asignaturas 
        response.setContentType("text/html");
        rd.include(request, response);
			
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
