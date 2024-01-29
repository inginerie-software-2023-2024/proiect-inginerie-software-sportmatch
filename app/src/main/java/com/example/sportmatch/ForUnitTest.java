package com.example.sportmatch;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForUnitTest extends AppCompatActivity {
    //unit testing
    //for unit testing
    private static final String KEY_HISTORY_DATA = "KEY_HISTORY_DATA";

    LogHistory mLogHistory;
    boolean mIsHistoryEmpty = true;
    private TextView mHistoryTextView;
    private DateFormat mSimpleDateFormatter;
    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_unit_test_activity);
        ///for unit testing
        mLogHistory = new LogHistory();

        mHistoryTextView = ((TextView) findViewById(R.id.history));
        mSimpleDateFormatter = new SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault());

        if (savedInstanceState != null) {
            // We've got a past state, apply it to the UI.
            mLogHistory = savedInstanceState.getParcelable(KEY_HISTORY_DATA);
            for (Pair<String, Long> entry : mLogHistory.getData()) {
                appendEntryToView(entry.first, entry.second);
            }
        }
        ///
    }

    /**
     * Called when the user wants to append an entry to the history.
     */
    public void updateHistory(View view) {
        // Get the text to add and timestamp.
        EditText editText = (EditText) view.getRootView().findViewById(R.id.editText);
        CharSequence textToAdd = editText.getText();
        long timestamp = System.currentTimeMillis();

        // Show it back to the user.
        appendEntryToView(textToAdd.toString(), timestamp);

        // Update the history.
        mLogHistory.addEntry(textToAdd.toString(), timestamp);

        // Reset the EditText.
        editText.setText("");
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_HISTORY_DATA, mLogHistory);
    }

    private void appendEntryToView(String text, long timestamp) {
        Date date = new Date(timestamp);
        if (!mIsHistoryEmpty) {
            mHistoryTextView.append("\n");
        } else {
            mHistoryTextView.setText("");
        }

        mHistoryTextView.append(String.format("%s [%s]", text, mSimpleDateFormatter.format(date)));

        mIsHistoryEmpty = false;
    }
}
