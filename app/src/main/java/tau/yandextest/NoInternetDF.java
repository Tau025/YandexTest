package tau.yandextest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
/**
 * Created by TAU on 21.05.2016.
 */
public class NoInternetDF extends DialogFragment {
    private NoInternetDFListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try { mListener = (NoInternetDFListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + mListener.getClass().getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_internet_msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), getResources().getString(android.R.string.ok), Toast.LENGTH_SHORT).show();
                        mListener.onPositiveButtonClicked();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), getResources().getString(android.R.string.cancel), Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

    public interface NoInternetDFListener{
        void onPositiveButtonClicked();
    }
}