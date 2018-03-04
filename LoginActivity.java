package com.jrschugel.loadmanager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Session Manager Class
    SessionManager session;
    // ID to identify phone permissions request
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = "LoginActivity";
    RequestQueue queue;
    private FirebaseAuth mAuth;
    private EditText etUsername, etPassword, etEmail;
    String firebasePassword;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        if(checkAndRequestPermissions()) {
            //permissions granted, load activity
            if (session.isLoggedIn()) {
                Intent intent = new Intent(LoginActivity.this, NavDrawerActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        final Button bLogin = findViewById(R.id.blogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String email = etEmail.getText().toString();
                firebasePassword = username.toUpperCase() + password;

                loginUser(email, firebasePassword);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (session.isLoggedIn()) {

            if (currentUser != null) {
                updateUI(currentUser);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "phone, camera, storage & GPS services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("phone, camera, storage & GPS Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }



    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void showExplanation(final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed")
                .setMessage("Rationale")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void UpdateRegistrationToken() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Save refreashed token to mysql database
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String username = user.get(SessionManager.KEY_USERNAME);
        if (username != null) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Response: " + response);
                }
            };
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(TELEPHONY_SERVICE);
            final String tmDevice;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                assert tm != null;
                tmDevice = tm.getDeviceId();

                MyFirebaseUpdateRequest updateRequest = new MyFirebaseUpdateRequest(username, refreshedToken, tmDevice, responseListener);
                queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(updateRequest);
                System.out.println("Registration.onTokenRefresh TOKEN: " + refreshedToken );
            }
        }
    }

    private  boolean checkAndRequestPermissions() {
        int permissionPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void updateUI (FirebaseUser user) {

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();




        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean(("success"));

                    if (success) {

                        String name = jsonResponse.getString("name");
                        String truck = jsonResponse.getString("truck");
                        String dname = jsonResponse.getString("dispname");
                        String dphone = jsonResponse.getString("dispphone");
                        String username = jsonResponse.getString("username");

                        session.createLoginSession(name, truck, dname, dphone, username);
                        UpdateRegistrationToken();
                        FirebaseMessaging.getInstance().subscribeToTopic("Reefers");
                        FirebaseMessaging.getInstance().subscribeToTopic(truck);
                        FirebaseMessaging.getInstance().subscribeToTopic(dname);

                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, NavDrawerActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        LoginActivity.this.startActivity(intent);
                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failed!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }
}
