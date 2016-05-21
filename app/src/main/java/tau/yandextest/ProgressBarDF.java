package tau.yandextest;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
/**
 * Created by TAU on 21.05.2016.
 */
public class ProgressBarDF extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.progress_bar, null);
        dialog.setContentView(view);
        return dialog;
    }
}