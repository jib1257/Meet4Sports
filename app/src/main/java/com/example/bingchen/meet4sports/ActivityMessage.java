package com.example.bingchen.meet4sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.example.bingchen.meet4sports.model.model.Message;
//import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.bingchen.meet4sports.Util.TimeUtil.getTimeAgo;

/**
 * Created by User on 4/15/2017.
 */

public class ActivityMessage extends AppCompatActivity {

    private static final DatabaseReference messageBaseDatabase =
            FirebaseDatabase.getInstance().getReference().child("ChatThreads");
    private String thatUserId;
    private String thisUserId;
    private String mCurrentChatThread;
    private DatabaseReference chatDatabase; //import firebase
    //private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DatabaseReference groupChatDatabase;
    private DatabaseReference userChatDatabase;
    private DatabaseReference gameDatabase;
    private DatabaseReference accountsDatabase;
    private LinearLayoutManager messageLinearLayoutManager;
    private boolean isGroup;
    private RecyclerView messageList;
    private ImageButton sendBtn;
    private EditText chatMessageInput;
    private String gameid;
    private HashSet<String> set;
    private Toolbar toolbar;
    public ActivityMessage() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = findViewById(R.id.chat_tool_bar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(ActivityMessage.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_message:

                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(ActivityMessage.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(ActivityMessage.this, ActivitySearch.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(ActivityMessage.this, ActivitySearch.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });


        // set up back button on toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chatMessageInput = findViewById(R.id.chat_message_input);
        sendBtn = findViewById(R.id.chat_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatMessageInput.getText().toString().trim();
                if (!message.equals("")) {
                    //if (isGroup) send_group_message(message);
                    //else send_message(message);
                    send_message(message);
                }
            }
        });

        messageList = findViewById(R.id.chat_messages_list);
        messageList.setHasFixedSize(true);
        messageLinearLayoutManager = new LinearLayoutManager(ActivityMessage.this);
        messageLinearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(messageLinearLayoutManager);


        thatUserId = getIntent().getStringExtra("that_user_id");
        thisUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!isGroup) mCurrentChatThread = hashChatThread(thatUserId, thisUserId);

        userChatDatabase = FirebaseDatabase.getInstance().getReference().child("UserChats");
        if (!isGroup) {
            chatDatabase = messageBaseDatabase.child(mCurrentChatThread);
            chatDatabase.keepSynced(true);
        }

        accountsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Message> options;

        setChatToolBarTitleAsUsername();

