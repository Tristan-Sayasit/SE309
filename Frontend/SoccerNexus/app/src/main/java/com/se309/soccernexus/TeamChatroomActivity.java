package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.username;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.se309.soccernexus.util.recyclerviews.TeamMsg;
import com.se309.soccernexus.util.recyclerviews.TeamMsgAdapter;
import com.se309.soccernexus.websockets.WebSocketListener;
import com.se309.soccernexus.websockets.WebSocketManager;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling logic for the Team Chatroom Page
 */
public class TeamChatroomActivity extends AppCompatActivity implements WebSocketListener {
    TextView teamName;
    ImageButton backBtn, sendBtn;
    EditText messageInput;
    RecyclerView recyclerView;
    private long teamID;
    public ArrayList<TeamMsg> msgList = new ArrayList<>();
    TeamMsgAdapter adapter = new TeamMsgAdapter(msgList);
    private String myLatestMessage;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Gets the name and ID of the team via extras. Creates a new instance of the WebSocketManager
     * and connects to the WebSocket.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_chat_activity);

        teamName = findViewById(R.id.team_message_teamText);
        messageInput = findViewById(R.id.team_message_input);
        sendBtn = findViewById(R.id.team_message_sendBtn);
        backBtn = findViewById(R.id.team_message_backBtn);
        recyclerView = findViewById(R.id.team_message_recyclerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamName.setText(extras.getString("name"));
            teamID = extras.getLong("team_id");
        }

        String serverUrl = BASE_SERVER_URL + "/teamChat/" + playerID + "/" + teamID;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(TeamChatroomActivity.this);

        updateRecyclerView();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = messageInput.getText().toString();

                if (s.equals("")) return;

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
                Intent intent = new Intent(TeamChatroomActivity.this, ViewTeamActivity.class);
                intent.putExtra("teamid", teamID);
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
     * Called when a new message is received from the WebSocket. The message is return to the
     * client with the username of who sent it. The method checks to see if the message is the same
     * as what was just sent, and creates a new TeamMsg object accordingly.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String[] split_message = message.split("\\@\\*");
            String username = split_message[0];
            String actualMessage = split_message[1];
            if (actualMessage.equals(myLatestMessage)) {
                msgList.add(0, new TeamMsg(actualMessage, 1, username));
            } else {
                msgList.add(0, new TeamMsg(actualMessage, 0, username));
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
     * Called when the WebSocket receives an error. Method is required but empty.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {

    }

    /**
     * Called when the activity stops. Closes the WebSocket.
     */
    @Override
    protected void onStop() {
        super.onStop();
        WebSocketManager.getInstance().disconnectWebSocket();
    }

    /**
     * Called when the activity is destroyed. Closes the WebSocket.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnectWebSocket();
    }
}
