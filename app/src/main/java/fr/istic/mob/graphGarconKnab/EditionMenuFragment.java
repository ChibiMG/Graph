package fr.istic.mob.graphGarconKnab;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import garcon.maud.graphe.R;

/**
 * Created by Maud Garçon & Saly Knab on 10/10/2019.
 */

public class EditionMenuFragment extends BottomSheetDialogFragment {
    private Listener mListener;
    private Menu menu;
    private static final String ARG_MENU_ID = "menu_id";

    public static EditionMenuFragment newInstance(int menuId) {
        // Création bundle contenant le menu ID
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_ID, menuId);

        // Création du fragment et on passe le bundle
        EditionMenuFragment fragment = new EditionMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Petite astuce, créer un popupmenu qui n'est pas affiché pour charger la ressource menu (dont l'ID est en argument)
        PopupMenu p = new PopupMenu(getContext(), null);
        menu = p.getMenu();
        p.getMenuInflater().inflate(getArguments().getInt(ARG_MENU_ID), menu);

        // Chargement layout (une RecyclerView, comme une liste)
        return inflater.inflate(R.layout.menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        // Configuration de la recyclerView : LinearLayout pour l'affichage, et ItemAdapter pour le contenu
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement EditionMenuFragment.Listener");
        }
    }

    public interface Listener {
        void onItemClick(int id);
        void onDismissMenu();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDismissMenu();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.menu_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        // On appelle la method onItemClick dans la MainActivity
                        mListener.onItemClick(menu.getItem(getAdapterPosition()).getItemId());
                    }
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter() {
            // Récupération du nombre d'item dans le menu
            mItemCount = menu.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Remplissage du texte depuis le menu
            holder.text.setText(menu.getItem(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }


}
