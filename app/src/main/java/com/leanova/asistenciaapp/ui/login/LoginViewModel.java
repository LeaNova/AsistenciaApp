package com.leanova.asistenciaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.leanova.asistenciaapp.MainActivity;
import com.leanova.asistenciaapp.models.Session;
import com.leanova.asistenciaapp.request.ApiRetrofit;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> mutableAviso;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getAviso() {
        if(mutableAviso == null) {
            mutableAviso = new MutableLiveData<>();
        }
        return mutableAviso;
    }

    public void iniciarSesion(String mail, String pass) {
        Call<String> tokenPromesa = ApiRetrofit.getServiceApi().login(mail, pass);
        tokenPromesa.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = "Bearer " + response.body();

                    /*
                    SharedPreferences sharedP = context.getSharedPreferences("informacion", 0);
                    SharedPreferences.Editor editor = sharedP.edit();
                    editor.putString("token", token);
                    editor.commit();
                     */

                    saveData(token);
                    mutableAviso.setValue("");

                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                    Toast.makeText(context, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                } else {
                    mutableAviso.setValue("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("APIerror", t.getMessage());
            }
        });
    }

    public void tryToLogin() {
        Session session = ApiRetrofit.obtenerSession(context);

        if(new Date().after(session.getExpire())) {
            Toast.makeText(context, "Sesión expirada", Toast.LENGTH_SHORT).show();
        }

        if(!session.getToken().equals("") && new Date().before(session.getExpire())) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    private void saveData(String token) {
        File directorio = context.getFilesDir();
        File archivo = new File(directorio, "configuracion.dat");

        Long time = Calendar.getInstance().getTimeInMillis();
        Session session = new Session(token, new Date(time + (7*24*60*60*1000)));
        if(!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                Log.d("Error", e.toString());
            }
        }

        try {
            FileOutputStream fOutputStream = new FileOutputStream(archivo);
            BufferedOutputStream bOutputStream = new BufferedOutputStream(fOutputStream);
            ObjectOutputStream oOutputStream = new ObjectOutputStream(bOutputStream);
            //DataOutputStream dOutputStream = new DataOutputStream(bOutputStream);

            //dOutputStream.writeUTF(token);
            oOutputStream.writeObject(session);

            bOutputStream.flush();
            fOutputStream.close();

        } catch (FileNotFoundException e) {
            Log.d("error", e.toString());
        } catch (IOException ioe) {
            Log.d("error", ioe.toString());
        }
    }
}