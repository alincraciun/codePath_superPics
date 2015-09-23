package com.alinc.instagramstream;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alinc on 9/21/15.
 */
public class ErrorDialog extends DialogFragment {

    public ErrorDialog() {

    }

    public static ErrorDialog newInstance (String errorTitle, String errorMessage) {
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("errorTitle", errorTitle);
        args.putString("errorMessage", errorMessage);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_fragment, container);
        String errorTitle = getArguments().getString("errorTitle", "Error!");
        String errorMessage = getArguments().getString("errorMessage", "unknown error occured.");
        getDialog().setTitle(errorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.tvError);
        tvError.setText(errorMessage);

        return view;
    }
}
