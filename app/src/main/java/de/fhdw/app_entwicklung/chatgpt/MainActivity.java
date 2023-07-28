package de.fhdw.app_entwicklung.chatgpt;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;

public class MainActivity extends AppCompatActivity {

    public static final String PARCELABLE_EXTRA_CHAT = "parcelable_extra_chat";

    public static final ExecutorService backgroundExecutorService = Executors.newFixedThreadPool(4);
    public static final Handler uiThreadHandler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<Intent> launchActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Chat chat = data.getParcelableExtra(PARCELABLE_EXTRA_CHAT);
                        MainFragment fragment = getMainFragment();
                        if (fragment != null && chat != null) {
                            fragment.setChat(chat);
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_settings) {
            Intent i = new Intent(this, PrefsActivity.class);
            startActivity(i);
            return true;
        }
        if (item.getItemId() == R.id.menu_item_chats) {
            launchActivity.launch(new Intent(this, ChatsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.menu_item_share) {
            MainFragment fragment = getMainFragment();
            if (fragment != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getMainFragment().getChatText().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
    }
}