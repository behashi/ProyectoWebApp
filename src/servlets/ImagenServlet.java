package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.portable.InputStream;

/**
 * Servlet implementation class ImagenServlet
 */
public class ImagenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImagenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*ServletContext sc = getServletContext();
		HttpSession sesion = request.getSession(false);
		String dni = sesion.getAttribute("dniAl").toString();
		int valorDado = (int) Math.floor(Math.random()*60+1);
        try (InputStream is = (InputStream) sc.getResourceAsStream("/imagenes/"+valorDado+".jpg")) {

            // it is the responsibility of the container to close output stream
            OutputStream os = response.getOutputStream();

            if (is == null) {

                response.setContentType("text/plain");
                os.write("Failed to send image".getBytes());
            } else {

                byte[] buffer = new byte[1024];
                int bytesRead;

                response.setContentType("image/png");

                while ((bytesRead = is.read(buffer)) != -1) {

                    os.write(buffer, 0, bytesRead);
                }
            }
        }*/
	
	
		HttpSession sesion = request.getSession(false);
		String dni = request.getParameter("AlumnoDni").toString();
		sesion.setAttribute("dniAl",dni);
		String carpeta = getServletContext().getRealPath("imagenes");
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		BufferedReader origen = new BufferedReader(new FileReader(carpeta+"/"+dni+".pngb64"));
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.print("{\"dni\": \""+dni+"\", \"img\": \""); 
		String linea = origen.readLine(); out.print(linea); 
		while ((linea = origen.readLine()) != null) {out.print("\n"+linea);}
		out.print("\"}");
		out.close(); origen.close();
 }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
