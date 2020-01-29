package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.chirpattendance.activities.LoginActivity;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FragmentLogin extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private EditText password;
    private EditText organizationName;
    private TextView signUp;
    private Button login;
    private Button googleButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;


    public FragmentLogin() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialize(rootView);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.getInterfaceLoginActivty().goToSignUpFragment();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyFields()) {
                    verifyKey(organizationName.getText().toString(), password.getText().toString());
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter All Fields",Toast.LENGTH_LONG).show();
                }
            }
        });


 /*       googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLoginProcess();
            }
        });*/

        return rootView;
    }

    private void initialize(View rootView) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        login = rootView.findViewById(R.id.fragment_login_button_login);
        googleButton = rootView.findViewById(R.id.gooogle_button);
        password = rootView.findViewById(R.id.fragment_login_password_text_view);
        organizationName = rootView.findViewById(R.id.fragment_text_view_organization_name);
        signUp = rootView.findViewById(R.id.sign_up_text_view);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);
        auth = FirebaseAuth.getInstance();
    }

 /*   private void googleLoginProcess () {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
*/
/*
    private void firebaseAuthWithGoogle (GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            Intent intent = new Intent(getActivity(), RoomActivity.class);
                            editor = preferences.edit();
                            editor.putString("uid", uid);
                            editor.putInt("LoginState", 1);
                            editor.putString("key", organizationName.getText().toString());
                            editor.putString("password", password.getText().toString());
                            editor.apply();
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            getActivity().finish();
                            startActivity(intent);
                        }
                        else {
                            makeSnackbar(googleButton, "Some error occured, Try later");
                        }
                    }
                });
    }
*/


    public boolean verifyFields() {
        if (organizationName.getText() != null && organizationName.getText().length() > 0
                && password.getText() != null && password.getText().length() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void verifyKey(final String OrganizationName, final String Password) {
        reference.child("organization").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(OrganizationName).exists() &&
                        dataSnapshot.child(OrganizationName).child("authPassword").getValue().equals(Password))
                {
                    login.setVisibility(View.GONE);
                    organizationName.setEnabled(false);
                    password.setEnabled(false);
                    fragmentSwitch();

                }
                else {

                    Toast.makeText(getActivity(),"Wrong Details Entered",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeSnackbar(login, databaseError.getMessage());
            }
        });
    }

    private void makeSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void fragmentSwitch(){
        FragmentGoogleAuth fragmentGoogleAuth=new FragmentGoogleAuth();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.login_signup_frame,fragmentGoogleAuth)
                .commit();
    }

}
