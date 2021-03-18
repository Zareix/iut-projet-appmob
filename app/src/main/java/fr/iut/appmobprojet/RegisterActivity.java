package fr.iut.appmobprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// TODO : change input type for password
public class RegisterActivity extends AppCompatActivity {
    private static final int MIN_PASSWORD_LENGTH = 6;

    EditText mUsername, mEmail, mPassword, mPasswordConfirm;
    String username, email;
    Button buttonValidate;
    FirebaseAuth fAuth;
    FirebaseFirestore fDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.username_register);
        mEmail = findViewById(R.id.email_register);
        mPassword = findViewById(R.id.password_register);
        mPasswordConfirm = findViewById(R.id.password_confirm_register);
        buttonValidate = findViewById(R.id.validate_btn);

        fAuth = FirebaseAuth.getInstance();
        fDb = FirebaseFirestore.getInstance();

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password_confirm = mPasswordConfirm.getText().toString().trim();


                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Un nom est nécessaire");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Une adresse mail est nécessaire");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Un mot de passe est nécessaire");
                    return;
                }

                if (password.length() < MIN_PASSWORD_LENGTH) {
                    mPassword.setError("Votre mot de passe doit faire plus de " + MIN_PASSWORD_LENGTH + " caractères");
                    return;
                }

                if (TextUtils.isEmpty(password_confirm)) {
                    mPasswordConfirm.setError("Merci de confirmer votre mot de passe");
                    return;
                }

                if (!password.equals(password_confirm)) {
                    mPasswordConfirm.setError("Les mots de passe ne correspondent pas");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            //TODO : Add user info to DB
                            Map<String, Object> data = new HashMap<>();
                            data.put("username", username);
                            data.put("email", email);
                            data.put("createdAt", "to be defined");
                            data.put("imageUrl", "to be defined");
                            data.put("justificatifStatut", "to be defined");
                            data.put("statut", "to be defined");
                            fDb.collection("users").document(username).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // success
                                        }
                                    });

                            if (user != null) {
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Erreur lors de la création de l'utilisateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}