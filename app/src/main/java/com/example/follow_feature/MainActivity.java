package com.example.follow_feature;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    Button followButton = (Button) findViewById(R.id.followButton);
    TextView otherName = (TextView) findViewById(R.id.otherName);

    DatabaseHelper dbHelper;
    //SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    //private String currentUserId = sharedPreferences.getString("username", null);
    private String currentUser = "Riz"; //example
    private String otheUser = "Kyla"; //example


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            dbHelper = new DatabaseHelper(this);

            dbHelper.addUser(currentUser, "Current User");
            dbHelper.addUser(otheUser, "Other User");

            updateFollowButton();

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dbHelper.checkFollowing(currentUser, otheUser)) {
                        //To unfollow
                        if (dbHelper.unfollowUser(currentUser, otheUser)) {
                            Toast.makeText(MainActivity.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //To follow
                        if (dbHelper.followUser(currentUser, otheUser)) {
                            Toast.makeText(MainActivity.this, "Followed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateFollowButton();
                }
            });


            return insets;
        });
    }

    private void updateFollowButton() {
        if (dbHelper.checkFollowing(currentUser, otheUser)) {
            followButton.setText("Unfollow");
        } else {
            followButton.setText("Follow");
        }
    }
}