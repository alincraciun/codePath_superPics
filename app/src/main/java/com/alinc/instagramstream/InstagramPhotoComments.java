package com.alinc.instagramstream;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alinc on 9/19/15.
 */
public class InstagramPhotoComments extends DialogFragment {

    public InstagramPhotoComments() {
    }

    public static InstagramPhotoComments newInstance(String title) {
        InstagramPhotoComments frag = new InstagramPhotoComments();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comments_fragment, container);
        TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
        ((TextView)tvComment).setText("This is an instance of MyDialogFragment");
        return view;
    }
}
