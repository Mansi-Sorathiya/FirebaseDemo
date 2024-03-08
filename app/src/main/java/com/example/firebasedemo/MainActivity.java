package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    Boolean Login=false;
    EditText email,password,phone,otp;
    Button register ,signin, getotp,registerotp,signout;
    SignInButton googlesignin;
    private FirebaseAuth x;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Object signInRequest;
    private int REQ_ONE_TAP;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("myPref",MODE_PRIVATE);
        editor = preferences.edit();
        Login=preferences.getBoolean("Login",false);


        email=findViewById(R.id.email);
        password=findViewById(R.id.password1);
        phone=findViewById(R.id.phone);
        otp=findViewById(R.id.otp);

        register=findViewById(R.id.register);
        signin=findViewById(R.id.signin);
        getotp =findViewById(R.id.getotp);
        registerotp=findViewById(R.id.registerotp);
        signout=findViewById(R.id.signout);

        googlesignin=findViewById(R.id.google_button);

        mAuth = FirebaseAuth.getInstance();
//        Log.d("AAA", "onCreate: mauth="+mAuth.getCurrentUser().getEmail());

//        register.setOnClickListener(view -> registerUser());
//        signin.setOnClickListener(view -> signinUser());
//        getotp.setOnClickListener(view -> getOTP());
//        registerotp.setOnClickListener(view -> registerPhone());

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);
                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);
            }
        });
    }


//    private void registerPhone() {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.getText().toString());
//        signInWithPhoneAuthCredential(credential);
//    }
//
//    private void getOTP()
//    {
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d("CCC", "onVerificationCompleted:" + credential);
//
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w("DDD", "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
//                    // reCAPTCHA verification attempted with null Activity
//                }
//
//                // Show a message and update the UI
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d("DDD", "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//            }
//        };
//
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+91"+phone.getText().toString())       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
//    {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("DDD", "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // Update UI
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w("DDD", "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void signinUser()
//    {
//        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("BBB", "signInWithEmail:success");
////                            startActivity(new Intent(MainActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
////                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            editor.putBoolean("Login", true);
//                            editor.commit();
//                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                            intent.putExtra("mAuth",mAuth.getCurrentUser().getEmail());
//                            startActivity(intent);
//
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("BBB", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //pdateUI(null);
//                        }
//                    }
//                });
//    }
//
//    private void registerUser()
//    {
//        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("AAA", "createUserWithEmail:success");
//                            startActivity(new Intent(MainActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("AAA", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition

                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    editor.putBoolean("Login", true);
                                    editor.commit();
                                    Intent intent=new Intent(MainActivity.this, Navigation_Activity.class);
                                    intent.putExtra("user",task.getResult().getUser().getEmail());
                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                                    startActivity(new Intent(MainActivity.this, Navigation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                                    intent.putExtra("user",task.getResult().getUser().getEmail());
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Authentication Failed :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}