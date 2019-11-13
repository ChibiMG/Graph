package garcon.maud.graphe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Maud Garçon & Saly Knab on 10/10/2019.
 *
 * TODO : V1 edition en sous menu arc et noeud (lieste arc dans les noeuds)
 * TODO : V2 landscape (prise en compte rotation) + etiquette des arcs parfaitement dessiné + sauvegarde/import de graphe + changer la courbure de l'arc lors du touché + arc orienté (+ arc courbé) + arc en mode tiré
 */

public class MainActivity extends AppCompatActivity implements EditionMenuFragment.Listener {

    // Variables de vue du graphe
    private ImageView imageGraph;
    private DrawableGraph graphView;
    private Graph graphe;

    // Mode actuel
    private Mode mode;

    // Variable des modes
    private Node noeudSelec1;
    private Node noeudSelec2;

    private EditionMenuFragment editionMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialisation de la vue du graphe avec 9 noeuds
        graphe = new Graph(9);

        //on passe ça en drawable puis en imageview pour afficher une "image drawable"
        graphView = new DrawableGraph(graphe);
        imageGraph = findViewById(R.id.graph_image);
        imageGraph.setImageDrawable(graphView);

        //on met en mode normal qui est le mode de base
        mode = Mode.NORMAL;

        //on lie la fonction onTouch à ImageGraph pour que les coordonnées soient relatives à la zone de dessin et pas à l'activité principale
        imageGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                //on recupère les coordonnées
                float x = e.getX();
                float y = e.getY();

