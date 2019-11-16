package fr.istic.mob.graphGarconKnab;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Maud Garçon & Saly Knab
 */

public class DrawableGraph extends Drawable {

    //creation du graphe
    private Graph graphe;

    //constructeur
    public DrawableGraph(Graph graphe) {
        this.graphe = graphe;
    }


    // methode draw
    @Override
    public void draw(@NonNull Canvas canvas) {
        //dessiner les arcs du graphe
        Path path = new Path();
        for (Arc arc : graphe.getArcs()){
            Paint paintT = new Paint();
            path.reset();
            path.moveTo(arc.getNoeudDepart().getX()+ arc.getNoeudDepart().getTailleNoeud()/2,
                    arc.getNoeudDepart().getY()+ arc.getNoeudDepart().getTailleNoeud()/2);
            path.lineTo(arc.getNoeudArrive().getX()+ arc.getNoeudArrive().getTailleNoeud()/2,
                    arc.getNoeudArrive().getY()+ arc.getNoeudArrive().getTailleNoeud()/2);
            //dessiner les arcs avec des lignes
            canvas.drawPath(path, arc.getPaint());

            //param dessin du texte
            paintT.setTextSize(35);
            paintT.setColor(0xFF000000);

            //On créé les textes des noeuds
            canvas.drawText(arc.getNom(),(arc.getNoeudArrive().getX()+arc.getNoeudDepart().getX())/2, (arc.getNoeudArrive().getY()+arc.getNoeudDepart().getY())/2,paintT);
        }

        //dessiner les noeuds du graphe
        for (Node noeud : graphe.getNoeuds()) {
            //param de dessin du noeud
            Paint paintT = new Paint();

            //param dessin du texte
            paintT.setTextSize(noeud.getTailleNoeud()/3);
            paintT.setColor(0xFFFFFFFF);
            paintT.setShadowLayer(5.0f, 0.0f, 0.0f, 0xFF000000);

            //si le noeud est selectionné (pour faire un arc)
            if (noeud.isSelected()){
                //on lui ajoute une ombre bleue
                noeud.getPaint().setShadowLayer(20.0f, 0.0f, 0.0f, 0xFF0000FF);
            }

            //dans le cas normal on créé des noeuds sans ombre
            canvas.drawRoundRect( noeud.getX(), noeud.getY(), noeud.getX() + noeud.getTailleNoeud(), noeud.getY()+noeud.getTailleNoeud(), noeud.getTailleNoeud()/2, noeud.getTailleNoeud()/2, noeud.getPaint());
            noeud.getPaint().clearShadowLayer();
            //On créé les textes des noeuds
            canvas.drawText(noeud.getNom(),noeud.getX()+noeud.getTailleNoeud()/2 - paintT.measureText(noeud.getNom())/2,noeud.getY()+noeud.getTailleNoeud()/2 - (paintT.descent() + paintT.ascent())/2,paintT);
        }
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
