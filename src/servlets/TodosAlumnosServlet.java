package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.json.JSONArray;
/**
 * Servlet implementation class TodosAlumnosServlet
 */
public class TodosAlumnosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TodosAlumnosServlet() {
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
		String respuesta = "";
		PrintWriter writer = response.getWriter();
		
		HttpSession sesion = request.getSession(false);
		String acronimo = sesion.getAttribute("acr").toString();
		String key = sesion.getAttribute("key").toString();
		String cookie = sesion.getAttribute("cookie").toString();
			
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
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			respuesta=content.toString();
			in.close();
			con.disconnect();
		}catch(Exception e) {
			System.out.print(e.getStackTrace());
		}
		//construimos el json a partir de la respuesta
		
		
		JSONArray alumnos=null;
		JSONArray resultado = new JSONArray();
		try {
			alumnos = new JSONArray(respuesta);
		}catch(Exception e) {
			System.out.println("Sus muertos: "+e);
		}
		//----------------Ahora llamo por cada alumnos------------------------//
		

		for(int i=0;i<alumnos.length();i++){
			String dni="";
			String nota="";
			try {
				dni = alumnos.getJSONObject(i).getString("alumno");
				nota = alumnos.getJSONObject(i).getString("nota");

			}catch(Exception e) {
				System.out.print("Erro parseando JSON: "+e);
			}
			
			url = new URL ("http://localhost:9090/CentroEducativo/alumnos/"+ dni + "?key=" + key.toLowerCase());

			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Cookie",cookie);
			con.setDoOutput(false);
			responseCode = con.getResponseCode();
					
			try {
				BufferedReader in = new BufferedReader(
						  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				respuesta=content.toString();
				in.close();
				con.disconnect();
			}catch(Exception e) {
				System.out.print(e.getStackTrace());
			}
			//Creamos el object de cada alumno y se lo metemos al array
			JSONObject al=null;
			String nombre, apellidos;
			try {
				al = new JSONObject(respuesta);
				nombre = al.getString("nombre");
				apellidos = al.getString("apellidos");
				//buscamos dni
				JSONObject o = new JSONObject();
				o.put("dni",dni);
				o.put("nombre",nombre);
				o.put("apellidos",apellidos);
				o.put("nota",nota);
				//
				resultado.put(i,o);
				
			}catch(Exception e) {
				System.out.println("Sus muertos al: "+e);
			}
		}
		
		response.getWriter().write(resultado.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
