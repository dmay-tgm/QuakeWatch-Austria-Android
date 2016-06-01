package tgm.shakeit.quakewatchaustria;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

/**
 * Activity that displays input fields for postal code and city. If the quake is was felt more than 30 minutes ago, a date- and timepicker will be displayed.
 *
 * @author Daniel May
 * @version 2016-05-31
 */
public class LocationPage extends AppCompatActivity {
    private TextInputEditText plz, loc, date, time;
    private TextInputLayout layout_PLZ;
    private TextInputLayout layout_loc;
    private boolean plz_ready, loc_ready;
    private FloatingActionButton fab;
    private DateTime dateTime;

    /**
     * Inflates the layout and builds the whole layout according to the presets made earlier.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        boolean now = (boolean) getIntent().getExtras().getSerializable("now");

        layout_PLZ = (TextInputLayout) findViewById(R.id.input_PLZ_layout);
        plz = (TextInputEditText) findViewById(R.id.input_PLZ);
        if (plz != null)
            plz.addTextChangedListener(new MyTextWatcher(plz));

        layout_loc = (TextInputLayout) findViewById(R.id.input_loc_layout);
        loc = (TextInputEditText) findViewById(R.id.input_loc);
        if (loc != null)
            loc.addTextChangedListener(new MyTextWatcher(loc));

        TextInputLayout layout_date = (TextInputLayout) findViewById(R.id.input_date_layout);
        date = (TextInputEditText) findViewById(R.id.input_date);

        TextInputLayout layout_time = (TextInputLayout) findViewById(R.id.input_time_layout);
        time = (TextInputEditText) findViewById(R.id.input_time);

        fab = (FloatingActionButton) findViewById(R.id.loc_next);
        if (fab != null) {
            fab.setEnabled(false);
            fab.setOnClickListener(new View.OnClickListener() {
                /**
                 * Sets the JSON values, that have been entered.
                 * @param v the view that was clicked on
                 */
                @Override
                public void onClick(View v) {
                    Report.setPLZ(plz.getText().toString());
                    Report.setOrt(loc.getText().toString());
                    Report.setTime(dateTime);
                    if (Report.getLocLastUpdate() == null)
                        Report.setLocLastUpdate(dateTime);
                    Intent intent = new Intent(getBaseContext(), ComicPage.class);
                    startActivity(intent);
                }
            });
        }
        //start with now
        dateTime = new DateTime(DateTimeZone.getDefault());
        if (!now) {
            updateDateLabel();
            updateTimeLabel();
            final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    dateTime = dateTime.withYear(year);
                    dateTime = dateTime.withMonthOfYear(monthOfYear + 1);
                    dateTime = dateTime.withDayOfMonth(dayOfMonth);
                    updateDateLabel();
                }
            };

            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dpd = new DatePickerDialog(LocationPage.this, datePickerDialog, dateTime.getYear(), dateTime.getMonthOfYear() + 1, dateTime.getDayOfMonth());
                    dpd.getDatePicker().setMaxDate(new Date().getTime());
                    dpd.show();
                }
            });

            final TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    dateTime = dateTime.withHourOfDay(hourOfDay);
                    dateTime = dateTime.withMinuteOfHour(minute);
                    updateTimeLabel();
                }
            };

            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerDialog(LocationPage.this, timePickerDialog, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), true).show();
                }
            });
        } else {
            // disable layout if necessary
            if (layout_date != null)
                layout_date.setVisibility(View.GONE);
            if (layout_time != null)
                layout_time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the time label.
     */
    private void updateTimeLabel() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        dtf = dtf.withLocale(Locale.GERMAN);
        time.setText(dtf.print(dateTime));
    }

    /**
     * Updates the date label
     */
    private void updateDateLabel() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd. MMMM yyyy");
        dtf = dtf.withLocale(Locale.GERMAN);
        date.setText(dtf.print(dateTime));
    }

    /**
     * Validates a postal code. The code is only valid, if it's not empty, contains 10 characters at max and contains only a-z, A-Z or "-".
     */
    private void validatePLZ() {
        String plz_text = plz.getText().toString().trim();
        if (plz_text.isEmpty() || !plz_text.matches("[a-zA-Z0-9\\-]+")) {
            layout_PLZ.setError("Buchstaben, Zahlen oder Bindestrich eingeben");
            plz_ready = false;
        } else {
            layout_PLZ.setError(null);
            plz_ready = true;
            checkReady();
        }
    }

    /**
     * Validates if the city name isn't empty.
     */
    private void validateLoc() {
        String loc_text = loc.getText().toString().trim();
        if (loc_text.isEmpty()) {
            layout_loc.setError("Ortsname eingeben");
            loc_ready = false;
        } else {
            layout_loc.setError(null);
            loc_ready = true;
            checkReady();
        }
    }

    /**
     * check if everything is setup and ready to go
     */
    private void checkReady() {
        if (plz_ready && loc_ready)
            fab.setEnabled(true);
        else
            fab.setEnabled(false);
    }

    /**
     * Inner private class for reacting to text changes. Is used to frequently call the validating methods.
     */
    private class MyTextWatcher implements TextWatcher {
        /**
         * The observed view.
         */
        private final View view;

        /**
         * Saves the view to observe.
         *
         * @param view the view to observe
         */
        private MyTextWatcher(View view) {
            this.view = view;
        }

        /**
         * Gets called before that text has changed
         *
         * @param charSequence the charsequence
         * @param i            the i
         * @param i1           the i1
         * @param i2           the i2
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /***
         * Gets called while the text is changing
         *
         * @param charSequence the charsequence
         * @param i            the i
         * @param i1           the i1
         * @param i2           the i2
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /**
         * Calls the corresponding view's validating method.
         *
         * @param editable the editable
         */
        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_PLZ:
                    validatePLZ();
                    break;
                case R.id.input_loc:
                    validateLoc();
                    break;
            }
        }
    }
}