/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1381.framework.init;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import etu1381.framework.Mapping;
import etu1381.framework.annotation.Scope;
import etu1381.framework.annotation.URLAnnotation;

/**
 *
 * @author zexceed
 */
public class Infoclass {
    
    public static ArrayList<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        //System.out.println("url : " + resource.toString());
        File directory = new File(resource.getFile().replace("%20", " "));
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            String filename = file.getName();
            if (filename.endsWith(".class")) {
                String className = packageName + '.' + filename.substring(0, filename.length() - 6);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            } else if (file.isDirectory()) {
                List<Class<?>> subClasses = getClasses(packageName + "." + filename);
                classes.addAll(subClasses);
            }
        }
        return classes;
    }
    
    public static Method[] recupmethods(Class<?> clazz)
    {
        Method[] methods = clazz.getDeclaredMethods();
        return methods;
    }

    public static List<Class<?>> recupclasses() throws ClassNotFoundException
    {   
        List<Class<?>> listeclasses = new ArrayList<>();
        try
        {
            listeclasses = getClasses("etu1381.framework");
        }
        catch(Exception e)
        {
            System.out.println("Exception sur la recuperation des classes " + e.getMessage());
            throw e;
        }
        return listeclasses;
        // for (Class<?> c : classes) {
        //     System.out.println(c.getName());
        // }
    }

    public static String geturlmappedtoamethod(Method methode)
    {
        String url = "";
        if (methode.isAnnotationPresent(URLAnnotation.class)) {
            URLAnnotation annotation = methode.getAnnotation(URLAnnotation.class);
            url = annotation.value();
        }
        return url;
    }

    public static HashMap<String, Mapping> geturlannotationhashmap() throws Exception
    {
        HashMap<String, Mapping> hashmap = new HashMap<>();

        List<Class<?>> listeclasses = new ArrayList<>();
        try{
            listeclasses = recupclasses();
        }
        catch(Exception e)
        {
            System.out.println("Exception sur la recuperation des classes " + e.getMessage());
            throw e;
        }

        for(Class<?> classe : listeclasses)
        {
            Method[] listemethodes = Infoclass.recupmethods(classe);
            for(Method methode : listemethodes)
            {
                String url = "";
                if (methode.isAnnotationPresent(URLAnnotation.class)) {
                    URLAnnotation annotation = methode.getAnnotation(URLAnnotation.class);
                    url = annotation.value();
                    hashmap.put(url, new Mapping(classe.getName(), methode.getName()));
                }
            }
        }

        return hashmap;
    }

    public static HashMap<Class<?>, Object> getscopehashmap() throws Exception
    {
        HashMap<Class<?>, Object> hashmap = new HashMap<>();

        List<Class<?>> listeclasses = new ArrayList<>();
        try{
            listeclasses = recupclasses();
        }
        catch(Exception e)
        {
            System.out.println("Exception sur la recuperation des classes sur getscope" + e.getMessage());
            throw e;
        }

        for(Class<?> classe : listeclasses)
        {
            if (classe.isAnnotationPresent(Scope.class)) {
                hashmap.put(classe, classe.getConstructor().newInstance());
            }
        }

        return hashmap;
    }
}
