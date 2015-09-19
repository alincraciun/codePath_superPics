package com.alinc.instagramstream;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alinc on 9/17/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    private LayoutInflater inflater;
    private static class ViewHolder {
        TextView tvCaption;
        TextView tvUser;
        ImageView ivPhoto;
        ImageView ivUserPhoto;
        TextView tvLikes;
        TextView tvTimeStamp;
    }
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto photo = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //Create a new view from template
            convertView = inflater.inflate(R.layout.item_photo, parent, false);

            viewHolder.ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
            viewHolder.tvUser = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimePast);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivUserPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.userImageURL).resize(60, 60).into(viewHolder.ivUserPhoto);
        viewHolder.tvUser.setText(photo.username);
        viewHolder.tvTimeStamp.setText(photo.timeStamp);
        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.tvLikes.setText("\u2764" + " " + String.valueOf(photo.likesCount) + " likes");
        viewHolder.ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageURL).fit().centerCrop().placeholder(R.drawable.xvga_35mm).into(viewHolder.ivPhoto);


        if(photo.allComments.size() > 0) {
            LinearLayout list = (LinearLayout) convertView.findViewById(R.id.llComments);
            list.removeAllViews();
            //Log.i("Debug::", String.valueOf(photo.commentsCount));
            for(String photoComment : photo.allComments) {
                TextView commentRow = (TextView) inflater.inflate(R.layout.photo_comment, null);
                //Log.i("Debug::", photoComment);
                commentRow.setText(photoComment);
                list.addView(commentRow);
            }
        }

        return convertView;
    }
}
