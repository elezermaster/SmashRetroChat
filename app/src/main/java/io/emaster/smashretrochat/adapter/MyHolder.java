package io.emaster.smashretrochat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.emaster.smashretrochat.R;


/**
 * Created by elezermaster on 04/09/16.
 */
public class MyHolder extends RecyclerView.ViewHolder {

    TextView nameText;
    TextView onlineText;
    TextView placeText;
    CircleImageView userImage;

    public MyHolder(View itemView) {
        super(itemView);

        nameText =(TextView) itemView.findViewById(R.id.userFirstNameProfile);
        onlineText =(TextView) itemView.findViewById(R.id.connectionStatus);
        userImage = (CircleImageView) itemView.findViewById(R.id.userPhotoProfile);

    }
}