        if (isGroup) {
            options =
                    new FirebaseRecyclerOptions.Builder<Message>()
                            .setQuery(groupChatDatabase, Message.class)
                            .setLifecycleOwner(this).build();
        } else {
            options =
                    new FirebaseRecyclerOptions.Builder<Message>()
                            .setQuery(chatDatabase, Message.class)
                            .setLifecycleOwner(this)
                            .build();
        }
        final FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder> messagesRecyclerViewAdapter = new FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder>(options) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                if (viewType == 0) {  // Message is from others
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_message_received, parent, false);
                    return new ReceiveMessagesViewAdapter(view);
                }
                else {  // Message is from user themselves
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_message_sent, parent, false);
                    return new SendMessagesViewAdapter(view);
                }
            }

            @Override
            public int getItemViewType(int position) {
                Message message = getItem(position);
                if (message.getSender().equals(thisUserId)) {
                    return 1;  // ViewType: Message from user themselves
                }
                else return 0;  // ViewType: Message from others
            }


            @Override
            protected void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, final Message model) {
                if (holder.getClass() == SendMessagesViewAdapter.class) {
                    ((SendMessagesViewAdapter) holder).setMessage(model.getMessage());
                    ((SendMessagesViewAdapter) holder).setTime(model.getTime());
                }
                else {
                    ((ReceiveMessagesViewAdapter) holder).setMessage(model.getMessage());
                    ((ReceiveMessagesViewAdapter) holder).setTime(model.getTime());

                    // below is to get the sender name, because sender name is not contained in Message object
                    if (!isGroup) {
                        accountsDatabase.child(thatUserId).child("name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ((ReceiveMessagesViewAdapter) holder).setSenderName(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getSender()).child("name");
                        senderRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i("DataSnapshotsss", dataSnapshot.toString());
                                Log.i("GroupSender", model.getSender());
                                ((ReceiveMessagesViewAdapter) holder).setSenderName(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                }
            }
        };


        messagesRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                                                    @Override
                                                                    public void onItemRangeInserted(int positionStart, int itemCount) {
                                                                        super.onItemRangeInserted(positionStart, itemCount);
                                                                        int friendlyMessageCount = messagesRecyclerViewAdapter.getItemCount();
                                                                        int lastVisiblePosition =
                                                                                messageLinearLayoutManager.findLastVisibleItemPosition();
                                                                        // If the recycler view is initially being loaded or the
                                                                        // user is at the bottom of the list, scroll to the bottom
                                                                        // of the list to show the newly added message.
                                                                        if (lastVisiblePosition == -1 ||
                                                                                (positionStart >= (friendlyMessageCount - 1) &&
                                                                                        lastVisiblePosition == (positionStart - 1))) {
                                                                            messageList.scrollToPosition(positionStart);
                                                                        }
                                                                    }
                                                                }
        );

        messagesRecyclerViewAdapter.startListening();
        messageList.setAdapter(messagesRecyclerViewAdapter);
    }

    // smaller uid goes first
    private String hashChatThread(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "+" + uid2;
        } else {
            return uid2 + "+" + uid1;
        }
    }

    private void send_message(String message) {

        HashMap messageMap = new HashMap();
        messageMap.put("message", message);
        messageMap.put("sender", thisUserId);
        messageMap.put("time", ServerValue.TIMESTAMP);
        chatDatabase.push().setValue(messageMap);

        HashMap messageSnapMap = new HashMap();
        HashMap userChatMap = new HashMap();
        messageSnapMap.put("latestMessage", message);
        messageSnapMap.put("lastTime", ServerValue.TIMESTAMP);
        messageSnapMap.put("isGroup", false);


        userChatMap.put(thisUserId + "/" + thatUserId, messageSnapMap);
        userChatMap.put(thatUserId + "/" + thisUserId, messageSnapMap);

        userChatDatabase.updateChildren(userChatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    chatMessageInput.setText("");
                }
            }
        });

    }

    private void setChatToolBarTitleAsUsername() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        if (isGroup) {
            myRef = database.getReference("GameThread").child(gameid).child("gameName");
        } else {
            myRef = database.getReference("Users").child(getIntent().getStringExtra("that_user_id")).child("name");
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friendName = dataSnapshot.getValue().toString();
                toolbar.setTitle(friendName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class SendMessagesViewAdapter extends RecyclerView.ViewHolder {
    public View mView;

    public SendMessagesViewAdapter(View itemView) {
        super(itemView);
        mView = itemView;
    }


    public void setTime(long time) {
        TextView userNameView = mView.findViewById(R.id.text_message_time);
        userNameView.setText(getTimeAgo(time));
    }

    public void setMessage(String message) {
        TextView userNameView = mView.findViewById(R.id.text_message_body);
        userNameView.setText(message);
    }
}

    public static class ReceiveMessagesViewAdapter extends RecyclerView.ViewHolder {
    public View mView;

    public ReceiveMessagesViewAdapter(View itemView) {
        super(itemView);
        mView = itemView;
    }


    public void setSenderName(String name) {
        TextView userNameView = mView.findViewById(R.id.text_message_name);
        userNameView.setText(name);
    }

    public void setTime(long time) {
        TextView userNameView = mView.findViewById(R.id.text_message_time);
        userNameView.setText(getTimeAgo(time));

    }

    public void setMessage(String message) {
        TextView userNameView = mView.findViewById(R.id.text_message_body);
        userNameView.setText(message);

    }
}

}