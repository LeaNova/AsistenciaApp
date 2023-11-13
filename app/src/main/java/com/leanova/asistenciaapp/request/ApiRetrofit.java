package com.leanova.asistenciaapp.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leanova.asistenciaapp.models.Session;
import com.leanova.asistenciaapp.models.UsuarioInfo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public class ApiRetrofit {
    private static final String PATH = "http://192.168.0.17:5000/";

    private static ServiceApi serviceApi;

    public static ServiceApi getServiceApi() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

        return serviceApi;
    }

    /*
    public static String obtenerToken(Context context) {
        SharedPreferences sharedP = context.getSharedPreferences("informacion", 0);
        String token = sharedP.getString("token", "");
        return token;
    }
    */

    public static Session obtenerSession(Context context) {
        Session session = null;
        File directorio = context.getFilesDir();
        File archivo = new File(directorio, "configuracion.dat");

        try {
            FileInputStream fInput = new FileInputStream(archivo);
            BufferedInputStream bInput = new BufferedInputStream(fInput);
            //DataInputStream dInput = new DataInputStream(bInput);
            ObjectInputStream oInputStream = new ObjectInputStream(bInput);

            session = (Session) oInputStream.readObject();

            fInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return session;
    }

    public interface ServiceApi {
        //LOGIN
        @FormUrlEncoded
        @POST("usuario/login")
        Call<String> login(
                @Field("mail") String mail,
                @Field("pass") String pass);

        @GET("usuario/get")
        Call<UsuarioInfo> getUsuario(@Header("Authorization") String token);

        @FormUrlEncoded
        @PATCH("asistencia/marcar")
        Call<ResponseBody> marcar(
                @Field("codIngreso") String codIngreso,
                @Field("time") String time,
                @Header("Authorization") String token);

        @FormUrlEncoded
        @PATCH("usuario/edit/pass")
        Call<ResponseBody> updatePass(
                @Field("oldPass") String oldPass,
                @Field("newPass") String newPass,
                @Header("Authorization") String token);
    }
}
