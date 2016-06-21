package tgm.shakeit.quakewatchaustria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Activity that shows additional questions depending on the strength.
 *
 * @author Moirtz Mühlehner
 * @version 2016-06-01.1
 */
public class ZusatzFragen extends AppCompatActivity {

    private static TextView sbvalue;
    private int staerke;
    private Switch sw1, sw2, sw3;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private LinearLayout layout;
    private SeekBar sb;
    private EditText et;


    /**
     * Gets called on create
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zusatzfragen);
        TextView tv2 = (TextView) findViewById(R.id.textView);
        TextView tv3 = (TextView) findViewById(R.id.textView2);
        TextView tv4 = (TextView) findViewById(R.id.textView3);
        TextView tv5 = (TextView) findViewById(R.id.textView4);
        et = (EditText) findViewById(R.id.editText);
        sw1 = (Switch) findViewById(R.id.switch1);
        sw2 = (Switch) findViewById(R.id.switch3);
        sw3 = (Switch) findViewById(R.id.switch2);
        cb1 = (CheckBox) findViewById(R.id.checkBox);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        cb4 = (CheckBox) findViewById(R.id.checkBox4);
        cb5 = (CheckBox) findViewById(R.id.checkBox5);
        cb6 = (CheckBox) findViewById(R.id.checkBox6);
        cb7 = (CheckBox) findViewById(R.id.checkBox7);
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        sb = (SeekBar) findViewById(R.id.seekBar);
        sb.incrementProgressBy(1);
        sb.setMax(4);
        sbvalue = (TextView) findViewById(R.id.textView5);
        sbvalue.setText("" + sb.getProgress());
        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Gets called when the progress changes
             * @param seekBar the seekbar
             * @param progress the progress
             * @param fromUser fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == sb.getMax())
                    sbvalue.setText("4 oder höher");
                else
                    sbvalue.setText(String.valueOf(progress));
            }

            /**
             * Gets called on start tracking touch
             * @param seekBar the seekbar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * Gets called on stop tracking touch
             * @param seekBar the seekbar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        staerke = (int) getIntent().getExtras().getSerializable("staerke");
        switch (staerke) {
            case 1:
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                sw1.setVisibility(View.GONE);
                sw2.setVisibility(View.GONE);
                sw3.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                break;

            case 2:
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                sw1.setVisibility(View.GONE);
                sw2.setVisibility(View.GONE);
                sw3.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                break;

            case 3:
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("Sind Gegenstände umgefallen?");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("Sind Sie aus Angst ins Freie geflüchtet?");
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Feine Risse im Verputz?");
                sw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                break;

            case 4:
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("Sind Sie aus Angst ins Freie geflüchtet?");
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Haben Sie Gebäudeschäden beobachtet?");
                sw1.setVisibility(View.GONE);
                sw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                break;

            case 5:
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("Hatten Sie Gleichgewichtsprobleme?");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("Sind viele Gegenstände aus den Regalen gefallen?");
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Haben Sie Gebäudeschäden beobachtet?");
                sw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                break;

            case 6:
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("Sind Möbel umgekippt?");
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Haben Sie Gebäudeschäden beobachtet?");
                sw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.GONE);
                sw3.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                break;

        }
        sw3.setOnClickListener(new View.OnClickListener() {
            /**
             * Handels on click
             * @param v the view
             */
            @Override
            public void onClick(View v) {
                if (staerke > 3) {
                    if (sw3.isChecked()) {
                        layout.setVisibility(View.VISIBLE);
                    } else {
                        layout.setVisibility(View.GONE);
                    }
                }
            }
        });
        next.setOnClickListener(new FloatingActionButton.OnClickListener() {
            /**
             * Handles clicks
             * @param v the view that is clicked on
             */
            @Override
            public void onClick(View v) {
                int floor = sb.getProgress();
                if (floor < 4)
                    Report.setFloor(floor);
                else
                    Report.setFloor(4);
                switch (staerke) {
                    case 3:
                        Report.addZusatz(1, sw1.isChecked());
                        Report.addZusatz(2, sw2.isChecked());
                        Report.addZusatz(3, sw3.isChecked());
                        Report.addZusatz(et.getText().toString());
                    case 4:
                        Report.addZusatz(2, sw1.isChecked());
                        Report.addZusatz(7, sw2.isChecked());
                        Report.addZusatz(8, cb1.isChecked());
                        Report.addZusatz(9, cb2.isChecked());
                        Report.addZusatz(10, cb3.isChecked());
                        Report.addZusatz(11, cb4.isChecked());
                        Report.addZusatz(12, cb5.isChecked());
                        Report.addZusatz(13, cb6.isChecked());
                        Report.addZusatz(14, cb7.isChecked());
                        Report.addZusatz(et.getText().toString());

                    case 5:
                        Report.addZusatz(4, sw1.isChecked());
                        Report.addZusatz(5, sw2.isChecked());
                        Report.addZusatz(7, sw3.isChecked());
                        Report.addZusatz(8, cb1.isChecked());
                        Report.addZusatz(9, cb2.isChecked());
                        Report.addZusatz(10, cb3.isChecked());
                        Report.addZusatz(11, cb4.isChecked());
                        Report.addZusatz(12, cb5.isChecked());
                        Report.addZusatz(13, cb6.isChecked());
                        Report.addZusatz(14, cb7.isChecked());
                        Report.addZusatz(et.getText().toString());

                    case 6:
                        Report.addZusatz(6, sw1.isChecked());
                        Report.addZusatz(7, sw3.isChecked());
                        Report.addZusatz(8, cb1.isChecked());
                        Report.addZusatz(9, cb2.isChecked());
                        Report.addZusatz(10, cb3.isChecked());
                        Report.addZusatz(11, cb4.isChecked());
                        Report.addZusatz(12, cb5.isChecked());
                        Report.addZusatz(13, cb6.isChecked());
                        Report.addZusatz(14, cb7.isChecked());
                        Report.addZusatz(et.getText().toString());
                }
                Intent i = new Intent(getApplicationContext(), ContactPage.class);
                startActivity(i);

            }
        });

    }
}