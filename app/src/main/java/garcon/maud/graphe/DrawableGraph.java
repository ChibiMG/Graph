package garcon.maud.graphe;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Maud Garçon & Saly Knab on 10/10/2019.
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

        //dessiner les noeuds du graphe
        for (Node noeud : graphe.getNoeuds()) {
            //param de dessin du noeud
            Paint paintN = new Paint();
            Paint paintT = new Paint();

            //param dessin du texte
            paintT.setTextSize(noeud.getTailleNoeud()/3);
            paintT.setColor(0xFFFFFFFF);
            paintT.setShadowLayer(20.0f, 0.0f, 0.0f, 0xFF000000);

            //si le noeud est selectionné (pour faire un arc)
            if (noeud.isSelected()){
                //on lui ajoute une ombre bleue
                paintN.setShadowLayer(20.0f, 0.0f, 0.0f, 0xFF0000FF);
            }

            //dans le cas normal on créé des noeuds sans ombre
            canvas.drawRoundRect( noeud.getX(), noeud.getY(), noeud.getX() + noeud.getTailleNoeud(), noeud.getY()+noeud.getTailleNoeud(), noeud.getTailleNoeud()/2, noeud.getTailleNoeud()/2, paintN);

            //On créé les textes des noeuds
            canvas.drawText(noeud.getNom(),noeud.getX()+noeud.getTailleNoeud()/2,noeud.getY()+noeud.getTailleNoeud()/2,paintT);
        }

        //dessiner les arcs du graphe
        for (Arc arc : graphe.getArcs()){
            //dessiner les arcs avec des lignes
            canvas.drawLine(
                    arc.getNoeudDepart().getX()+ arc.getNoeudDepart().getTailleNoeud()/2,
                    arc.getNoeudDepart().getY()+ arc.getNoeudDepart().getTailleNoeud()/2,
                    arc.getNoeudArrive().getX()+ arc.getNoeudArrive().getTailleNoeud()/2,
                    arc.getNoeudArrive().getY()+ arc.getNoeudArrive().getTailleNoeud()/2,
                    arc.getPaint()
            );
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
