/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1381.framework.model;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import etu1381.framework.annotation.Auth;
import etu1381.framework.annotation.OnlyJSON;
import etu1381.framework.annotation.Scope;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.file.FileUpload;
import etu1381.framework.modelview.ModelView;
import com.google.gson.Gson;

/**
 *
 * @author zexceed
 */
@Scope("singleton")
public class Employe extends Resetable{
    private int id;
    private String nom;
    private int age;
    private boolean engaged;
    private HashMap<String, Object> session;
    
    // private String descfichier;
    // private FileUpload file;
    
    // public FileUpload getFile() {
    //     return file;
    // }

    // public void setFile(FileUpload file) {
    //     this.file = file;
    // }

    public Employe(HashMap<String, Object> session) {
        this.setSession(session);
    }

    public Employe()
    {
    }

    public Employe(int id, String nom, int age, boolean engaged) {
        this.setId(id);
        this.setNom(nom);
        this.setAge(age);
        this.setEngaged(engaged);
    }

    public Employe(String id, String nom, String age, String engaged)
    {
        this.setId(Integer.parseInt(id));
        this.setNom(nom);
        this.setAge(Integer.parseInt(age));
        this.setEngaged(Boolean.parseBoolean(engaged));
    }

    public Employe(ArrayList<String> attributsparametres)
    {
        this.setId(Integer.parseInt(attributsparametres.get(0)));
        this.setNom(attributsparametres.get(1));
        this.setAge(Integer.parseInt(attributsparametres.get(2)));
        this.setEngaged(Boolean.parseBoolean(attributsparametres.get(3)));
    }

    @URLAnnotation("/urlemployerehetra")
    @Auth(profil="admin")
    public ModelView getall()
    {
        Employe[] tabemployes = new Employe[3];
        tabemployes[0] = new Employe(1, "Jean", 22, true);
        tabemployes[1] = new Employe(2, "Jacques", 31, false);
        tabemployes[2] = new Employe(3, "Soa", 25, false);
        return new ModelView("ListeEmp.jsp");
        // return tabemployes;
    }

    @URLAnnotation("/urlemployeparid")
    public ModelView getempbyid(Integer id)
    {
        Employe[] tabemployes = new Employe[3];
        tabemployes[0] = new Employe(1, "Jean", 22, true);
        tabemployes[1] = new Employe(2, "Jacques", 31, false);
        tabemployes[2] = new Employe(3, "Soa", 25, false);
        return new ModelView("SearchEmpId.jsp");

        // return tabemployes[1];
    }

    @URLAnnotation("/urlemployeparidetnom")
    public ModelView getempbyidandname(Integer id, String nom)
    {
        // Employe[] tabemployes = new Employe[3];
        // tabemployes[0] = new Employe(1, "Jean", 22, true);
        // tabemployes[1] = new Employe(2, "Jacques", 31, false);
        // tabemployes[2] = new Employe(3, "Soa", 25, false);
        ModelView mv = new ModelView("SearchEmp.jsp");
        try {
            mv.addItem("id_azo", id.intValue());
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("id_azo", id.intValue());
        }
        return mv;

        // return tabemployes[1];
    }

    @URLAnnotation("/urlfichieremp")
    public ModelView traitementfichieremp()
    {
        ModelView mv = new ModelView("ResultfichierEmp.jsp");
        return mv;
    }

    @URLAnnotation("/urlcoucouemploye")
    public String getcoucou()
    {
        return "coucou";
    }

    @URLAnnotation("/urlloginemp")
    public ModelView login(String profil)
    {
        ModelView mv = new ModelView("FormSprint11.jsp");
        mv.setSessiontoadd(new HashMap<String, Object>());
        mv.getSessiontoadd().put("profil", profil);
        return mv;
    }

    @URLAnnotation("/urladmin")
    @Auth(profil="admin")
    public ModelView adminfunction(String valeur)
    {
        ModelView mv = new ModelView("SuccessAuth.jsp");
        try {
            mv.addItem("nb_test", 2);
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("nb_test", 2);
        }
        return mv;
    }

    @URLAnnotation("/urlinterdit")
    @Auth(profil="compta")
    public ModelView forbiddenfunction(String valeur)
    {
        ModelView mv = new ModelView("SuccessAuth.jsp");
        try {
            mv.addItem("nb_test", 2);
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("nb_test", 2);
        }
        return mv;
    }

    @URLAnnotation("/urlinjection")
    @Auth(profil="admin")
    public ModelView injectfunction()
    {
        ModelView mv = new ModelView("ListeEmp.jsp");
        try {
            mv.addItem("nb_test", 2);
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("nb_test", 2);
        }
        return mv;
    }

    @URLAnnotation("/urltestjson")
    public ModelView testjson()
    {
        ModelView mv = new ModelView("ResultJSON.jsp");
        mv.setJson(true);
        try {
            mv.addItem("nb_test", 2);
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("nb_test", 2);
        }
        return mv;
        // return tabemployes[1];
    }

    @URLAnnotation("/urljsononly")
    @OnlyJSON(value = "array", view = ".jsp")
    public Employe[] empall()
    {
        // ModelView mv = new ModelView("OnlyJSON.jsp");
        Employe[] tabemployes = new Employe[3];
        tabemployes[0] = new Employe(1, "Jean", 22, true);
        tabemployes[1] = new Employe(2, "Jacques", 31, false);
        tabemployes[2] = new Employe(3, "Soa", 25, false);
        // try {
        //     mv.addItem("nb_test", 2);
        // } catch (NullPointerException nullex) {
        //     mv.setData(new HashMap<String, Object>());
        //     mv.addItem("nb_test", 2);
        // }
        
        return tabemployes;
        // return tabemployes[1];
    }

    @URLAnnotation("/URLlogoutemp")
    public ModelView logout()
    {
        ModelView mv = new ModelView("FormSprint11.jsp");
        mv.setData(new HashMap<String, Object>());
        mv.setInvalidateSession(true);
        return mv;
    }

    @URLAnnotation("/URLdelsession")
    public ModelView delsession(String[] FrameWorksessions)
    {
        ModelView mv = new ModelView("FormSprint11.jsp");
        ArrayList<String> listesession = new ArrayList<>();
        for (String sessionName : FrameWorksessions) 
        {
            listesession.add(sessionName);
        }
        mv.setSessionToRemove(listesession);
        mv.setData(new HashMap<String, Object>());
        return mv;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public boolean getEngaged() {
        return engaged;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }  

    
}
