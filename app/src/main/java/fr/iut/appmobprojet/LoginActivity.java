package fr.iut.appmobprojet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activité de connexion à l'application
 */
public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button buttonValidate;
    FirebaseAuth fAuth;

    int cptRetour = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassword = findViewById(R.id.password_register);
        mEmail = findViewById(R.id.email_register);
        buttonValidate = findViewById(R.id.validate_btn);

        fAuth = FirebaseAuth.getInstance();

        buttonValidate.setOnClickListener(v -> {
            String password = mPassword.getText().toString().trim();
            String email = mEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Une adresse mail est nécessaire");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Un mot de passe est nécessaire");
                return;
            }

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Connexion réussi", Toast.LENGTH_SHORT).show();
                    getApplicationContext().getSharedPreferences("user", MODE_PRIVATE).edit().putString("username", task.getResult().getUser().getDisplayName()).apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Identifiant inconnu", Toast.LENGTH_SHORT).show();
                }
            });
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

    public void handleRedirectToRegister(View v) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(LoginActivity.this, "Faites retour une nouvelle fois pour quitter", Toast.LENGTH_SHORT).show();
        cptRetour++;
        if (cptRetour >= 2) {
            finish();
            System.exit(0);
        }
    }
}