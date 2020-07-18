package com.example.gloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton=(SignInButton)findViewById(R.id.sign_in_button);

        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                       // .requestIdToken("")
                        .requestProfile()
                        .requestEmail()
                        .build();
        mSignInClient = GoogleSignIn.getClient(this, options);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                GoogleSignInAccount acct = task.getResult();
                onLoggedIn(acct);
            } else {
                Toast.makeText(getApplicationContext(),"Sign in cancel", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }
}
