package tgm.shakeit.quakewatchaustria;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class LocationPage extends AppCompatActivity {
    private TextInputEditText plz, loc;
    private TextInputLayout layout_PLZ, layout_loc;
    private boolean plz_ready, loc_ready;
    private FloatingActionButton fab;
    private boolean now;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        now = (boolean) getIntent().getExtras().getSerializable("now");
        layout_PLZ = (TextInputLayout) findViewById(R.id.input_PLZ_layout);
        plz = (TextInputEditText) findViewById(R.id.input_PLZ);
        if (plz != null) {
            plz.addTextChangedListener(new MyTextWatcher(plz));
        }
        layout_loc = (TextInputLayout) findViewById(R.id.input_loc_layout);
        loc = (TextInputEditText) findViewById(R.id.input_loc);
        if (loc != null) {
            loc.addTextChangedListener(new MyTextWatcher(loc));
        }
        fab = (FloatingActionButton) findViewById(R.id.loc_next);
        if (fab != null) {
            fab.setEnabled(false);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Report.setPLZ(plz.getText().toString());
                    Report.setOrt(loc.getText().toString());
                    Intent intent = new Intent(getBaseContext(), ComicPage.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void validatePLZ() {
        String plz_text = plz.getText().toString();
        if (plz_text.isEmpty() || !plz_text.matches("[a-zA-Z0-9\\-]+")) {
            layout_PLZ.setError("Buchstaben, Zahlen oder Bindestrich eingeben");
            plz_ready = false;
        } else {
            layout_PLZ.setError(null);
            plz_ready = true;
            checkReady();
        }
    }

    private void validateLoc() {
        String loc_text = loc.getText().toString();
        if (loc_text.isEmpty()) {
            layout_loc.setError("Ortsname eingeben");
            loc_ready = false;
        } else {
            layout_loc.setError(null);
            loc_ready = true;
            checkReady();
        }
    }

    private void checkReady() {
        if (plz_ready && loc_ready)
            fab.setEnabled(true);
        else
            fab.setEnabled(false);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_PLZ:
                    validatePLZ();
                    break;
                case R.id.input_loc:
                    validateLoc();
            }
        }
    }


}