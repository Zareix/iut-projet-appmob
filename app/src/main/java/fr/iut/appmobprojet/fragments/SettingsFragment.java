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
<<<<<<< HEAD
=======

>>>>>>> parent of 8de8d62 (Revert "cleanup")
    public SettingsFragment() {
    }
<<<<<<< HEAD
=======

>>>>>>> parent of 8de8d62 (Revert "cleanup")
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
        EditText mPhoto = view.findViewById(R.id.change_picture);
        EditText mJustificatif = view.findViewById(R.id.justificatif_statut);
        Button buttonValidate = view.findViewById(R.id.validate_btn3);

        buttonValidate.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> data = new HashMap<>();

            if (!mEmail.getText().toString().equals("")) {
                data.put("email", mEmail.getText().toString());
<<<<<<< HEAD
                user.updateEmail((String) data.get("email"))
                        .addOnCompleteListener(task -> {
                            // complete
                        });
=======
                user.updateEmail((String) data.get("email"));
>>>>>>> parent of 8de8d62 (Revert "cleanup")
            }

            if (!mPhoto.getText().toString().equals(""))
                data.put("imageUrl", "to be defined");

            if (!mJustificatif.getText().toString().equals(""))
                data.put("justificatifStatut", mJustificatif.getText().toString());

            FirebaseFirestore.getInstance().collection("users").document(getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null)).update(data)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Informations modifiées", Toast.LENGTH_SHORT).show());
        });
    }
}