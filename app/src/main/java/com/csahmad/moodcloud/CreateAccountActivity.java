package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** The activity for creating an {@link Account}. */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProfileController profileController = new ProfileController();
        AccountController accountController = new AccountController();

        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountController.isUsernameUnique()) {
                    Profile profile = new Profile(usernameText.getText().toString());
                    
                    Account account = new Account(usernameText.getText().toString(), passwordText.getText().toString(), profile);
                    accountController.addAccount(account);
                    //probably something to sign in the user
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NewsFeedActivity.class);
                    startActivity(intent);

                }

            }
        });
    }
}
