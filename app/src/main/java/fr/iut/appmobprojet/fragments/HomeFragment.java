package fr.iut.appmobprojet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import fr.iut.appmobprojet.AllProductsFragment;
import fr.iut.appmobprojet.R;

/**
 * Fragment correspondant à la page d'accueil
 */
public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment allProductsFragment = AllProductsFragment.newInstance(false);
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.for_fragment_main, allProductsFragment, "allproducts").commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}