package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.User;
import com.se309.soccernexus.util.recyclerviews.UserAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

public class FriendRequestsActivity  extends AppCompatActivity {

    ImageButton backBtn;
    RecyclerView recyclerView;
    ArrayList<User> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_requests_activity);

        backBtn = findViewById(R.id.request_backBtn);
        recyclerView = findViewById(R.id.request_recyclerView);

        getFriendRequests();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendRequestsActivity.this, PrivateMessageLobbyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getFriendRequests() {
        RequestQueue queue = Volley.newRequestQueue(FriendRequestsActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/friends/getFriendRequests/" + playerID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("friend request response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String firstName = response.getJSONObject(i).getJSONObject("sending_player").getString("first_name");
                                String lastName = response.getJSONObject(i).getJSONObject("sending_player").getString("last_name");
                                String username = response.getJSONObject(i).getJSONObject("sending_player").getString("username");
                                long id = response.getJSONObject(i).getLong("request_id");

                                requestList.add(new User(firstName, lastName, username, id, 0, 1));
                            } catch (Exception e) {
                            }
                        }
                        updateRecyclerView();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        queue.add(request);
    }

    private void updateRecyclerView() {
        UserAdapter adapter = new UserAdapter(requestList, FriendRequestsActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}
