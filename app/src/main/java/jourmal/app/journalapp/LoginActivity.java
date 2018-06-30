package jourmal.app.journalapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 101;

    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signIn = (Button)findViewById(R.id.btn_sign);

        ////

        //if the user is connected we directly go to the homeActivity
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(this,"Welcome back : "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
            Intent home =  new Intent(LoginActivity.this, MainActivity.class);
            startActivity(home);
            finish();


        }else{
            //add the google provider for the sign in
            final List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            });



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                // Successfully signed in .. go to the home now
                Toast.makeText(this,"Welcome : "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();

                Intent home =  new Intent(LoginActivity.this, MainActivity.class);
                startActivity(home);
                finish();
            }else{
                //
                if(response != null){
                    Toast.makeText(this,"Not signed in.. "+ response.getError().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }

}

