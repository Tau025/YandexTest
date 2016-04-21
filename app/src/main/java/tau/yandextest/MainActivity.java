package tau.yandextest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(Singer.singers);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(Singer selectedSinger) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_ID, selectedSinger.getId());
                startActivity(intent);
            }
        });
    }
}
