package com.leanova.asistenciaapp.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.leanova.asistenciaapp.databinding.FragmentMainBinding;
import com.leanova.asistenciaapp.models.UsuarioInfo;
import com.leanova.asistenciaapp.ui.CaptureActivityPortraint;

import java.util.Date;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainViewModel mainViewModel;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        mainViewModel.marcarAsistencia(result);
    });

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getMutableUsuario().observe(getViewLifecycleOwner(), new Observer<UsuarioInfo>() {
            @Override
            public void onChanged(UsuarioInfo usuario) {
                binding.tvNombreH.setText(usuario.getNombre());
                binding.tvApellidoH.setText(usuario.getApellido());
                binding.tvDniH.setText(usuario.getDni());
                binding.tvDireccionH.setText(usuario.getDireccion());
                binding.tvTelefonoH.setText(usuario.getTelefono());
                Date fecha = usuario.getFechaIngreso();
                int mes = fecha.getMonth()+1;
                int anio = fecha.getYear()+1900;
                String ingreso = fecha.getDate() + "-" + mes + "-" + anio;
                binding.tvFechaH.setText(ingreso);
            }
        });
        mainViewModel.getUsuario();
        armarVista(root);

        return root;
    }

    private void armarVista(View view) {
        binding.btLeerQRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanear();
            }
        });
    }

    private void escanear() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("ESCANEAR QR");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(CaptureActivityPortraint.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
    }
}