                switch (e.getAction()){
                    //action de toucher l'ecran
                    case MotionEvent.ACTION_DOWN:
                        for (Node noeud : graphe.getNoeuds()) {
                            //on cherche le noeud sur lequel on est
                            if (x >= noeud.getX() && x <= noeud.getX() + noeud.getTailleNoeud() && y >= noeud.getY() && y <= noeud.getY() + noeud.getTailleNoeud()) {
                                //suivant le mode :
                                switch (mode) {
                                    case NORMAL:
                                        //en mode normal on récupere le noeud pour le deplacer
                                        noeudSelec1 = noeud;
                                        break;
                                    case AJOUT_ARC:
                                        //en mode ajouter un arc on sélectionne 1 noeud puis un autre et on trace l'arc entre les 2
                                        if (noeudSelec1 == null){
                                            noeudSelec1 = noeud;
                                            //le noeud passe en selectionnée
                                            noeud.setSelected(true);
                                            //mettre a jouter la vue
                                            imageGraph.invalidate();
                                        }
                                        else {
                                            noeudSelec2 = noeud;
                                        }
                                        break;
                                    case EDIT_NOEUD:
                                        //en mode suppression de noeud on selectionne le noeud a supprimer
                                        noeudSelec1 = noeud;
                                }
                                //ajout d'un vibreur lorsqu'on touche un noeud
                                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vib.vibrate(100);
                                return true;
                            }
                        }
                        return false;
                    //action deplacement sur l'ecran
                    case MotionEvent.ACTION_MOVE:
                        //si on a un noeud selectionne et en mode normal
                        if (mode == Mode.NORMAL && noeudSelec1 != null){
                            //on deplace le noeud suivant les nouvelles coordonnees et il ne peut pas sortir de l'image view
                            if(y>noeudSelec1.getTailleNoeud()/2 && y<imageGraph.getMeasuredHeight()-noeudSelec1.getTailleNoeud()/2){
                                noeudSelec1.setY(y- noeudSelec1.getTailleNoeud()/2);
                            }
                            if (x>noeudSelec1.getTailleNoeud()/2 && x<imageGraph.getMeasuredWidth()-noeudSelec1.getTailleNoeud()/2){
                                noeudSelec1.setX(x- noeudSelec1.getTailleNoeud()/2);
                            }
                            //on met a jour l'affichage
                            imageGraph.invalidate();
                            return true;
                        }
                     //on enleve le touché
                    case MotionEvent.ACTION_UP:
                        switch (mode) {
                            case NORMAL:
                                //si mode normal le noeud selectionné redevient null
                                noeudSelec1 = null;
                                break;
                            case AJOUT_ARC:
                                //si mode ajout arc et noeudselec2 est non null
                                if (noeudSelec2 != null){
                                    //on ajoute l'arc
                                    graphe.ajouterArc(noeudSelec1, noeudSelec2);
                                    //on passe la selection du noeud a faux
                                    noeudSelec1.setSelected(false);
                                    //les noeuds redeviennent null
                                    noeudSelec1 = null;
                                    noeudSelec2 = null;
                                    //on repasse en mode normal
                                    mode = Mode.NORMAL;
                                    //on actualise la vue
                                    imageGraph.invalidate();
                                }
                                break;
                            case EDIT_NOEUD:
                                if (editionMenuFragment == null) {
                                    editionMenuFragment = EditionMenuFragment.newInstance(R.menu.menu_edit_node);
                                    editionMenuFragment.show(getSupportFragmentManager(), "Custom bottom sheet");
                                }
                                break;
                        }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            //cas du bouton ajouter noeud
            case R.id.action_ajouter :
                //appeler la methode
                graphe.ajouterNoeud();
                //rafraichir l'affichage
                imageGraph.invalidate();
                break;
            //cas du bouton reset du graphe
            case R.id.action_reset :
                //supprime tout les noeuds
                graphe.getNoeuds().clear();
                //supprime tout les arcs
                graphe.getArcs().clear();
                //rafraichir
                imageGraph.invalidate();
                break;
            //cas du bouton ajouter arc
            case R.id.action_ajouter_arc :
                //on change de mode en ajout arc
                mode = Mode.AJOUT_ARC;
                Toast.makeText(this,getText(R.string.select_two_nodes), Toast.LENGTH_LONG).show();
                break;
             //cas du bouton envoyer un mail
            case R.id.action_envoie :

                try{
                    //capturer le graphe dans un Bitmap
                    imageGraph.setDrawingCacheEnabled(true);
                    Bitmap bm = imageGraph.getDrawingCache();

                    //Enregistrer la photo
                    String parentPath = getApplicationContext().getFilesDir().getPath();
                    File outputDossier = new File(parentPath);

                    //boolean bool = outputDossier.mkdirs();
                    File outputFichier = new File(outputDossier, "screenshot.jpg");
                    FileOutputStream fichierOutputStream = new FileOutputStream(outputFichier);

                    //on compresse le bitmap en jpeg
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fichierOutputStream);

                    //envoie du mail
                    Intent it = new Intent(Intent.ACTION_SEND);
                    it.setType(getString(R.string.imgJpg));

                    //ajouter pj
                    it.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainActivity.this, "garcon.maud.graphe.fileprovider", outputFichier));
                    imageGraph.setDrawingCacheEnabled(false);
                    it.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
                    startActivity(Intent.createChooser(it, getString(R.string.send_mail)));

                }catch (IOException e){
                    Toast.makeText(MainActivity.this, R.string.not_app, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_editer_noeud:
                mode = Mode.EDIT_NOEUD;
                Toast.makeText(this,getText(R.string.select_node), Toast.LENGTH_LONG).show();
                break;

            case R.id.action_editer_arc:
                EditionMenuFragment.newInstance(R.menu.menu_edit_arc).show(getSupportFragmentManager(), "Custom bottom sheet");
                mode = Mode.EDIT_ARC;
                break;
                //TODO edition des arcs
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int id) {
        switch (id) {
            //cas bouton supprimer noeud
            case R.id.action_sup_noeud:
                //on appelle la fonction de suppression de noeud
                graphe.supprimerNoeud(noeudSelec1);
                //on repasse le noeud selectionné a null
                noeudSelec1 = null;
                //on repasse en mode normal
                mode = Mode.NORMAL;
                //on met a jour la vue
                imageGraph.invalidate();
                //fermer le fragement
                editionMenuFragment.dismiss();
                break;

            case R.id.action_modif_nom_noeud:
                //ouvrir une fenetre d'alerte
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.action_modif_nom_noeud));

                // creation du champ text
                final EditText input = new EditText(this);
                //pour avoir le nom du noeud dans le champ
                input.setHint(noeudSelec1.getNom());
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noeudSelec1.setNom(input.getText().toString());
                        imageGraph.invalidate();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;

            case R.id.action_modif_couleur_noeud:
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, noeudSelec1.getPaint().getColor(), new ColorPickerDialog.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        noeudSelec1.getPaint().setColor(color);
                        imageGraph.invalidate();
                    }

                });
                colorPickerDialog.show();
                break;

            case R.id.action_modif_taille_noeud:
                //ouvrir une fenetre d'alerte
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(getString(R.string.action_modif_taille_noeud));

                // creation du champ text
                final EditText input2 = new EditText(this);
                //pour avoir le nom du noeud dans le champ
                input2.setHint(noeudSelec1.getNom());
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder2.setView(input2);

                // Set up the buttons
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noeudSelec1.setTailleNoeud(input2.getText());
                        imageGraph.invalidate();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder2.show();
                break;
        }
    }

    @Override
    public void onDismissMenu() {
        editionMenuFragment = null;
        mode = Mode.NORMAL;
    }
}
