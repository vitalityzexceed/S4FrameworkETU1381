/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1381.framework.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.rmi.CORBA.Util;
import javax.servlet.RequestDispatcher;
import java.util.List;
import java.util.Map;

import etu1381.framework.Mapping;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.file.FileUpload;
import etu1381.framework.init.Infoclass;
import etu1381.framework.modelview.ModelView;
import etu1381.framework.util.Utilitaire;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import com.google.gson.Gson;

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

    private HashMap<String, Mapping> hashmap = new HashMap<>();
    private HashMap<Class<?>, Object> hashmapsingleton = new HashMap<>();
    private HttpSession session = null;
    private String profilactuel = null;
    Enumeration<String> sessionAttributeNames = null;


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
                System.out.println("Exception sur split url ");
                e.printStackTrace();
            }
            this.setSession(request.getSession());
            

            //Sprint 5 : test sy mitovy ilay URL sur navigateur sy ny iray amin'ilay key hashmap
            Mapping matchedmapping = null;
            int indice = 0;
            Enumeration<String> nomsinput = request.getParameterNames();
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
                        //get the Parameters of the method
                        Parameter[] parametresmethode = matchedmethod.getParameters();
                        // create an array of arguments to pass to the method
                        // Object[] arguments = new Object[parameterTypes.length];
                        ArrayList<Object> arguments = new ArrayList<>();
                        // set the arguments to appropriate values (in this case, null)
                        //Sprint 10 : singleton
                        HashMap<Class<?>, Object> hashmapsingleton = null;
                        try {
                            hashmapsingleton = this.getHashmapsingleton();
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Sprint 7 : raha tsy misy arguments ilay methode
                        if(parametresmethode.length==0)
                        {
                            System.out.println("Mandalo arguments null");
                            System.out.println("Nombre d'arguments de la methode : " + arguments.size());
                            //atao dynamique ilay invoke
                            
                            try {
                                try {
                                    //reset attributes
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                    
                                } catch (Exception inve) {
                                    inve.printStackTrace();
                                    System.out.println("Tsy Mandalo singleton");
                                    
                                    returnedmodelview = (ModelView)matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments.toArray());
                                    //Sprint 11 : authentification
                                    this.setProfilactuel("no_value");
                                    if (returnedmodelview.getSessiontoadd() != null) {
                                        hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    }
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                    {
                                        //raha misy annotation auth
                                        if (returnedmodelview.getSessiontoadd() != null) {
                                            hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                        }
                                        if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                        this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                        if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                    }
                                    //raha tsisy annotation auth dia tsy mila authentification
                                    //Sprint 11 : authentification
                                    
                                }
                                //raha ilay classe singleton no antsoina
                                System.out.println("Mandalo singleton");
                                
                                returnedmodelview = (ModelView)matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                //Sprint 11 : authentification
                                this.setProfilactuel("no_value");
                                if (returnedmodelview.getSessiontoadd() != null) {
                                    hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                }
                                if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                {
                                    //raha misy annotation auth
                                    if (returnedmodelview.getSessiontoadd() != null) {
                                        hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    }
                                    if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                    this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                    
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                }
                                //Sprint 11 : authentification

                            } catch (Exception e) {
                                //raha tsy ilay classe singleton no antsoina
                                e.printStackTrace();
                               }
                        }
                        //Sprint 8 : raha misy arguments ilay methode
                        else
                        {
                            for(Parameter parametremethode : parametresmethode)
                            {
                                System.out.println("Argument actuel : " + parametremethode.getName());
                                while(nomsinput.hasMoreElements())
                                {
                                    String nominput = nomsinput.nextElement();
                                    String valeurrecuperee = request.getParameter(nominput);
                                    System.out.println("Valeur recupere from input : " + valeurrecuperee);
                                    if (nominput.equals(parametremethode.getName())) 
                                    {
                                        try {
                                            arguments.add(parametremethode.getType().getConstructor(String.class).newInstance(valeurrecuperee));
                                        }
                                        catch(InvocationTargetException inve) 
                                        {
                                            inve.printStackTrace();
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                nomsinput = request.getParameterNames();
                            }
                            System.out.println("Taille arguments : " + arguments.size());
                            for (Object argument : arguments) 
                            {
                                System.out.println("type argument : " + argument.getClass().getName()); 
                                System.out.println("Vakeur argument " + argument.toString());
                            }

                            System.out.println("Nombre d'arguments de la methode : " + arguments.size());
                            
                            //atao dynamique ilay invoke
                            
                            //Sprint 10
                            try {
                                //raha ilay classe singleton no antsoina
                                try {
                                    //reset attributes
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                } catch (Exception inve) {
                                    inve.printStackTrace();
                                    System.out.println("Tsy Mandalo singleton");
                                    returnedmodelview = (ModelView)matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments.toArray());
                                    //Sprint 11 : authentification
                                    this.setProfilactuel("no_value");
                                    if (returnedmodelview.getSessiontoadd() != null) {
                                        hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    }
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                    {
                                        //raha misy annotation auth
                                        if (returnedmodelview.getSessiontoadd() != null) {
                                            hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                        }
                                        if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                        {
                                 
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                        this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                        
                                        if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                    }
                                    //Sprint 11 : authentification

                                }
                                System.out.println("Mandalo singleton");
                                returnedmodelview = (ModelView)matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), arguments.toArray());
                                //Sprint 11 : authentification
                                this.setProfilactuel("no_value");
                                if (returnedmodelview.getSessiontoadd() != null) {
                                    hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                }
                                if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                {
                                    //raha misy annotation auth
                                    if (returnedmodelview.getSessiontoadd() != null) {
                                        hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    }
                                    if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                    {
                                        System.out.println("Session Null");
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                    this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                    
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                }
                                //Sprint 11 : authentification
                            } catch (Exception e) {
                                //raha tsy ilay classe singleton no antsoina
                                e.printStackTrace();
                            }
                        }
                        
                        //out.println("Mi retourne modelView => jsp : " + returnedmodelview.getView());
                        //Sprint 6 : avant de dispatch, mettre des informations dans la requete
                        //test de valeurs dans l'attribut data
                        
                        try {
                            returnedmodelview.addItem("Nom", new String("Rakoto"));
                            returnedmodelview.addItem("Prenom", new String("Jean"));
                        } catch (NullPointerException nullex) {
                            returnedmodelview.setData(new HashMap<String, Object>());
                            returnedmodelview.addItem("Nom", new String("Rakoto"));
                            returnedmodelview.addItem("Prenom", new String("Jean"));
                        }
                        
                        //test de valeurs dans l'attribut data

                        //Sprint7 : maka donnees avy any anaty formulaire dia mi-save objet dia mi rediriger ao amle page teo ihany
                        Field[] attributs = Class.forName(matchedmapping.getClassName()).getDeclaredFields();
                        ArrayList<String> attributsrecuperes = new ArrayList<>();
                        HashMap<String, Class<?>> nomettypeattributs = new HashMap<>();
                        Object objecttosave = null;
                        
                        // Sprint 8 : raha ohatra ka misy parametres ilay methode sy ilay URL
                        try
                        {
                            //Sprint 10
                            try {
                                //raha ilay classe singleton no antsoina
                                try {
                                    //reset attributes
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                } catch (Exception inve) {
                                    inve.printStackTrace();
                                    System.out.println("Tsy Mandalo singleton");
                                    objecttosave = Class.forName(matchedmapping.getClassName()).getConstructor().newInstance();

                                    //sprint 12 : injection sessions dans modele
                                    sessionAttributeNames = this.getSession().getAttributeNames();
                                    HashMap<String, Object> sessionsmodeles = new HashMap<>();
                                    while (sessionAttributeNames.hasMoreElements()) {
                                        String attributeName = sessionAttributeNames.nextElement();
                                        Object attributeValue = this.getSession().getAttribute(attributeName);
                                        sessionsmodeles.put(attributeName, attributeValue);
                                        // Do something with the attributeName and attributeValue
                                    }
                                    try {
                                        objecttosave.getClass().getMethod("setSession", HashMap.class).invoke(objecttosave, sessionsmodeles);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    sessionAttributeNames = this.getSession().getAttributeNames();
                                    //sprint 12 : injection sessions dans modele
                                    
                                }
                                //Sprint 7
                                System.out.println("Mandalo singleton");

                                //sprint 12 : injection sessions dans modele
                                sessionAttributeNames = this.getSession().getAttributeNames();
                                HashMap<String, Object> sessionsmodeles = new HashMap<>();
                                while (sessionAttributeNames.hasMoreElements()) {
                                    String attributeName = sessionAttributeNames.nextElement();
                                    Object attributeValue = this.getSession().getAttribute(attributeName);
                                    sessionsmodeles.put(attributeName, attributeValue);
                                    // Do something with the attributeName and attributeValue
                                }
                                try {
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("setSession", HashMap.class).invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), sessionsmodeles);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sessionAttributeNames = this.getSession().getAttributeNames();
                                //sprint 12 : injection sessions dans modele

                                objecttosave = hashmapsingleton.get(Class.forName(matchedmapping.getClassName()));

                            } catch (Exception e) {
                                //raha tsy ilay classe singleton no antsoina
                                e.printStackTrace();
                            }
                        }
                        catch(Exception e)
                        {
                            System.out.println("Erreur sur l'instanciation de l'objet a save : ");
                            e.printStackTrace();
                        }

                        //Sprint 7
                        for (Field attribut : attributs)
                        {
                            System.out.println("Attribut : " + attribut.getName());
                            System.out.println("Type Attribut : " + attribut.getType());

                            nomettypeattributs.put(attribut.getName(), attribut.getType());
                        }
                        for (String nomattribut : nomettypeattributs.keySet())
                        {
                            System.out.println("nom attribut : " + nomattribut);
                            System.out.println("type attribut : " + nomettypeattributs.get(nomattribut).toString());
                            //raha tsy mifanaraka amin'ilay Sprint 7 ilay ataon' ny utilisateur dia tonga dia miala
                            if (request.getParameter(nomattribut) == null) 
                            {
                                break;
                            }
                            attributsrecuperes.add(request.getParameter(nomattribut));

                            while(nomsinput.hasMoreElements())
                            {
                                String actualcursor = nomsinput.nextElement();
                                System.out.println("Actual cursor : " + actualcursor);
                                if (actualcursor.equals(nomattribut)) {
                                    
                                    //Sprint 7
                                    //miantso an'ilay set raha ohatra ka mitovy ilay izy
                                    System.out.println("Mandalo ato");
                                    try {
                                        //raha booleen da castena
                                        if (nomettypeattributs.get(nomattribut).toString().equals("boolean")) {
                                            System.out.println("Mandalo booleen");
                                            objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, Boolean.parseBoolean(request.getParameter(nomattribut)));
                                        }
                                        else if (nomettypeattributs.get(nomattribut).toString().equals("int")) {
                                            System.out.println("Mandalo int");
                                            objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, Integer.parseInt(request.getParameter(nomattribut)));
                                        }
                                        else
                                        {
                                            System.out.println("Tsy mandalo booleen");
                                            objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, request.getParameter(nomattribut));
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Exception sur l'appel du setter ");
                                        e.printStackTrace(); 
                                    }
                                }
                            }
                            nomsinput=request.getParameterNames();
                        }

                        
                        //Sprint7
                        
                        //Sprint 9 : traitement des upload fichiers
                        String contentType = request.getContentType();
                        if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
                            System.out.println("Mandalo traitement fichier");
                            FileUpload file = new FileUpload();
                            for (Part filepart : request.getParts()) {
                                try {
                                    file.setName(Paths.get(filepart.getSubmittedFileName()).getFileName().toString());
                                    InputStream is = filepart.getInputStream();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[4096];
                                    int bytesRead;
                                    while((bytesRead = is.read(buffer)) != -1)
                                    {
                                        baos.write(buffer, 0, bytesRead);
                                    }
                                    file.setBytearray(baos.toByteArray());
                                    System.out.println("taille byte array : " + file.getBytearray().length);
                                    returnedmodelview.addItem("tableaubyte", file.getBytearray().length);
                                    returnedmodelview.addItem("nomfichier", file.getName());
                                }
                                catch (InvalidPathException ipathe) 
                                {
                                    ipathe.printStackTrace();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        
                        for (String attributrecupere : attributsrecuperes) {
                            System.out.println("Attribut recupere : " + attributrecupere);
                            
                        }
                        
                        //alefa any amle JSP ilay objet ho jerena hoe tena nety ve ilay save
                        returnedmodelview.addItem("objet", objecttosave);

                        //Sprint 13 : change mv data to JSON if isJson is true
                        String jsoneddata = null;
                        if (returnedmodelview.isJson()) {
                            Gson gson = new Gson();
                            jsoneddata = gson.toJson(returnedmodelview.getData());
                            request.setAttribute("dataJSON", jsoneddata);
                        }
                        //Sprint 13 : change mv data to JSON if isJson is true
                        else{
                            for (String cledata : returnedmodelview.getData().keySet()) {
                                request.setAttribute(cledata, returnedmodelview.getData().get(cledata));
                            }
                        }

                        
                        // RequestDispatcher dispat = request.getRequestDispatcher(""+returnedmodelview.getView()+".jsp");
                        RequestDispatcher dispat = request.getRequestDispatcher(""+returnedmodelview.getView()+"");
                        dispat.forward(request,response);
                    }
                    else
                    {
                        //Sprint 14 : fonction tsy mi retourne ModelView fa JSON
                        ArrayList<Object> listeobjet = new ArrayList<>();
                        
                        HashMap<String, Object> hashmapdatarequete = null;
                        
                        //get the Parameters of the method
                        Parameter[] parametresmethode = matchedmethod.getParameters();
                        // create an array of arguments to pass to the method
                        // Object[] arguments = new Object[parameterTypes.length];
                        ArrayList<Object> arguments = new ArrayList<>();
                        // set the arguments to appropriate values (in this case, null)
                        //singleton
                        HashMap<Class<?>, Object> hashmapsingleton = null;
                        try {
                            hashmapsingleton = this.getHashmapsingleton();
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        Field[] attributs = Class.forName(matchedmapping.getClassName()).getDeclaredFields();
                        ArrayList<String> attributsrecuperes = new ArrayList<>();
                        HashMap<String, Class<?>> nomettypeattributs = new HashMap<>();
                        // Object objecttosave = null;

                        Object resultMethod = null;

                        // raha tsy misy arguments ilay methode
                        if(parametresmethode.length==0)
                        {
                            System.out.println("Mandalo arguments null");
                            System.out.println("Nombre d'arguments de la methode : " + arguments.size());
                            //atao dynamique ilay invoke
                            
                            try {
                                try {
                                    //reset attributes
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                    
                                } catch (Exception inve) {
                                    inve.printStackTrace();
                                    System.out.println("Tsy Mandalo singleton");
                                    
                                    resultMethod = matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments.toArray());
                                    //authentification
                                    this.setProfilactuel("no_value");
                                    
                                    // if (returnedmodelview.getSessiontoadd() != null) {
                                    //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    // }
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                    {
                                        //raha misy annotation auth
                                        // if (returnedmodelview.getSessiontoadd() != null) {
                                        //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                        // }
                                        if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                        this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                        if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                    }
                                    //raha tsisy annotation auth dia tsy mila authentification
                                    //authentification
                                    
                                }
                                //raha ilay classe singleton no antsoina
                                System.out.println("Mandalo singleton");
                                
                                // returnedmodelview = (ModelView)matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                resultMethod = matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), arguments.toArray());

                                //authentification
                                this.setProfilactuel("no_value");
                                // if (returnedmodelview.getSessiontoadd() != null) {
                                //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                // }
                                if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                {
                                    //raha misy annotation auth
                                    // if (returnedmodelview.getSessiontoadd() != null) {
                                    //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    // }
                                    if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                    this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                    
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                }
                                // authentification

                            } catch (Exception e) {
                                //raha tsy ilay classe singleton no antsoina
                                e.printStackTrace();
                               }
                        }
                        //raha misy arguments ilay methode
                        else
                        {
                            for(Parameter parametremethode : parametresmethode)
                            {
                                System.out.println("Argument actuel : " + parametremethode.getName());
                                while(nomsinput.hasMoreElements())
                                {
                                    String nominput = nomsinput.nextElement();
                                    String valeurrecuperee = request.getParameter(nominput);
                                    System.out.println("Valeur recupere from input : " + valeurrecuperee);
                                    if (nominput.equals(parametremethode.getName())) 
                                    {
                                        try {
                                            arguments.add(parametremethode.getType().getConstructor(String.class).newInstance(valeurrecuperee));
                                        }
                                        catch(InvocationTargetException inve) 
                                        {
                                            inve.printStackTrace();
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                nomsinput = request.getParameterNames();
                            }
                            System.out.println("Taille arguments : " + arguments.size());
                            for (Object argument : arguments) 
                            {
                                System.out.println("type argument : " + argument.getClass().getName()); 
                                System.out.println("Vakeur argument " + argument.toString());
                            }

                            System.out.println("Nombre d'arguments de la methode : " + arguments.size());
                            
                            //atao dynamique ilay invoke
                            
                            //
                            try {
                                //raha ilay classe singleton no antsoina
                                try {
                                    //reset attributes
                                    hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                                } catch (Exception inve) {
                                    inve.printStackTrace();
                                    System.out.println("Tsy Mandalo singleton");
                                    // returnedmodelview = (ModelView)matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments.toArray());
                                    resultMethod = matchedmethod.invoke(Class.forName(matchedmapping.getClassName()).newInstance(), arguments.toArray());

                                    //Sprint 11 : authentification
                                    this.setProfilactuel("no_value");
                                    // if (returnedmodelview.getSessiontoadd() != null) {
                                    //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    // }
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                    {
                                        //raha misy annotation auth
                                        // if (returnedmodelview.getSessiontoadd() != null) {
                                        //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                        // }
                                        if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                        this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                        
                                        if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                        {
                                            response.sendRedirect("ErrorAuth.jsp");
                                        }
                                    }
                                    //authentification

                                }
                                System.out.println("Mandalo singleton");
                                // returnedmodelview = (ModelView)matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), arguments.toArray());
                                resultMethod = matchedmethod.invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), arguments.toArray());

                                // authentification
                                this.setProfilactuel("no_value");
                                // if (returnedmodelview.getSessiontoadd() != null) {
                                //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                // }
                                if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) != 10) 
                                {
                                    //raha misy annotation auth
                                    // if (returnedmodelview.getSessiontoadd() != null) {
                                    //     hydrateSession(returnedmodelview.getSessiontoadd(), request);
                                    // }
                                    if (this.getSession().getAttribute(getInitParameter("profil")) == null) 
                                    {
                                        System.out.println("Session Null");
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                    this.setProfilactuel(this.getSession().getAttribute(getInitParameter("profil")).toString());
                                    
                                    if (Utilitaire.checkMethod(matchedmethod, this.getProfilactuel()) == 0) 
                                    {
                                        response.sendRedirect("ErrorAuth.jsp");
                                    }
                                }
                                //authentification
                            } catch (Exception e) {
                                //raha tsy ilay classe singleton no antsoina
                                e.printStackTrace();
                            }
                        }
                        
                        // //  raha ohatra ka misy parametres ilay methode sy ilay URL
                        // try
                        // {
                        //     //
                        //     try {
                        //         //raha ilay classe singleton no antsoina
                        //         try {
                        //             //reset attributes
                        //             hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("resetAttributes").invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())));
                        //         } catch (Exception inve) {
                        //             inve.printStackTrace();
                        //             System.out.println("Tsy Mandalo singleton");
                        //             objecttosave = Class.forName(matchedmapping.getClassName()).getConstructor().newInstance();

                        //             // injection sessions dans modele
                        //             sessionAttributeNames = this.getSession().getAttributeNames();
                        //             HashMap<String, Object> sessionsmodeles = new HashMap<>();
                        //             while (sessionAttributeNames.hasMoreElements()) {
                        //                 String attributeName = sessionAttributeNames.nextElement();
                        //                 Object attributeValue = this.getSession().getAttribute(attributeName);
                        //                 sessionsmodeles.put(attributeName, attributeValue);
                        //                 // Do something with the attributeName and attributeValue
                        //             }
                        //             try {
                        //                 objecttosave.getClass().getMethod("setSession", HashMap.class).invoke(objecttosave, sessionsmodeles);
                        //             } catch (Exception e) {
                        //                 e.printStackTrace();
                        //             }
                        //             sessionAttributeNames = this.getSession().getAttributeNames();
                        //             //injection sessions dans modele
                                    
                        //         }
                        //         //Sprint 7
                        //         System.out.println("Mandalo singleton");

                        //         //injection sessions dans modele
                        //         sessionAttributeNames = this.getSession().getAttributeNames();
                        //         HashMap<String, Object> sessionsmodeles = new HashMap<>();
                        //         while (sessionAttributeNames.hasMoreElements()) {
                        //             String attributeName = sessionAttributeNames.nextElement();
                        //             Object attributeValue = this.getSession().getAttribute(attributeName);
                        //             sessionsmodeles.put(attributeName, attributeValue);
                        //             // Do something with the attributeName and attributeValue
                        //         }
                        //         try {
                        //             hashmapsingleton.get(Class.forName(matchedmapping.getClassName())).getClass().getMethod("setSession", HashMap.class).invoke(hashmapsingleton.get(Class.forName(matchedmapping.getClassName())), sessionsmodeles);
                        //         } catch (Exception e) {
                        //             e.printStackTrace();
                        //         }
                        //         sessionAttributeNames = this.getSession().getAttributeNames();
                        //         //injection sessions dans modele

                        //         objecttosave = hashmapsingleton.get(Class.forName(matchedmapping.getClassName()));

                        //     } catch (Exception e) {
                        //         //raha tsy ilay classe singleton no antsoina
                        //         e.printStackTrace();
                        //     }
                        // }
                        // catch(Exception e)
                        // {
                        //     System.out.println("Erreur sur l'instanciation de l'objet a save : ");
                        //     e.printStackTrace();
                        // }

                        //
                        // for (Field attribut : attributs)
                        // {
                        //     System.out.println("Attribut : " + attribut.getName());
                        //     System.out.println("Type Attribut : " + attribut.getType());

                        //     nomettypeattributs.put(attribut.getName(), attribut.getType());
                        // }
                        // for (String nomattribut : nomettypeattributs.keySet())
                        // {
                        //     System.out.println("nom attribut : " + nomattribut);
                        //     System.out.println("type attribut : " + nomettypeattributs.get(nomattribut).toString());
                        //     //raha tsy mifanaraka amin'ilay Sprint 7 ilay ataon' ny utilisateur dia tonga dia miala
                        //     if (request.getParameter(nomattribut) == null) 
                        //     {
                        //         break;
                        //     }
                        //     attributsrecuperes.add(request.getParameter(nomattribut));

                        //     while(nomsinput.hasMoreElements())
                        //     {
                        //         String actualcursor = nomsinput.nextElement();
                        //         System.out.println("Actual cursor : " + actualcursor);
                        //         if (actualcursor.equals(nomattribut)) {
                                    
                        //             //
                        //             //miantso an'ilay set raha ohatra ka mitovy ilay izy
                        //             System.out.println("Mandalo ato");
                        //             try {
                        //                 //raha booleen da castena
                        //                 if (nomettypeattributs.get(nomattribut).toString().equals("boolean")) {
                        //                     System.out.println("Mandalo booleen");
                        //                     objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, Boolean.parseBoolean(request.getParameter(nomattribut)));
                        //                 }
                        //                 else if (nomettypeattributs.get(nomattribut).toString().equals("int")) {
                        //                     System.out.println("Mandalo int");
                        //                     objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, Integer.parseInt(request.getParameter(nomattribut)));
                        //                 }
                        //                 else
                        //                 {
                        //                     System.out.println("Tsy mandalo booleen");
                        //                     objecttosave.getClass().getMethod("set"+Character.toUpperCase(nomattribut.toCharArray()[0])+nomattribut.substring(1), nomettypeattributs.get(nomattribut)).invoke(objecttosave, request.getParameter(nomattribut));
                        //                 }
                        //             } catch (Exception e) {
                        //                 System.out.println("Exception sur l'appel du setter ");
                        //                 e.printStackTrace(); 
                        //             }
                        //         }
                        //     }
                        //     nomsinput=request.getParameterNames();
                        // }

                        // try {
                        //     resultMethod = matchedmethod.invoke(objecttosave, arguments.toArray());
                        // } catch (Exception e) {
                        //     e.printStackTrace();
                        // }
                        
                        listeobjet.add(resultMethod);
                        // response.setContentType("application/json");
                        // out=response.getWriter();
                        if (Utilitaire.checkJSON(matchedmethod)!=10) 
                        {
                            out.println(arraytoJSON((listeobjet.toArray())));
                        }
                        else
                        {
                            out.println(listeobjet.toArray());

                        }
                        
                        out.println("tsy mi-retourne modelView");
                    }
                    break;
                }
                else
                {
                    //karazana page d'erreur fa tsy hoe makao foana ilay requete satria manjary maka an'ilay liste de classes foana
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
                            e.printStackTrace();
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
            System.out.println("Exception Printwriter ");
            e.printStackTrace();
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
        try
        {
            processRequest(request, response);
        }
        catch(Exception e)
        {
            System.out.println("Exception doGet : " + e.getMessage());
            e.printStackTrace();
        }
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
        try
        {
            processRequest(request, response);
        }
        catch(Exception e)
        {
            System.out.println("Exception doPost : " + e.getMessage());
            e.printStackTrace();
        }
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

    @Override
    public void init() throws ServletException
    {
        try {
            setHashmap(Infoclass.geturlannotationhashmap());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setHashmapsingleton(Infoclass.getscopehashmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hydrateSession(HashMap<String, Object> hashmapsession, HttpServletRequest request) throws ServletException
    {
        HttpSession session = this.getSession();
        for (Map.Entry<String, Object> entry : hashmapsession.entrySet()) 
        {
            String cle = entry.getKey();
            Object valeur = entry.getValue();
            if(session.getAttribute(cle) == null)
            {
                System.out.println("Tsy nisy tam voloany le sesssion");
                session.setAttribute(cle, valeur);
            }
        }
    }

    public String arraytoJSON(Object[] tabobjet)
    {
        Gson gson = new Gson();
        return gson.toJson(tabobjet);
    }

    public String objecttoJSON(Object objet)
    {
        Gson gson = new Gson();
        return gson.toJson(objet);
    }

    public HashMap<String, Mapping> getHashmap()
    {
        return this.hashmap;
    }

    public void setHashmap(HashMap<String, Mapping> hashmap)
    {
        this.hashmap = hashmap;
    }

    public HashMap<Class<?>, Object> getHashmapsingleton() {
        return hashmapsingleton;
    }

    public void setHashmapsingleton(HashMap<Class<?>, Object> hashmapsingleton) {
        this.hashmapsingleton = hashmapsingleton;
    }

    
    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
    
    public String getProfilactuel() {
        return profilactuel;
    }

    public void setProfilactuel(String profilactuel) {
        this.profilactuel = profilactuel;
    }

}
