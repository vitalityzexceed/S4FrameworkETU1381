/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1381.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import etu1381.framework.Mapping;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.init.Infoclass;


/**
 *
 * @author OMEN
 */
public class FrontServlet extends HttpServlet {
    //private HashMap<String, Mapping> MappingUrls;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FrontServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>This is the sliced request " + request.getPathInfo() + "</h1>");
            out.println("<p>Liste des classes</p>");
            
            List<Class<?>> listeclasses = new ArrayList<>();
            try
            {
                listeclasses = Infoclass.recupclasses();
            }
            catch(Exception e)
            {
                out.println(e.getMessage());
            }
            HashMap<String, Mapping> hashmap = new HashMap<>();

            out.println("<ul>Liste des classes");
            for(Class<?> classe : listeclasses)
            {
                out.println("<li>" + classe.getName());
                    out.println("<ul>Liste des methodes");
                    Method[] listemethodes = Infoclass.recupmethods(classe);
                    for(Method methode : listemethodes)
                    {
                        try {
                            hashmap = Infoclass.geturlannotationhashmap();
                        } catch (Exception e) {
                            out.println(e.getMessage());
                        }

                        out.println("<li>" + methode.getName());
                        String url = "";
                        if (methode.isAnnotationPresent(URLAnnotation.class)) {
                            URLAnnotation annotation = methode.getAnnotation(URLAnnotation.class);
                            url = annotation.value();
                            out.println("<br>Voici l'url correspondant a la methode : " + url);
                        }
                        out.println("</li>");

                    }
                    out.println("</ul>");
                out.println("</li>");
            }
            out.println("</ul>");

            out.println("</ul>Hashmap : ");
            for(String cle : hashmap.keySet())
            {
                out.println("<li> Cle " + cle + " Valeur mapping : classname = " + hashmap.get(cle).getClassName() + " , methode = " + hashmap.get(cle).getMethod());
                out.println("</li>");

            }
            out.println("</ul>");

            out.println("</body>");
            out.println("</html>");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
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
            throws ServletException, IOException
    {
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
