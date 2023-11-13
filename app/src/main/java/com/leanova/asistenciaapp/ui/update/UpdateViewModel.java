package com.leanova.asistenciaapp.ui.update;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.leanova.asistenciaapp.request.ApiRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> mutableAviso;
    private MutableLiveData<Boolean> mutableEnable;
    private MutableLiveData<Boolean> mutableReset;

    public UpdateViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getMutableAviso() {
        if(mutableAviso == null) {
            mutableAviso = new MutableLiveData<>();
        }
        return mutableAviso;
    }

    public LiveData<Boolean> getMutableEnable() {
        if(mutableEnable == null) {
            mutableEnable = new MutableLiveData<>();
        }
        return mutableEnable;
    }

    public LiveData<Boolean> getMutableReset() {
        if(mutableReset == null) {
            mutableReset = new MutableLiveData<>();
        }
        return mutableReset;
    }

    public void checkPass(String pass1, String pass2) {
        if(!pass2.equals(pass1)) {
            mutableAviso.setValue("Las contraseñas no coinciden");
            mutableEnable.setValue(false);
        } else {
            mutableAviso.setValue("");
            mutableEnable.setValue(true);
        }
    }

    public void changePass(String oldPass, String newPass) {
        String token = ApiRetrofit.obtenerSession(context).getToken();

        Call<ResponseBody> changePromesa = ApiRetrofit.getServiceApi().updatePass(oldPass, newPass, token);
        changePromesa.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error en actualizar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "API error", Toast.LENGTH_SHORT).show();
                Log.d("APIerror", t.getMessage());
            }
        });
    }
}
