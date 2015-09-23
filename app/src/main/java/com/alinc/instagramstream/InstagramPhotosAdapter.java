package com.alinc.instagramstream;

import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
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
        TextView tvCommCount;
    }
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto photo = getItem(position);
        ViewHolder viewHolder;

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int screenWidth, imageHeight;


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
            viewHolder.tvCommCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
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

        screenWidth = (int)dpWidth - (viewHolder.ivPhoto.getPaddingLeft() + viewHolder.ivPhoto.getPaddingRight());
        viewHolder.ivPhoto.setMaxWidth(screenWidth);
        if(photo.imageWidth > 0)
            viewHolder.ivPhoto.setMaxHeight(photo.imageHeight / (photo.imageWidth / screenWidth));
        else
            viewHolder.ivPhoto.setMaxHeight(photo.imageHeight / screenWidth);
        Picasso.with(getContext()).load(photo.imageURL).centerCrop().placeholder(R.drawable.xvga_35mm).fit().into(viewHolder.ivPhoto);

        if(photo.allComments.size() > 0) {
            LinearLayout list = (LinearLayout) convertView.findViewById(R.id.llComments);
            list.removeAllViews();
            TextView commentRow = (TextView) inflater.inflate(R.layout.photo_comment, null);
            commentRow.setText(Html.fromHtml(photo.allComments.get((photo.allComments.size()) - 1).toString()));
            list.addView(commentRow);
            if((photo.allComments.size() - 2) >= 0) {
                commentRow = (TextView) inflater.inflate(R.layout.photo_comment, null);
                commentRow.setText(Html.fromHtml(photo.allComments.get((photo.allComments.size()) - 2).toString()));
                list.addView(commentRow);
            }
            viewHolder.tvCommCount.setText("View all " + String.valueOf(photo.commentsCount) + " comments");

        }

        return convertView;
    }
}
