package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import kr.co.nexters.winepick.R;

public class SurveyFragment extends Fragment {
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
