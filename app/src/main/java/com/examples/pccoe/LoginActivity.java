package com.examples.pccoe;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    FirebaseUser firebaseUser;
    ImageView logo;
    String email,prn,rollno,branch,division;
    private final String url="http://181.215.79.82";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("873112653322-asbm6uhvint6h7n5tnkuh8bijmv13cvd.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            email=firebaseUser.getEmail();
            requestuserdata1(email);


        }
        else {
            Intent intent=mGoogleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent,100);
            toast("hello");
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful())
            {
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, task -> {
                                    // Check condition
                                    if(task.isSuccessful())
                                    {   // When task is successful
                                        // Redirect to profile activity
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        firebaseUser = auth.getCurrentUser();
                                        email = firebaseUser.getEmail();
                                        if(email.contains("@pccoepune.org")){
                                            requestuserdata(email);

                                        }
                                        else {
                                            signout();
                                            firebaseUser.delete();
                                        //    user.delete();
                                            Intent intent=mGoogleSignInClient.getSignInIntent();
                                            // Start activity for result
                                            startActivityForResult(intent,100);
                                            toast("Login with college email");

                                        }

                                    }
                                    else
                                    {
                                        // When task is unsuccessful
                                        // Display Toast
                                        toast("Authentication Failed :"+ Objects.requireNonNull(task.getException())
                                                .getMessage());
                                    }
                                });
                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    private void signout() {
        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(LoginActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                firebaseAuth.signOut();
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    private void requestuserdata(String sendemail) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/fetch_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // on below line passing our response to json object.
                try {
                    // on below line passing our response to json object.
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonObject = jsonarray.getJSONObject(0);
                    rollno=jsonObject.getString("Roll No");
                    prn=jsonObject.getString("Prn");
                    division=jsonObject.getString("Division");
                    branch=jsonObject.getString("Branch");
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("rollno",rollno);
                    myIntent.putExtra("prn",prn);
                    myIntent.putExtra("branch",branch);
                    myIntent.putExtra("division",division);
                    startActivity(myIntent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    signout();
                    toast("No Account Found");
                    firebaseUser.delete();
                    Intent intent=mGoogleSignInClient.getSignInIntent();
                    // Start activity for result
                    startActivityForResult(intent,100);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent=mGoogleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
                toast("error");
                signout();


            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Email",sendemail);
                return param;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void requestuserdata1(String sendemail) {
        StringRequest request = new StringRequest(Request.Method.POST, url+"/fetch_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // on below line passing our response to json object.
                try {
                    // on below line passing our response to json object.
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonObject = jsonarray.getJSONObject(0);
                    rollno=jsonObject.getString("Roll No");
                    prn=jsonObject.getString("Prn");
                    division=jsonObject.getString("Division");
                    branch=jsonObject.getString("Branch");
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("rollno",rollno);
                    myIntent.putExtra("prn",prn);
                    myIntent.putExtra("branch",branch);
                    myIntent.putExtra("division",division);
                    startActivity(myIntent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    signout();
                    toast("No Account Found");
                    firebaseUser.delete();
                    Intent intent=mGoogleSignInClient.getSignInIntent();
                    // Start activity for result
                    startActivityForResult(intent,100);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("error");
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                finish();
            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Email",sendemail);
                return param;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(firebaseUser==null)
        {
            email=firebaseUser.getEmail();
            requestuserdata(email);

        }
    }
}