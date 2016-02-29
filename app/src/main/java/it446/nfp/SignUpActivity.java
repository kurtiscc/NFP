package it446.nfp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ChristensenKC on 10/7/2015.
 */
public class SignUpActivity extends AppCompatActivity{

    protected EditText mUserName;
    protected EditText mPassword;
    protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUserName = (EditText) findViewById(R.id.userNameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
    }
}
