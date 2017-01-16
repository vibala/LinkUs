package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.start_up.dev.apilinkus.Auth.AbstractAsyncActivity;
import com.start_up.dev.apilinkus.Auth.Message;
import com.start_up.dev.apilinkus.Auth.MyDatePickerFragment;
import com.start_up.dev.apilinkus.Auth.UserCreateForm;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.os.AsyncTask.Status.RUNNING;
import static java.lang.String.valueOf;

/**
 * Created by Vignesh on 11/8/2016.
 */

public class RegistrationActivity extends AbstractAsyncActivity {

    private Button dateofBirthBtn, signupBtn;
    private MyDatePickerFragment newFragment;
    private EditText fnTextView,lnTextView,emailTextView,pwdTextView,pwdRTextView;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    protected static final String TAG = RegistrationActivity.class.getSimpleName();
    private PostSecuredResourceTask task = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_up_activity);

        fnTextView = (EditText) findViewById(R.id.fneditText);
        lnTextView = (EditText) findViewById(R.id.lneditText);
        emailTextView = (EditText) findViewById(R.id.emaileditText);
        pwdTextView = (EditText) findViewById(R.id.registration_password);
        pwdRTextView = (EditText) findViewById(R.id.registration_password_confirm);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        dateofBirthBtn = (Button) findViewById(R.id.selectDOB);
        signupBtn = (Button) findViewById(R.id.signupbtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dateofBirthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment = new MyDatePickerFragment();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               task = new PostSecuredResourceTask();
               task.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(task != null && task.getStatus() == RUNNING){
            task.cancel(true);
            Bundle bundle = new Bundle();
            bundle.putString("reason","User cancelled the operation");
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundle);
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }else if(task == null){
            Bundle bundle = new Bundle();
            bundle.putString("reason","User cancelled the operation");
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundle);
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }
    }

    private void displayResponse(Message response){
        if(response.getId() == 100){
            Bundle bundle = new Bundle();
            bundle.putString("firstname",fnTextView.getText().toString());
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundle);
            setResult(RESULT_OK,returnIntent);
            //  Account successfully created
            finish();
        }

        Toast.makeText(this,response.getText(),Toast.LENGTH_LONG).show();
    }

    private class PostSecuredResourceTask extends AsyncTask<String,Void,Message> {

        private UserCreateForm form;
        private String lastname,firstname, passwordv1, passwordv2, email,sexe,dateofBirth;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            lastname = valueOf(lnTextView.getText());
            firstname = valueOf(fnTextView.getText());
            passwordv1 = valueOf(pwdTextView.getText());
            passwordv2 = valueOf(pwdRTextView.getText());
            email = valueOf(emailTextView.getText());
            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);

            if(radioSexButton.getText().equals(getResources().getString(R.string.textMale))){
                sexe = "MALE";
            }else if(radioSexButton.getText().equals(getResources().getString(R.string.textFemale))){
                sexe = "FEMALE";
            }

            if(newFragment == null){
                dateofBirth = null;
            }else {
                dateofBirth = newFragment.getDateofBirthInISOFormat();
            }

        }

        @Override
        protected Message doInBackground(String... strings) {

            final String url = getString(R.string.base_uri) + "/user/registration";

            // Phase 1 : Check all information
            boolean phaseone_valid = true;

            StringBuilder sb = new StringBuilder("Please check the following information :");
            if(lastname == null || lastname.isEmpty()){
                sb.append("\n-Lastname input is empty");
                phaseone_valid = false;
            }

            if(firstname == null || firstname.isEmpty()){
                sb.append("\n-Firstname input is empty");
                phaseone_valid = false;
            }

            if(passwordv1 == null || passwordv1.isEmpty() || passwordv2 == null || passwordv2.isEmpty()){
                sb.append("\n-Password input is empty");
                phaseone_valid = false;
            }else if(passwordv1.length() != passwordv2.length() || !passwordv1.contentEquals(passwordv2)){
                sb.append("\n-Passwords are not similar ");
                phaseone_valid = false;
            }

            if(email == null || email.isEmpty()){
                sb.append("\n- Email is empty");
                phaseone_valid = false;
            }

            if(sexe == null || sexe.isEmpty()){
                sb.append("\n- Sexe is empty");
                phaseone_valid = false;
            }

            if(dateofBirth == null || dateofBirth.isEmpty()){
                sb.append("\n- Date of Birth is empty");
                phaseone_valid = false;
            }

            if(!phaseone_valid)
                return new Message(417,"Errors",sb.toString());

            //final String authorization = "Bearer " + strings[0];
            //Log.d("Authorization",authorization);

            // Create the request header
            HttpHeaders requestHeaders = new HttpHeaders();
            // Set the authorization
            //requestHeaders.set("Authorization",authorization);
            // Set the content accept
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // Create a new RestTemplate instance [RestTemplate is a Spring rest client]
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            // Add the Form Message converter
            messageConverters.add(new FormHttpMessageConverter());
            // Add the Jackson Message converter
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            // Add the message converters to the restTemplate
            restTemplate.setMessageConverters(messageConverters);
            // Create the userform
            UserCreateForm usercreateform = new UserCreateForm(lastname,firstname,email,passwordv1,passwordv2,sexe,dateofBirth);
            // Entity
            HttpEntity<UserCreateForm> request = new HttpEntity<UserCreateForm>(usercreateform, requestHeaders);
            Log.d("Lastname",usercreateform.getUser_lastName());


            try {
                // Make the network request
                Log.d(TAG,url);
                Log.d(TAG,"Add new person");
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, request, Message.class);
                return response.getBody();

            }catch(HttpClientErrorException e){
                Log.d(TAG,e.getLocalizedMessage(),e);
                return new Message(-1,e.getStatusText(),e.getLocalizedMessage());
            }catch(ResourceAccessException e){
                Log.d(TAG,e.getLocalizedMessage(),e);
                return new Message(-1,e.getClass().getSimpleName(),e.getLocalizedMessage());
            }catch(HttpServerErrorException e){
                Log.d(TAG,e.getLocalizedMessage(),e);
                return new Message(-1,e.getClass().getSimpleName(),e.getLocalizedMessage());
            }catch(Exception e){
            Log.d(TAG,e.getLocalizedMessage(),e);
            return new Message(-1,e.getClass().getSimpleName(),e.getLocalizedMessage());
        }

        }

        @Override
        protected void onPostExecute(Message message) {
            dismissProgressDialog();
            displayResponse(message);

        }
    }
}
