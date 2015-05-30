package geoquiz.android.bignerdranch.com.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Roberto on 4/26/2015.
 */
public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = ".com.bignerdranch.android.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    private static final String KEY_CHEATER = "cheater";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;
    private Button mShowAnswer;
    private boolean mCheated = false;

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

        mShowAnswer = (Button)findViewById(R.id.show_answer_button);

        mApiLevelTextView = (TextView)findViewById(R.id.cheat_api_level);
        mApiLevelTextView.setText("API level " + Build.VERSION.SDK_INT);

        //Answer is not shown unless the user press the button.
        //Recovers the data if an orientation change happened.
        if (savedInstanceState != null){
            mCheated = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }
        setAnswerShownResult(mCheated);

        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mCheated = true;
                setAnswerShownResult(mCheated);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_CHEATER, mCheated);
    }

}
