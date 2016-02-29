package it446.nfp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChristensenKC on 10/7/2015.
 */
public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getSimpleName();
    protected TextView mSignUpTextView;
    protected EditText mUserName;
    protected EditText mPassword;
    protected Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignUpTextView = (TextView) findViewById(R.id.signUpText);

        String myString = getResources().getString(R.string.sign_up_text);
        int i1 = myString.indexOf("S");
        int i2 = myString.indexOf("!");
        mSignUpTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mSignUpTextView.setText(myString, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable)mSignUpTextView.getText();
        ClickableSpan myClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        };
        mySpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)), i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mySpannable.setSpan(myClickableSpan, i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        mUserName = (EditText) findViewById(R.id.userNameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);
    }

    public void onClick(View view) {

        String userName = mUserName.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", userName);
            jsonBody.put("password", password);

        } catch (JSONException e) {

        }
        String url = "http://nfp.capstone.it.et.byu.edu/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        String user_id = response.toString();
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response: ", error.toString());
            }
        });

        if (ApplicationController.getInstance() == null) {
            Log.d("TEST", "here");
        } else {
            ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
        }
        Toast.makeText(LoginActivity.this, "Your User Has Been Logged In...", Toast.LENGTH_SHORT)
                .show();


        if (userName.equals("")) {
            mUserName.setError(getString(R.string.username_required));
            return;
        }
        if (password.equals("")) {
            mPassword.setError(getString(R.string.password_required));
            return;
        }
    }
}
