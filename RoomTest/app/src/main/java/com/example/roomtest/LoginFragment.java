package com.example.roomtest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private AppDatabase db;
    private EditText etEmail, etPassword;
    private Button btnSignUp, btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        db = AppDatabase.getInstance(getContext());

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        btnLogin = view.findViewById(R.id.btn_login);

        // Registro de usuario
        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    db.userDao().insertUser(new User(email, password));
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Usuario registrado", Toast.LENGTH_SHORT).show());
                });
            } else {
                Toast.makeText(getContext(), "Por favor ingrese ambos campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Inicio de sesión
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    User user = db.userDao().getUserByEmailAndPassword(email, password);
                    getActivity().runOnUiThread(() -> {
                        if (user != null) {
                            // Navegar a HomeFragment con el mensaje de éxito
                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Inicio de sesión exitoso");

                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setArguments(bundle);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, homeFragment);
                            transaction.addToBackStack(null);  // Permite volver al LoginFragment si es necesario
                            transaction.commit();
                        } else {
                            Toast.makeText(getContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Toast.makeText(getContext(), "Por favor ingrese ambos campos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
