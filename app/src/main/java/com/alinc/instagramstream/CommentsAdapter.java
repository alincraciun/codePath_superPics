package com.alinc.instagramstream;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alinc on 9/22/15.
 */
public class CommentsAdapter extends ArrayAdapter<String> {
    public CommentsAdapter(Context context, ArrayList<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String comment = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comments_fragment, parent, false);
        }

        TextView tvComment = (TextView) convertView.findViewById(R.id.tvAllComments);
        tvComment.setText(Html.fromHtml(comment));


        return convertView;
    }
}
