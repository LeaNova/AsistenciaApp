package com.leanova.asistenciaapp.ui.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.journeyapps.barcodescanner.ScanIntentResult;
import com.leanova.asistenciaapp.models.UsuarioInfo;
import com.leanova.asistenciaapp.request.ApiRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<UsuarioInfo> mutableUsuario;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<UsuarioInfo> getMutableUsuario() {
        if(mutableUsuario == null) {
            mutableUsuario = new MutableLiveData<>();
        }
        return mutableUsuario;
    }

    public void getUsuario() {
        //String token = ApiRetrofit.obtenerToken(context);
        String token = ApiRetrofit.obtenerSession(context).getToken();

        Call<UsuarioInfo> usuarioPromesa = ApiRetrofit.getServiceApi().getUsuario(token);
        usuarioPromesa.enqueue(new Callback<UsuarioInfo>() {
            @Override
            public void onResponse(Call<UsuarioInfo> call, Response<UsuarioInfo> response) {
                if(response.isSuccessful()) {
                    UsuarioInfo u = response.body();
                    mutableUsuario.postValue(u);
                } else {
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }

            @Override
            public void onFailure(Call<UsuarioInfo> call, Throwable t) {
                Toast.makeText(context, "API error", Toast.LENGTH_SHORT).show();
                Log.d("API error", t.getMessage());
                System.exit(0);
            }
        });
    }

    public void marcarAsistencia(ScanIntentResult result) {
        if(result.getContents() != null) {
            Log.d("Result", result.getContents());
            String[] params = result.getContents().split("&");

            boolean qrCorrecto = params[0].equals("QRasistencia");
            String codIngreso = params[1];
            String time = params[2];

            //String token = ApiRetrofit.obtenerToken(context);
            if(qrCorrecto) {
                String token = ApiRetrofit.obtenerSession(context).getToken();

                Call<ResponseBody> asistenciaPromesa = ApiRetrofit.getServiceApi().marcar(codIngreso, time, token);
                asistenciaPromesa.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(context, "Asistencia marcada", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error en marcar", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "API error", Toast.LENGTH_SHORT).show();
                        Log.d("API error", t.getMessage());
                    }
                });
            } else {
                Toast.makeText(context, "QR invalido", Toast.LENGTH_LONG).show();
            }
        }
    }
}