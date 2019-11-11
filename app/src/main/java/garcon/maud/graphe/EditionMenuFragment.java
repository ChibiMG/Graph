package garcon.maud.graphe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Maud Garçon & Saly Knab on 10/10/2019.
 */

public class EditionMenuFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private Listener mListener;

    public static EditionMenuFragment newInstance() {

        Bundle args = new Bundle();

        EditionMenuFragment fragment = new EditionMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edition_menu, container, false);

        ((TextView) view.findViewById(R.id.action_sup_noeud)).setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View view) {
        mListener.onItemClick(view.getId());
    }

    public interface Listener {
        void onItemClick(int id);
    }
}
