package com.example.qldapm.evtranslator.Dialogue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.qldapm.evtranslator.R;

/**
 * Created by vanty on 11/1/2015.
 */
public class ThemmoiFolder extends DialogFragment {
    private String nameString = "";
    private int _thaotac;
    public ThemmoiFolder(String name)
    {
        nameString = name;
        _thaotac = 1;
    }
    public ThemmoiFolder()
    {_thaotac = 0;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View myLayout = inflater.inflate(R.layout.dialogueaddname, null);
        EditText Name = (EditText)myLayout.findViewById(R.id.namefolder);
        Name.setText(nameString);
        builder.setView(myLayout)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       EditText Name = (EditText)myLayout.findViewById(R.id.namefolder);
                       String textName = Name.getText().toString();
                        mListener.onDialogPositiveClick(ThemmoiFolder.this,textName,_thaotac);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ThemmoiFolder.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog,String value,int thaotac);
        public void onDialogNegativeClick(DialogFragment dialog,String value,int thaotac);
    }
    NoticeDialogListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
