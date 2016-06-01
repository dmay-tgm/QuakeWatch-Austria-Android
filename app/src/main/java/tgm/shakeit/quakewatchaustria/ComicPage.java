package tgm.shakeit.quakewatchaustria;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Activity for displaying a list of comics to choose from.
 *
 * @author Daniel May
 * @version 2016-06-01.1
 */
public class ComicPage extends AppCompatActivity {

    /**
     * Initializes the comic page view.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_page);
        String[] title = getResources().getStringArray(R.array.report_title_array);
        String[] detail = getResources().getStringArray(R.array.report_detail_array);

        Comic weather_data[] = new Comic[]
                {
                        new Comic(R.drawable.living_room_01, title[0], detail[0]),
                        new Comic(R.drawable.living_room_02, title[1], detail[1]),
                        new Comic(R.drawable.living_room_03, title[2], detail[2]),
                        new Comic(R.drawable.living_room_04, title[3], detail[3]),
                        new Comic(R.drawable.outside_07_zerstoerung, title[4], detail[4]),
                        new Comic(R.drawable.outside_08_zerstoerung, title[5], detail[5])
                };

        ComicArrayAdapter adapter = new ComicArrayAdapter(this,
                R.layout.custom_comic_row, weather_data);


        ListView comicList = (ListView) findViewById(R.id.comicList);

        if (comicList != null) {
            comicList.setAdapter(adapter);
            comicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent = new Intent(getBaseContext(), ZusatzFragen.class);
                    int temp = ((Comic) adapterView.getItemAtPosition(i)).getIcon();
                    switch (temp) {
                        case R.drawable.living_room_01:
//                            intent.putExtra("staerke", 1);
                            Report.setKlass(1);
                            break;
                        case R.drawable.living_room_02:
//                            intent.putExtra("staerke", 2);
                            Report.setKlass(2);
                            break;
                        case R.drawable.living_room_03:
//                            intent.putExtra("staerke", 3);
                            Report.setKlass(3);
                            break;
                        case R.drawable.living_room_04:
//                            intent.putExtra("staerke", 4);
                            Report.setKlass(4);
                            break;
                        case R.drawable.outside_07_zerstoerung:
//                            intent.putExtra("staerke", 5);
                            Report.setKlass(5);
                            break;
                        case R.drawable.outside_08_zerstoerung:
//                            intent.putExtra("staerke", 6);
                            Report.setKlass(6);
                            break;
                    }
//                    startActivity(intent);
                }
            });
        }
    }
}