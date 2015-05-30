package geoquiz.android.bignerdranch.com.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;


public class QuizActivity extends ActionBarActivity {

    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "cheated";
    private static final String TAG = "QuizActivity";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private TextView mApiLevelTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_chaco, true),
            new TrueFalse(R.string.question_corrientes, true),
            new TrueFalse(R.string.question_iorio, true),
            new TrueFalse(R.string.question_ocampo, false),
            new TrueFalse(R.string.question_santafe, false)
    };

    private boolean[] mCheatedBank = new boolean[]{
            false,
            false,
            false,
            false,
            false
    };

    private int mCurrentIndex = 0;

    private void updateQuestion(){
      //Log.d(TAG, "Current question index =" + mCurrentIndex );
      try {
          int question = mQuestionBank[mCurrentIndex].getQuestion();
          mQuestionTextView.setText(question);
      } catch (ArrayIndexOutOfBoundsException ex){
          Log.e(TAG, "Index was out of bounds, ex");
      }
    }

    private void checkAnswer(boolean userPressedTrue){
            int messageResId = 0;
            if (mCheatedBank[mCurrentIndex]){
                messageResId = R.string.judgement_toast;
            } else {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }
            Toast.makeText(
                this,
                messageResId,
                Toast.LENGTH_SHORT).show();
    }
    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)&(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN)) {
            ActionBar actionBar = getActionBar();
            actionBar.setSubtitle("Capitales");
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               checkAnswer(true);
           }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               checkAnswer(false);
           }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (mCurrentIndex == 0){
                    mCurrentIndex = (mQuestionBank.length - 1);
                } else {
                    mCurrentIndex = (mCurrentIndex - 1);
                }
                updateQuestion();
            }
        });

        mApiLevelTextView = (TextView)findViewById(R.id.api_level);
        mApiLevelTextView.setText("API level " + Build.VERSION.SDK_INT);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheatedBank = savedInstanceState.getBooleanArray(KEY_CHEATER);
        }
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data == null){
            return;
        }
        mCheatedBank[mCurrentIndex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_CHEATER, mCheatedBank);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
