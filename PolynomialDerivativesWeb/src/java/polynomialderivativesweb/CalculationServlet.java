package polynomialderivativesweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.ServletContext;


/**
 * A servlet implementing an access to calculation part of model.
 *
 * @author Arkadiusz Pilarski
 * @version 5.0
 */
//@WebServlet(urlPatterns = {"/CalculationServlet"})
public class CalculationServlet extends HttpServlet {

    /**
     * A model of the application, created once.
     */
    private final Model model;

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
     * Class constructor.
     */
    public CalculationServlet() {
        model = new Model();
    }

    /**
     * A method that sets the polynomial in readable format.
     *
     * @param coefficients stores coefficients values.
     * @param degree stores a polynomial degree value.
     * @return returns a string containing polynomial that has been set.
     */
    public String setPolynomial(double[] coefficients, int degree) {
        boolean firstViewed;
        int ifJustZeroes;
        int i;
        String result = "";

        if (coefficients != null) {
            firstViewed = false;
            ifJustZeroes = 0;
            result = "";

            //----------------------------------------------
            // Calculates how many coefficients of 0 value there
            // are and privents such coefficients from displaying.
            for (i = degree; i >= 0; i--) {
                if (coefficients[i] == 0) {
                    ifJustZeroes += 1;
                    continue;
                }

                //----------------------------------------------
                // Creating a suitable display format
                if (coefficients[i] > 0 && firstViewed) {
                    result += ("+");
                }
                if (i == 0) {
                    result += (coefficients[i] + "");
                } else if (i != 1) {
                    result += (coefficients[i] + "x^" + i + " ");
                } else {
                    result += (coefficients[i] + "x ");
                }
                firstViewed = true;
            }

            //----------------------------------------------
            // Handling the situation with only-zero coefficients values
            if (ifJustZeroes == degree + 1) {
                result = ("0");
            }
        }
        return result;
    }

    /**
     * Handles the HTTP <code>GET</code> method. Checkes the entered parameters.
     * Makes all necessary conversions. Runs calculations with entered values.
     * Shows results of programs work. Stores results using both HttpSession and
     * cookies.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html; charset=ISO-8859-2");
        HttpSession session = req.getSession(true);

        //----------------------------------------------
        //Getting Connection object from context.
        context = getServletContext();
        con = (Connection) context.getAttribute("connection");

        //----------------------------------------------
        //Setting connection with database if not set yet.
        if (con == null) {
            String database = context.getInitParameter("database");
            String username = context.getInitParameter("username");
            String password = context.getInitParameter("password");
            String driver = context.getInitParameter("driver");

            try {
                Class.forName(driver);
                con = DriverManager.getConnection(database, username, password);
                System.out.println("Connected to database!");

//                // Creating table if not created yet (on project purposes).
//                statement.executeUpdate("CREATE TABLE Historia"
//                        + "(sessionID VARCHAR(40), insertedPolynomial VARCHAR(1000), "
//                        + "derivativeOrder INTEGER, calculatedPolynomial VARCHAR(1000) )");
//                System.out.println("Table created");

                //Setting context attribute
                context.setAttribute("connection", con);

            } catch (SQLException sqle) {
                System.err.println("SQL exception: " + sqle.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println("ClassNotFound exception: " + cnfe.getMessage());
            }
        }

        //----------------------------------------------
        // Setting the page outlook
        PrintWriter out = res.getWriter();
        res.setContentType("text/html");
        out.println("<style>.nowa2 {color: greenyellow; text-align: center;}</style>");
        out.println("<body style=\"background-color:black\">");
        out.println("<div class=\"nowa2\">");
        String result = "", firstResult = "";;

        //----------------------------------------------
        // Getting and validaiting parameters
        try {
            int degree = Integer.parseInt(req.getParameter("degreeText"));
            int order = Integer.parseInt(req.getParameter("orderText"));
            String coeffs = req.getParameter("coefficientsText");

            //----------------------------------------------
            // Creating a suitable coefficients table 
            String[] tab2;
            tab2 = coeffs.split(" ");
            double[] coefficients = new double[tab2.length];
            int i = 0;
            for (String coef : tab2) {
                coefficients[i++] = Double.parseDouble(coef);
            }
            model.checkParameters(coefficients, degree, order);

            //----------------------------------------------
            // Displaying entered polynomial
            out.print("<title>Polynomials</title>");
            out.print("<h3>Entered polynomial</h3>");
            result = setPolynomial(coefficients, degree);
            out.println(result);
            firstResult = result;
            out.println("<br><br>");

            //----------------------------------------------
            // Running calculations
            try {
                model.checkDegreeOrderRelation(degree, order);
                model.calculateDerivative(coefficients, coefficients, degree, order);
                result = setPolynomial(coefficients, degree);

            } catch (DegreeToOrderRelationException e) {
                result = "0";
            }

            //----------------------------------------------
            // Displaying calculated polynomial
            out.println("<h3>Calculated polynomial</h3>");
            out.println(result);

            //----------------------------------------------
            // Adding to database
            statement = con.createStatement();
            statement.executeUpdate("INSERT INTO Historia VALUES ('" + session.getId() + "'" + ",'"
                    + firstResult + "'," + order + ", '" + result + "')");
            System.out.println("Data added!");

            //----------------------------------------------
            // Creating/getting a session object 
            ArrayList<String> results;
            Object obj = session.getAttribute("historiaWielomianow");
            if (obj == null) {
                results = new ArrayList<>();
            } else {
                results = (ArrayList<String>) obj;
            }

            //----------------------------------------------
            // Adding a new result to the object storing all results
            // and updating a session object.
            results.add(result);
            session.setAttribute("historiaWielomianow", results);

            //----------------------------------------------            
            // Adding a cookie to the users' browser
            Cookie cookie = new Cookie("cookie" + req.getCookies().length, result);
            res.addCookie(cookie);

        } catch (NumberFormatException e) {
            res.sendError(res.SC_BAD_REQUEST, "Unsuitable data entered.");
        } catch (CalculationException e) {
            res.sendError(res.SC_BAD_REQUEST, "Calculations went wrog.");
        } catch (NullPointerException e) {
            Cookie cookie = new Cookie("cookie0", result);
            res.addCookie(cookie);
        } catch (SQLException sqle) {
            System.err.println("SQL exception: " + sqle.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

}
