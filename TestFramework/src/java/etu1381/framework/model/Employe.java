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
public class Employe {
    private int id;
    private String nom;
    private int age;
    private boolean engaged;
    
    public Employe(int id, String nom, int age, boolean engaged) {
        this.setId(id);
        this.setNom(nom);
        this.setAge(age);
        this.setEngaged(engaged);
    }

    @URLAnnotation("/urlemployerehetra")
    public Employe[] getall()
    {
        Employe[] tabemployes = new Employe[3];
        tabemployes[0] = new Employe(1, "Jean", 22, true);
        tabemployes[1] = new Employe(2, "Jacques", 31, false);
        tabemployes[2] = new Employe(3, "Soa", 25, false);
        return tabemployes;
    }

    @URLAnnotation("/urlemployeparid")
    public Employe getempbyid(int id)
    {
        Employe[] tabemployes = new Employe[3];
        tabemployes[0] = new Employe(1, "Jean", 22, true);
        tabemployes[1] = new Employe(2, "Jacques", 31, false);
        tabemployes[2] = new Employe(3, "Soa", 25, false);

        return tabemployes[1];
    }

    @URLAnnotation("/urlcoucouemploye")
    public String getcoucou()
    {
        return "coucou";
    }

    public Employe()
    {}

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public boolean isEngaged() {
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
    
}
