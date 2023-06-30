/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1381.framework.model;

import java.util.ArrayList;

import etu1381.framework.annotation.Scope;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.modelview.ModelView;

/**
 *
 * @author zexceed
 */
@Scope("singleton")
public class Departement extends Resetable {
    private int id;
    private String nom;
    
    public Departement(int id, String nom) {
        this.setId(id);
        this.setNom(nom);
    }

    public Departement(String id, String nom)
    {
        this.setId(Integer.parseInt(id));
        this.setNom(nom);
    }

    public Departement(ArrayList<String> attributsparametres)
    {
        this.setId(Integer.parseInt(attributsparametres.get(0)));
        this.setNom(attributsparametres.get(1));

    }

    public Departement() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    @URLAnnotation("/urlsalutdepartement")
    public ModelView getsalutdepartment()
    {
        return new ModelView("SalutDept");
    }    

    @URLAnnotation("/urlnbdepartements")
    public ModelView getnbdepartements()
    {
        return new ModelView("NbDept");
    }
}
