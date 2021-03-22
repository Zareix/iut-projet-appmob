package fr.iut.appmobprojet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fr.iut.appmobprojet.R;

/**
 * Fragment correspondant à la page des paramètres
 */
public class SettingsFragment extends Fragment {
    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText mEmail = view.findViewById(R.id.change_email);
        EditText mJustifEditText = view.findViewWithTag(R.id.justificatif_statut);
        Button buttonValidate = view.findViewById(R.id.validate_btn3);

        buttonValidate.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (!mEmail.getText().toString().equals("")) {
                Map<String, Object> data = new HashMap<>();
                data.put("email", mEmail.getText().toString());
                user.updateEmail((String) data.get("email")).addOnSuccessListener(aVoid -> FirebaseFirestore.getInstance().collection("users").document(getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null)).update(data)
                        .addOnSuccessListener(aVoid1 -> Snackbar.make(v.findViewById(R.id.settings_fragment), "Email mis à jour !", Snackbar.LENGTH_SHORT).show()));
            } else {
                Toast.makeText(getContext(), "Merci de spécifier une adresse mail", Toast.LENGTH_SHORT).show();
            }

            if (!mJustifEditText.getText().toString().equals("")) {
                Map<String, Object> data = new HashMap<>();
                data.put("justificatifStatut", mJustifEditText.getText().toString());
                FirebaseFirestore.getInstance().collection("users").document(getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null)).update(data)
                        .addOnSuccessListener(aVoid -> Snackbar.make(v.findViewById(R.id.settings_fragment), "Justificatif envoyé !", Snackbar.LENGTH_SHORT).show());

            }
        });
    }
}