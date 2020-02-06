package com.example.chirpattendance.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.RoomActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FragmentGoogleAuth extends Fragment {

    private MaterialButton proceed;
    ProgressBar progressBar;
    private static final String TAG = "GoogleActivity";
    private static final int GOOGLE_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private String organizationName;
    private LinearLayout linearLayout;
    private SharedPreferences.Editor editor;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPreferences;


    public FragmentGoogleAuth() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_google_auth, container, false);
        initialize(rootView);

        sharedPreferences=getContext().getSharedPreferences("Admin Details",MODE_PRIVATE);
        organizationName=sharedPreferences.getString("organizationName",null);

        try{
            mAuth=FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();
        }catch (Exception e){
            Log.e("Error","ERROR");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return rootView;
    }

    private void initialize(View rootView) {
        proceed = rootView.findViewById(R.id.proceed);
        progressBar = rootView.findViewById(R.id.progressBar);
        constraintLayout=rootView.findViewById(R.id.constraintLayout);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
                Log.e(TAG, "Google Sign In successful with Account Id" + account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            String uid = mAuth.getCurrentUser().getUid();
                            String email=mAuth.getCurrentUser().getEmail();
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("organization");
                            reference.child("organization").child(organizationName).child("admin").child(uid).child("email").setValue(email);
                            reference.child("organization").child(organizationName).child("admin").child(uid).child("name").setValue(currentUser.getDisplayName());
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(constraintLayout, "SIGN IN SUCCESS", Snackbar.LENGTH_LONG).show();
                            sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("Admin Details", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", uid);
                            Intent intent = new Intent(getActivity(), RoomActivity.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            getActivity().finish();
                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(constraintLayout, "SIGN IN ERROR", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }



}
