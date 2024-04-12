package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.username;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.se309.soccernexus.util.recyclerviews.Chat;
import com.se309.soccernexus.util.recyclerviews.ChatAdapter;
import com.se309.soccernexus.websockets.WebSocketListener;
import com.se309.soccernexus.websockets.WebSocketManager;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Individual Message Page
 */
public class IndividualMessageActivity extends AppCompatActivity implements WebSocketListener {

    private String otherUsername;

    TextView nameText;

    EditText messageInput;
    ImageButton sendBtn, backBtn;

    RecyclerView recyclerView;

    private ArrayList<Chat> chatList = new ArrayList<>();

    ChatAdapter adapter = new ChatAdapter(chatList);

    //what message the user last sent
    private String myLatestMessage;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Gets the name and username of the other play through the extras.
     * Creates a new instance of the WebSocketManager and attempts to connect to the
     * WebSocket.
     * Requests the message history from the backend and updates the recycler view
     * accordingly.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_chat_activity);

        nameText = findViewById(R.id.individual_message_userText);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            nameText.setText(extras.getString("name"));
            otherUsername = extras.getString("username");
        }

        messageInput = findViewById(R.id.individual_message_input);
        sendBtn = findViewById(R.id.individual_message_sendBtn);
        backBtn = findViewById(R.id.individual_message_backBtn);
        recyclerView = findViewById(R.id.individual_message_recyclerView);

        String serverUrl = BASE_SERVER_URL + "/playerToPlayer/" + username + "/" + otherUsername;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(IndividualMessageActivity.this);

        updateRecyclerView();


        RequestQueue queue = Volley.newRequestQueue(IndividualMessageActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getMessageHistory/" + username + "/" + otherUsername, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("history", response.toString());

                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String msg = response.getJSONObject(i).getString("message_text");
                                if(response.getJSONObject(i).getJSONObject("sending_player").getLong("player_id") == playerID) {
                                    boolean hasRead = response.getJSONObject(i).getBoolean("is_read");
                                    if(hasRead) {
                                        chatList.forEach(x -> x.setHasRead(false));
                                        chatList.add(0, new Chat(msg, 1, hasRead));
                                    } else {
                                        chatList.add(0, new Chat(msg, 1, hasRead));
                                    }
                                } else {
                                    chatList.add(0, new Chat(msg, 0, false));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        updateRecyclerView();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("history error", error.toString());
                    }
                }

        );

        queue.add(request);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = messageInput.getText().toString();

                if(s.equals("")) return;

                try {
                    WebSocketManager.getInstance().sendMessage(s);
                    myLatestMessage = s;
                    messageInput.getText().clear();
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(IndividualMessageActivity.this, PrivateMessageLobbyActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the recycler view for the chat.
     */
    public void updateRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Called when the WebSocket is open. Method is required but empty.
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     * Called when the WebSocket receives a message. Upon a message received,
     * the method checks to see if it was the message we just sent to decide whether
     * or not to make the new Chat object a sender. Clears the latest message and
     * updates the recycler view.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {

            if(message.equals(myLatestMessage)) {
                chatList.add(0, new Chat(message, 1, false));
            } else {
                chatList.add(0, new Chat(message, 0, false));
            }
            myLatestMessage = "";
            adapter.notifyDataSetChanged();
            recyclerView.getLayoutManager().scrollToPosition(0);
        });
    }

    /**
     * Called when the WebSocket is closed.
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.i("websocket closed", reason.toString());
    }

    /**
     * Called when the WebSocket receives an error.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {

    }

    /**
     * Called when the activity stops. Upon a stop, the WebSocket closes.
     */
    @Override
    protected void onStop() {
        super.onStop();
        WebSocketManager.getInstance().disconnectWebSocket();
    }
}
