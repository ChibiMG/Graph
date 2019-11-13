package garcon.maud.graphe;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
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

        //dessiner les noeuds du graphe
        for (Node noeud : graphe.getNoeuds()) {
            //param de dessin du noeud
            Paint paint = new Paint();
            //si le noeud est selectionne (pour faire un arc)
            if (noeud.isSelected()){
                //on lui ajoute une ombre bleue
                paint.setShadowLayer(20.0f, 0.0f, 0.0f, 0xFF0000FF);
            }
            //dans le cas normal on cree des noeuds sans ombre
            canvas.drawRoundRect( noeud.getX(), noeud.getY(), noeud.getX() + noeud.getTailleNoeud(), noeud.getY()+noeud.getTailleNoeud(), noeud.getTailleNoeud()/2, noeud.getTailleNoeud()/2, paint);
        }

        //dessiner les arcs du graphe
        for (Arc arc : graphe.getArcs()){
            //dessiner les arcs avec des lignes
            canvas.drawLine(
                    //On recupere les positions du noeud de depart...
                    arc.getNoeudDepart().getX()+ arc.getNoeudDepart().getTailleNoeud()/2,
                    arc.getNoeudDepart().getY()+ arc.getNoeudDepart().getTailleNoeud()/2,
                    //...et ceux du noeud d'arrive
                    arc.getNoeudArrive().getX()+ arc.getNoeudArrive().getTailleNoeud()/2,
                    arc.getNoeudArrive().getY()+ arc.getNoeudArrive().getTailleNoeud()/2,
                    //on trace l'arc
                    arc.getPaint()
            );
        }
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    //gere la couleur du noeud
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    //gere l'opacite du noeud 
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
