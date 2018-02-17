package polynomialderivativesweb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet implementing an access to history of calculations.
 *
 * @author Arkadiusz Pilarski
 * @version 5.0
 */
//@WebServlet(urlPatterns = {"/HistoryServlet"})
public class HistoryServlet extends HttpServlet {

    /**
     * Connection object.
     */
    private Connection con;

    /**
     * ServletContext object.
     */
    private ServletContext context;

    /**
     * Statement object.
     */
    private Statement statement;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Displays history stored in users' cookies and HttpSession
     * object.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        context = getServletContext();
        con = (Connection) context.getAttribute("connection");

        //----------------------------------------------
        //Setting connection with database
        if (con == null) {
            String database = context.getInitParameter("database");
            String username = context.getInitParameter("username");
            String password = context.getInitParameter("password");
            String driver = context.getInitParameter("driver");

            try {
                // Ładujemy plik z klasą sterownika bazy danych
                Class.forName(driver);
                // Tworzymy połączenie do bazy danych
                con = DriverManager.getConnection(database, username, password);
                System.out.println("Connected to database!");

                //Setting attribute
                context.setAttribute("connection", con);

            } catch (SQLException sqle) {
                System.err.println("SQL exception: " + sqle.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println("ClassNotFound exception: " + cnfe.getMessage());
            }
        }

        //----------------------------------------------
        // Seting the page outlook
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CookieHistory</title>");
            out.println("</head>");
            out.println("<style>.nowa2 {color: greenyellow; text-align: center;}</style>");
            out.println("<body style=\"background-color:black\">");
            out.println("<div class=\"nowa2\">");

            //----------------------------------------------
            // Displaying history stored in users' cookies 
            out.println("<h4>Your recently calculated polynomials <br>stored in <font color=\"red\">cookies</font>:</h4>");
            Cookie[] cookies = req.getCookies();
            if (cookies != null && cookies.length >= 2) {
                for (Cookie cookie : cookies) {
                    if (!cookie.getName().equals("JSESSIONID")) {
                        out.println(cookie.getValue() + "<br>");
                    }
                }
            } else {
                out.println("None.");
            }

            //----------------------------------------------
            // Displaying history stored in a session object
            out.println("<h4>Your recently calculated polynomials <br>stored in <font color=\"red\">HttpSession</font>:</h4>");
            ArrayList<String> results = (ArrayList<String>) req.getSession().getAttribute("historiaWielomianow");
            if (results != null && results.size() >= 1) {
                for (String s : results) {
                    out.println(s + "<br>");
                }
            } else {
                out.println("None.");
            }

            out.println("<h4>Recently calculated polynomials <br>stored in <font color=\"red\">Database</font>:</h4>");

            //----------------------------------------------
            // Displaying history stored in database
            try {
                statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM Historia");
                out.println("<font color=\"red\">|-----</font>------------------------------------------------------------------------------------<font color=\"red\">-----|</font>");
                out.println("<br>");
                out.println("<br>");

                while (rs.next()) {
                    out.println("Entered: ");
                    out.println(rs.getString("insertedpolynomial"));
                    out.println("<br>");
                    out.println("Derivative order:");
                    out.println(rs.getString("derivativeorder"));
                    out.println("<br>");
                    out.println("Calculated: ");
                    out.println(rs.getString("calculatedpolynomial"));
                    out.println("<br>");
                    out.println("<br>");
                    out.println("<font color=\"red\">|-----</font>------------------------------------------------------------------------------------<font color=\"red\">-----|</font>");
                    out.println("<br>");
                    out.println("<br>");
                }
                rs.close();
            } catch (SQLException sqle) {
                System.err.println("SQL exception: " + sqle.getMessage());
            }

            out.println("</body>");
            out.println("</html>");

        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
