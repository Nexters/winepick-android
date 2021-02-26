package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.co.nexters.winepick.R;

public class SurveyFragment extends Fragment {

    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SurveyFragment() {
        // Required empty public constructor
    }

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SurveyFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static SurveyFragment newInstance(String param1, String param2) {
        SurveyFragment fragment = new SurveyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    private static final String ARG_CURRENT_STAGE = "currentStage";

    private int currentStage;

    TextView questionText;
    TextView questionNum;
    TextView totalNum;
    TextView answerA;
    TextView answerB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.getBundle("currentStage");

        int i = getArguments().getInt("currentStage");
        Log.isLoggable("argument", i);

        if (getArguments() != null) {
            currentStage = getArguments().getInt("currentStage");
            Log.isLoggable("data: ", currentStage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        questionText = view.findViewById(R.id.survey_question_text);
        questionNum = view.findViewById(R.id.current_survey_number_text);
        totalNum = view.findViewById(R.id.all_survey_number_text);
        answerA = view.findViewById(R.id.survey_answer_A);
        answerB = view.findViewById(R.id.survey_answer_B);
        return view;
    }

    public String answerClick(View view) {
        switch (view.getId()) {
            case R.id.survey_answer_A:
                return "A";
            case R.id.survey_answer_B:
                return "B";
        }
        return "";
    }

    public void setData(String text, String ansA, String ansB, String num) {
        Log.i(ansA,"tag");
        Log.i(ansB, "tag" );
        questionText.setText(text);
        questionNum.setText("0" + num);
        totalNum.setText("0" + num + " / 09");
        answerA.setText(ansA);
        answerB.setText(ansB);
    }
}