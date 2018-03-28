package com.example.avi.firebaseauth;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackFormFragment extends Fragment implements View.OnClickListener {

    View view;

    RatingBar ratingBar_feedback;
    TextView ratingText_feedback;
    EditText editText_feedback;
    Button buttonFeedbackSave, buttonFeedbackCancel;
    String feedbackText;
    ProgressBar progressBar;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback_form, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ratingBar_feedback = (RatingBar) view.findViewById(R.id.feedback_ratingBar);
        ratingText_feedback = (TextView) view.findViewById(R.id.rating_text);
        buttonFeedbackSave = (Button) view.findViewById(R.id.btn_feedback_submit);
        buttonFeedbackCancel = (Button) view.findViewById(R.id.btn_feedback_cancel);
        editText_feedback = (EditText) view.findViewById(R.id.et_feedback_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_feedback);
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
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_feedback_submit:
                validateFeedback();
                break;
            case R.id.btn_feedback_cancel:
                break;
        }
    }

    private void validateFeedback() {
        feedbackText = editText_feedback.getText().toString();
        float rating = ratingBar_feedback.getRating();
        if (feedbackText.isEmpty()) {
            editText_feedback.setError("Give some feedback");
            editText_feedback.requestFocus();
            return;
        }
        if (rating <= 0) {
            Snackbar.make(view, "Please give some rating", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        feedbackSubmit();
    }


    private void feedbackSubmit() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mAuth.getCurrentUser();
        String username = user.getDisplayName();
        Map<String, Object> feedbackObjj = new HashMap<>();
        feedbackObjj.put("user", username);
        feedbackObjj.put("feedback Text", feedbackText);
        feedbackObjj.put("ratings", ratingBar_feedback.getRating());

        db.collection("Feedback Database")
                .add(feedbackObjj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                        getActivity().getFragmentManager().popBackStack();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error while uploading your feedback !\nTry after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


