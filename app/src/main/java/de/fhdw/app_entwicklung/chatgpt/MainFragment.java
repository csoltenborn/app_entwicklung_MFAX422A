package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        printFragmentState();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printFragmentState();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        printFragmentState();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        printFragmentState();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        printFragmentState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printFragmentState();
    }

    @Override
    public void onStart() {
        super.onStart();
        printFragmentState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printFragmentState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        printFragmentState();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        printFragmentState();
    }

    @Override
    public void onStop() {
        super.onStop();
        printFragmentState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        printFragmentState();
    }

    private void printFragmentState() {
        TextView textView = getTextView();
        if (textView != null) {
            textView.append(getLifecycle().getCurrentState().name() + "\n");
        }
    }

    private TextView getTextView() {
        if (getView() != null) {
            return getView().findViewById(R.id.textView);
        }
        return null;
    }

}