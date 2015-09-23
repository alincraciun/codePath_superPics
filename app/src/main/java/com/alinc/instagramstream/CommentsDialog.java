package com.alinc.instagramstream;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by alinc on 9/21/15.
 */
public class CommentsDialog extends DialogFragment {

    public CommentsDialog() {

    }

    public static CommentsDialog newInstance (ArrayList<String> allComments) {
        CommentsDialog frag = new CommentsDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("allComments", allComments);
        args.put
        frag.setArguments(args);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment, container);
        ListView lv = (ListView) view.findViewById(R.id.lv_comments);
        CommentsAdapter adapter = new CommentsAdapter(getActivity().getApplicationContext(), getArguments().getStringArrayList("allComments"));
        lv.setAdapter(adapter);
        getDialog().setTitle("COMMENTS");

        return view;
    }
}
