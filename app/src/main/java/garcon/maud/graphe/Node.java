package garcon.maud.graphe;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Maud Garçon & Saly Knab on 10/10/2019.
 */

public class Node {
    //position du noeud
    private float x;
    private float y;
    //parametres de dessin pour le noeud
    private Paint paint = new Paint();
    //taille du noeud
    private int tailleNoeud;
    //nom du noeud
    private String nom;
    //si il est selectionne ou non
    private boolean isSelected;

    //constructeur
    public Node(float x, float y, String nom) {
        this.x = x;
        this.y = y;
        this.tailleNoeud = 100;
        this.nom = nom;
    }

    //getter et setter --------------------------------------------------------------------
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getTailleNoeud() {
        return tailleNoeud;
    }

    public void setTailleNoeud(int tailleNoeud) {
        this.tailleNoeud = tailleNoeud;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    //----------------------------------------------------------------------------------------


}
