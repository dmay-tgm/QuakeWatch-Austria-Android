package tgm.shakeit.quakewatchaustria;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Moritz
 */
public class ZusatzFragen extends AppCompatActivity{

    private int staerke;
    private static TextView tv2,tv3,tv4;
    private Switch sw1,sw2,sw3;
    private CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7;
    private FloatingActionButton next;
    private ScrollView sv;
    private NumberPicker np;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zusatzfragen);
        tv2 = (TextView) findViewById(R.id.textView);
        tv3 = (TextView) findViewById(R.id.textView2);
        tv4 = (TextView) findViewById(R.id.textView3);
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
        sv = (ScrollView) findViewById(R.id.scrollview);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(100);
        np.setMinValue(0);
        next = (FloatingActionButton) findViewById(R.id.next);

        staerke = (int) getIntent().getExtras().getSerializable("staerke");
        switch (staerke) {
            case 1:
                tv2.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
                sw1.setVisibility(View.INVISIBLE);
                sw2.setVisibility(View.INVISIBLE);
                sw3.setVisibility(View.INVISIBLE);
                sv.setVisibility(View.INVISIBLE);

            case 2:
                tv2.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
                sw1.setVisibility(View.INVISIBLE);
                sw2.setVisibility(View.INVISIBLE);
                sw3.setVisibility(View.INVISIBLE);
                sv.setVisibility(View.INVISIBLE);

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
                sv.setVisibility(View.INVISIBLE);

            case 4:
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("Sind Gegenstände umgefallen?");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("Sind Sie aus Angst ins Freie geflüchtet?");
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Haben Sie Gebäudeschäden beobachtet?");
                sw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                sv.setVisibility(View.INVISIBLE);

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
                sv.setVisibility(View.INVISIBLE);

            case 6:
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("Sind Möbel umgekippt?");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("Sind viele Gegenstände aus den Regalen gefallen?");
                tv4.setVisibility(View.VISIBLE);
                tv4.setText("Haben Sie Gebäudeschäden beobachtet?");
                sw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                sv.setVisibility(View.INVISIBLE);

        }
        sw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw3.isChecked()){
                    sv.setVisibility(View.VISIBLE);
                }
                else
                {
                    sv.setVisibility(View.INVISIBLE);
                }
            }
        });
        next.setOnClickListener(new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Report.setFloor(np.getValue());
            switch (staerke) {
                case 3:
                    Report.addZusatz(1,sw1.isChecked());
                    Report.addZusatz(2,sw2.isChecked());
                    Report.addZusatz(3,sw3.isChecked());
                case 4:
                    Report.addZusatz(2,sw1.isChecked());
                    Report.addZusatz(7,sw2.isChecked());
                    Report.addZusatz(8,cb1.isChecked());
                    Report.addZusatz(9,cb2.isChecked());
                    Report.addZusatz(10,cb3.isChecked());
                    Report.addZusatz(11,cb4.isChecked());
                    Report.addZusatz(12,cb5.isChecked());
                    Report.addZusatz(13,cb6.isChecked());
                    Report.addZusatz(14,cb7.isChecked());

                case 5:
                    Report.addZusatz(4,sw1.isChecked());
                    Report.addZusatz(5,sw2.isChecked());
                    Report.addZusatz(7,sw3.isChecked());
                    Report.addZusatz(8,cb1.isChecked());
                    Report.addZusatz(9,cb2.isChecked());
                    Report.addZusatz(10,cb3.isChecked());
                    Report.addZusatz(11,cb4.isChecked());
                    Report.addZusatz(12,cb5.isChecked());
                    Report.addZusatz(13,cb6.isChecked());
                    Report.addZusatz(14,cb7.isChecked());

                case 6:
                    Report.addZusatz(6,sw1.isChecked());
                    Report.addZusatz(7,sw3.isChecked());
                    Report.addZusatz(8,cb1.isChecked());
                    Report.addZusatz(9,cb2.isChecked());
                    Report.addZusatz(10,cb3.isChecked());
                    Report.addZusatz(11,cb4.isChecked());
                    Report.addZusatz(12,cb5.isChecked());
                    Report.addZusatz(13,cb6.isChecked());
                    Report.addZusatz(14,cb7.isChecked());

            }
            Intent i = new Intent(getApplicationContext(), LocationPage.class);
            startActivity(i);
        }
    });

    }
}
