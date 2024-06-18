package com.example.merisuraksha.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.gbuttons.GoogleSignInButton;
import com.example.merisuraksha.R;
import com.example.merisuraksha.databinding.ResetpassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {
    EditText memail,mpass;
    TextView mforget,msignuptxt;
    CheckBox mremember;
    Button msign;
    ProgressBar progressBar;
    GoogleSignInButton googleSignInButton;
    FirebaseAuth mauth;
    FirebaseFirestore mstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        // Background Work
        ScrollView scrollView = findViewById(R.id.gradient_listback);
        AnimationDrawable animationDrawable = (AnimationDrawable) scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //id
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        memail = findViewById(R.id.emaill);
        mpass = findViewById(R.id.passl);
        mforget = findViewById(R.id.forgetp);
        msignuptxt = findViewById(R.id.signuptxt);
        mforget = findViewById(R.id.forgetp);
        msign = findViewById(R.id.signinbut);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        progressBar = findViewById(R.id.progress1);
        mauth = FirebaseAuth.getInstance();

        if(mauth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }



        msignuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),Register.class));
            finish();
            }
        });

        msign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString().trim().toLowerCase();
                String pass = mpass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    memail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mpass.setError("Password is Required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Sucessful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        mforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResetpassBinding binding = ResetpassBinding.inflate(getLayoutInflater());
                AlertDialog dialog = new AlertDialog.Builder(LoginPage.this)
                        .setView(binding.getRoot()).create();
                binding.summit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mail = binding.enteremailreset.getText().toString();
                            if (TextUtils.isEmpty(mail)) {
                                binding.enteremailreset.setError("Enter The Email");
                                return;
                            }
                            mauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginPage.this, "Reset Link is Successfully Send", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginPage.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                });
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });



    }
}