package com.example.avi.firebaseauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackForm extends AppCompatActivity implements View.OnClickListener {

    RatingBar ratingBar_feedback;
    TextView ratingText_feedback;
    EditText editText_feedback;
    Button buttonFeedbackSave, buttonFeedbackCancel;
    String feedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);
        ratingBar_feedback = (RatingBar) findViewById(R.id.feedback_ratingBar);
        ratingText_feedback = (TextView) findViewById(R.id.rating_text);
        buttonFeedbackSave = (Button) findViewById(R.id.btn_feedback_submit);
        buttonFeedbackCancel = (Button) findViewById(R.id.btn_feedback_cancel);
        editText_feedback = (EditText) findViewById(R.id.et_feedback_text);
        buttonFeedbackSave.setOnClickListener(this);
        buttonFeedbackCancel.setOnClickListener(this);

        ratingBar_feedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String rate = null;
                float rating = ratingBar_feedback.getRating();
                if (rating >= 4.1) {
                    rate = "Excellent";
                } else if (rating >= 3.1) {
                    rate = "Very Good";
                } else if (rating >= 2.1) {
                    rate = "Average";
                } else if (rating >= 1.1) {
                    rate = "Good";
                } else if (rating >= 0.1) {
                    rate = "Poor";
                }
                ratingText_feedback.setText(rate);
                ratingText_feedback.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_feedback_submit:
                validateFeedback();
                break;
            case R.id.btn_feedback_cancel:
                finish();
                break;
        }
    }

    private void validateFeedback() {
        feedbackText = editText_feedback.getText().toString();
        if (feedbackText.isEmpty()) {
            editText_feedback.setError("Give some feedback");
            editText_feedback.requestFocus();
            return;
        }
        feedbackSubmit();

    }


    private void feedbackSubmit() {
        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
        finish();
    }
}