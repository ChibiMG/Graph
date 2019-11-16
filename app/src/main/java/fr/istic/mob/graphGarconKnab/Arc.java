package fr.istic.mob.graphGarconKnab;

import android.graphics.Paint;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;

/**
 * Created by Maud Garçon & Saly Knab
 */

public class Arc {
    // Noeud de depart de l'arc
    private Node noeudDepart;
    // Noeud d'arrive de l'arc
    private Node noeudArrive;
    //Nom de l'arc
    private String nom;

    //parametres de dessin pour l'arc
    private Paint paint = new Paint();

    //constructeur de l'arc
    public Arc(Node noeudDepart, Node noeudArrive) {
        //definition du noeud de depart et d'arrive
        this.noeudDepart = noeudDepart;
        this.noeudArrive = noeudArrive;
        //definition du nom de l'arc
        this.nom = "";

        //on doit definir une taille et un style dans le paint pour que l'arc soit visible
        paint.setStrokeWidth(6F);
        paint.setStyle(Paint.Style.STROKE);
    }

    // getter et setter -----------------------------------------

    public Node getNoeudDepart() {
        return noeudDepart;
    }

    public Node getNoeudArrive() {
        return noeudArrive;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Distance entre l'arc et un point
     * @param x ordonnee du point
     * @param y absice du point
     * @return
     */
    public double distance(float x, float y) {
        // Coef dir de la droite
        float a = (noeudArrive.getY() - noeudDepart.getY()) / (noeudArrive.getX() - noeudDepart.getX());
        // Ordonnée à l'origine
        float b = noeudArrive.getY() + noeudArrive.getTailleNoeud()/2 - a * (noeudArrive.getX() + noeudArrive.getTailleNoeud()/2);
        // Valeur de la droite en x
        float yDroite = a * x + b;

        // Distance entre le point et la droite
        return abs(y - yDroite) * cos(atan(a));
    }

    //------------------------------------------------------------
}
