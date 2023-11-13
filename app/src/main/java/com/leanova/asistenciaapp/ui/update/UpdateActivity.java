package com.leanova.asistenciaapp.ui.update;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leanova.asistenciaapp.R;

public class UpdateActivity extends AppCompatActivity {
    private UpdateViewModel updateViewModel;

    private EditText etPassOld_U, etNewPass1_U, etNewPass2_U;
    private TextView tvAviso_U;
    private Button btUpdate_U;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UpdateViewModel.class);

        armarVista();

        updateViewModel.getMutableAviso().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvAviso_U.setText(s);
            }
        });
        updateViewModel.getMutableEnable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                btUpdate_U.setEnabled(aBoolean);
            }
        });

    }

    private void armarVista() {
        this.etPassOld_U = findViewById(R.id.etOldPass_U);
        this.etNewPass1_U = findViewById(R.id.etNewPass1_U);
        this.etNewPass2_U = findViewById(R.id.etNewPass2_U);
        etNewPass2_U.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newPass = etNewPass1_U.getText().toString();
                updateViewModel.checkPass(newPass, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        this.tvAviso_U = findViewById(R.id.tvAviso_U);
        this.btUpdate_U = findViewById(R.id.btUpdate_U);
        btUpdate_U.setEnabled(false);
        btUpdate_U.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = etPassOld_U.getText().toString();
                String newPass = etNewPass2_U.getText().toString();

                updateViewModel.changePass(oldPass, newPass);
            }
        });
    }
}