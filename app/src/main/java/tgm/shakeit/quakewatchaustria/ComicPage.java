package tgm.shakeit.quakewatchaustria;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class ComicPage extends AppCompatActivity {
    private String[] title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_page);
        title = getResources().getStringArray(R.array.report_title_array);
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
                    //Intent intent = new Intent(getBaseContext(),DetailPage.class);
                    int temp = ((Comic) adapterView.getItemAtPosition(i)).getIcon();
                    switch (temp) {
                        case R.drawable.living_room_01:
                            break;
                        case R.drawable.living_room_02:
                            break;
                        case R.drawable.living_room_03:
                            break;
                        case R.drawable.living_room_04:
                            break;
                        case R.drawable.outside_07_zerstoerung:
                            break;
                        case R.drawable.outside_08_zerstoerung:
                            break;

                    }


                    // intent.putExtra("data",temp);
                    //startActivity(intent);
                }
            });
        }
    }
}