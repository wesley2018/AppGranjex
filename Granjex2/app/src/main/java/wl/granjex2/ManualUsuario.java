package wl.granjex2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wl.granjex2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManualUsuario extends Fragment {

    public ManualUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_usuario, container, false);
    }

}
