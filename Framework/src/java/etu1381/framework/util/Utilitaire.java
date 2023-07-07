package etu1381.framework.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import etu1381.framework.annotation.Auth;
import etu1381.framework.annotation.OnlyJSON;
import etu1381.framework.modelview.ModelView;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Utilitaire 
{
    public static int checkMethod(Method method_to_test, String required_profile)    
    {
        // 0 non autorise
        // 1 autorise
        // 10 annotation non presente

        if (method_to_test.isAnnotationPresent(Auth.class)) {
            Auth authannotation = method_to_test.getAnnotation(Auth.class);
            String profil = authannotation.profil();
            if (profil.equals(required_profile)) {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 10;
        }
    }

    public static int checkJSON(Method method_to_test)
    {
        // 0 tsy mitady JSON
        // 1 mitady JSON simple
        // 2 mitady JSON array
        // 10 annotation non presente

        if (method_to_test.isAnnotationPresent(OnlyJSON.class)) {
            OnlyJSON jsonannotation = method_to_test.getAnnotation(OnlyJSON.class);
            String profil = jsonannotation.value();
            if (profil.equals("array")) {
                return 2;
            }
            else if (profil.equals("simple")) {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 10;
        }
    }

    public static void removeAllSessions(HttpServletRequest request, HttpServletResponse response, String urlredirect) throws IllegalStateException, Exception
    {
        HttpSession httpsession = request.getSession();
        try {
            httpsession.invalidate();
            response.setContentType("text/html");
            response.getWriter().println("Logout success, Redirecting to the front page ...");
            response.setHeader("Refresh", "5; URL="+urlredirect);
        } catch (IllegalStateException ise) {
            System.out.println("Efa vide ilay HttpSession");
            ise.printStackTrace();
            throw ise;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static void removesession(ArrayList<String> sessionsToRemove, HttpServletRequest request) throws IllegalStateException, Exception
    {
        HttpSession httpsession = request.getSession();
        for (String session : sessionsToRemove) {
            try {
                httpsession.removeAttribute(session);
            }
            catch(IllegalStateException ise) 
            {
                System.out.println("Efa tsy bound @ HttpSession intsony ilay session tiana ho fafana");
                ise.printStackTrace();
                throw ise;
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
    // public HashMap<String, Object> bypassinvokenomodelview(Object dataretourmethode)
    // {
    //     HashMap<String, Object> hashmap = new HashMap<String, Object>();
    //     ModelView mv = new ModelView();
    //     mv.setSessiontoadd(new HashMap<String, Object>());
    //     hashmap.put("dataretourmethode", dataretourmethode);
    //     hashmap.put("bypassingmodelview", mv);
    //     return hashmap;
    // }
}
