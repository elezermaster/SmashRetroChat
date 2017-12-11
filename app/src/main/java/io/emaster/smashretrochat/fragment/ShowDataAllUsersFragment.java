package io.emaster.smashretrochat.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.activity.ShowDataActivity;
import io.emaster.smashretrochat.activity.ShowDataItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDataAllUsersFragment extends Fragment {


    AppBarLayout appBarLayout;
    Toolbar mToolbar;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    public ShowDataAllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_show_data_all_users, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;




//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = FirebaseDatabase.getInstance().getReference("User_Details");
//
//        recyclerView = (RecyclerView)findViewById(R.id.show_data_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(ShowDataActivity.this));
//        Toast.makeText(ShowDataActivity.this, "Wait !  Fetching List...", Toast.LENGTH_SHORT).show();

        //View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

       //// recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_news);
       //// recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       ///// firebaseClient =  new FirebaseClient(getActivity(), ReferenceUrl.FIREBASE_CHAT_URL, recyclerView );

        ///////////firebaseClient.refreshData();
        // Initialize adapter
        ///////List<UsersChatModel> emptyListChat=new ArrayList<UsersChatModel>();

        // Toast.makeText(getActivity().getApplicationContext(), ""+ user.getProviders().toString(), Toast.LENGTH_LONG).show();
        // Log.i("FIRE"," onCreateView user: " +user.getProviders().toString());

        //mUsersChatAdapter =new UsersChatAdapter(this,emptyListChat);

        // Set adapter to recyclerView
        //mUsersFireChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mUsersFireChatRecyclerView.setHasFixedSize(true);
        //mUsersFireChatRecyclerView.setAdapter(mUsersChatAdapter);

        ///createDemiUsers();

        ////return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

//        //Log.d("LOGGED", "IN onStart ");
//        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems, ShowDataActivity.ShowDataViewHolder>
//                (ShowDataItems.class, R.layout.show_data_single_item, ShowDataActivity.ShowDataViewHolder.class, myRef)
//        {
//
//            public void populateViewHolder(final ShowDataActivity.ShowDataViewHolder viewHolder, ShowDataItems model, final int position) {
//                viewHolder.Image_URL(model.getImage_URL());
//                viewHolder.Image_Title(model.getImage_Title());
//
//
//                //OnClick Item it will Delete data from Database
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(final View v) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDataActivity.this);
//                        builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        int selectedItems = position;
//                                        mFirebaseAdapter.getRef(selectedItems).removeValue();
//                                        mFirebaseAdapter.notifyItemRemoved(selectedItems);
//                                        recyclerView.invalidate();
//                                        onStart();
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog dialog = builder.create();
//                        dialog.setTitle("Confirm");
//                        dialog.show();
//                    }
//                });
//
//
//            }
//        };
//
//        recyclerView.setAdapter(mFirebaseAdapter);
    }
}
