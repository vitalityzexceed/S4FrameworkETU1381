/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1381.framework.model;

import etu1381.framework.annotation.URLAnnotation;

/**
 *
 * @author zexceed
 */
public class Departement {
    private int id;
    private String nom;
    
    public Departement(int id, String nom) {
        this.setId(id);
        this.setNom(nom);
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
    public String getsalutdepartment()
    {
        return "salut";
    }    

    @URLAnnotation("/urlnbdepartements")
    public int getnbdepartements()
    {
        return 10;
    }
}
