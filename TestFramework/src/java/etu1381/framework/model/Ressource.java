package etu1381.framework.model;

import java.util.ArrayList;
import java.util.HashMap;

import etu1381.framework.annotation.Scope;
import etu1381.framework.annotation.URLAnnotation;
import etu1381.framework.file.FileUpload;
import etu1381.framework.modelview.ModelView;


public class Ressource extends Resetable
{
    private String description;
    private double quantite;

    public Ressource(String description, double quantite) {
        this.setDescription(description);
        this.setQuantite(quantite);
    }
    public Ressource() {
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getQuantite() {
        return quantite;
    }
    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    @URLAnnotation("/urlressourcedescquantite")
    public ModelView ressmisyparam(String description, Double quantite)
    {
        // Employe[] tabemployes = new Employe[3];
        // tabemployes[0] = new Employe(1, "Jean", 22, true);
        // tabemployes[1] = new Employe(2, "Jacques", 31, false);
        // tabemployes[2] = new Employe(3, "Soa", 25, false);
        ModelView mv = new ModelView("Viewressource");
        try {
            mv.addItem("quantite_azo", quantite.doubleValue());
        } catch (NullPointerException nullex) {
            mv.setData(new HashMap<String, Object>());
            mv.addItem("quantite_azo", quantite.intValue());
        }
        return mv;

        // return tabemployes[1];
    }
    
}
