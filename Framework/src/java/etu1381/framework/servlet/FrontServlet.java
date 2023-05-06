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
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.util.List;
import etu1381.framework.Mapping;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.init.Infoclass;
import etu1381.framework.modelview.ModelView;


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
            String urlentree = null;
            try
            {
                urlentree = request.getRequestURI();
                System.out.println("urlentree : " + urlentree);
                System.out.println("last : " + urlentree.substring(urlentree.lastIndexOf('/')));
                urlentree = urlentree.substring(urlentree.lastIndexOf('/')).split(".do")[0];
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

            HashMap<String, Mapping> hashmap = new HashMap<>();

            try {
                hashmap = Infoclass.geturlannotationhashmap();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //Sprint 5 : test sy mitovy ilay URL sur navigateur sy ny iray amin'ilay key hashmap
            Mapping matchedmapping = null;
            int indice = 0;
            for(String cle : hashmap.keySet())
            {
                indice++;
                if(cle.equals(urlentree))
                {
                    matchedmapping = hashmap.get(cle);
                    String matchedmethodname = matchedmapping.getMethod();
                    Method matchedmethod = null;
                    //alaina daholo aloha ny methodes ao amin'ilay class dia avy eo apidirina ao amin'ilay matchedmethod amzay tsy sahirana raha
                    //misy arguments ilay methode
                    Method[] matchedclassmethods = Class.forName(matchedmapping.getClassName()).getMethods();
                    for (Method method : matchedclassmethods) {
                        if (method.getName().equals(matchedmethodname)) {
                            matchedmethod = method;
                        }
                    }
                    String typederetour = matchedmethod.getReturnType().getSimpleName();
                    System.out.println("type de retour : " + typederetour);
                    ModelView returnedmodelview = null;
                    if (typederetour.equals("ModelView"))
                    {
                        // get the parameter types for the method
                        Class<?>[] parameterTypes = matchedmethod.getParameterTypes();
                        // create an array of arguments to pass to the method
                        Object[] arguments = new Object[parameterTypes.length];
                        // set the arguments to appropriate values (in this case, null)
                        Arrays.fill(arguments, null);
                        //atao dynamique ilay invoke
                        returnedmodelview = (ModelView)matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments);
                        //out.println("Mi retourne modelView => jsp : " + returnedmodelview.getView());
                        //Sprint 6 : avant de dispatch, mettre des informations dans la requete
                        //test de valeurs dans l'attribut data
                        returnedmodelview.setData(new HashMap<String, Object>());
                        returnedmodelview.addItem("Nom", new String("Rakoto"));
                        returnedmodelview.addItem("Prenom", new String("Jean"));
                        //test de valeurs dans l'attribut data

                        for (String cledata : returnedmodelview.getData().keySet()) {
                            request.setAttribute(cledata, returnedmodelview.getData().get(cledata));
                        }
                        RequestDispatcher dispat = request.getRequestDispatcher(""+returnedmodelview.getView()+".jsp");
                        dispat.forward(request,response);
                    }
                    else
                    {
                        out.println("tsy mi-retourne modelView");
                    }
                    break;
                }
                else
                {
                    if (indice == hashmap.size()) {
                        /* TODO output your page here. You may use following sample code. */
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Servlet FrontServlet</title>");            
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<h1>This is the sliced request " + urlentree + "</h1>");
                        out.println("<p>Liste des classes</p>");
                        
                        List<Class<?>> listeclasses = new ArrayList<>();
                        try
                        {
                            listeclasses = Infoclass.recupclasses();
                        }
                        catch(Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                        
                        out.println("<ul>Liste des classes");
                        for(Class<?> classe : listeclasses)
                        {
                            out.println("<li>" + classe.getName());
                                out.println("<ul>Liste des methodes");
                                Method[] listemethodes = Infoclass.recupmethods(classe);
                                for(Method methode : listemethodes)
                                {
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
                        for(String key : hashmap.keySet())
                        {
                            out.println("<li> Cle " + key + " Valeur mapping : classname = " + hashmap.get(key).getClassName() + " , methode = " + hashmap.get(key).getMethod());
                            out.println("</li>");

                        }
                        out.println("</ul>");

                        out.println("</body>");
                        out.println("</html>");
                    }
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.println("Exception Printwriter " + e.getMessage());
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
