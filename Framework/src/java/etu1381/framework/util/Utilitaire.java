package etu1381.framework.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import etu1381.framework.annotation.Auth;
import etu1381.framework.annotation.OnlyJSON;
import etu1381.framework.modelview.ModelView;


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
