package etu1381.framework.util;

import java.lang.reflect.Method;

import etu1381.framework.annotation.Auth;

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
}
