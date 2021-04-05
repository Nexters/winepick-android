package kr.co.nexters.winepick.ui.survey;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.data.constant.Constant;
import kr.co.nexters.winepick.data.repository.SearchRepositoryKt;
import kr.co.nexters.winepick.data.repository.WinePickRepository;
import kr.co.nexters.winepick.data.response.PersonalityType;
import kr.co.nexters.winepick.databinding.ActivitySurveyResultBinding;
import kr.co.nexters.winepick.di.AuthManager;
import kr.co.nexters.winepick.ui.base.BaseActivity;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import kr.co.nexters.winepick.ui.component.ConfirmDialog;
import kr.co.nexters.winepick.ui.search.SearchActivity;
import kr.co.nexters.winepick.util.DisplayExtKt;
import kr.co.nexters.winepick.util.ViewExtKt;
import timber.log.Timber;


public class SurveyResultActivity extends BaseActivity<ActivitySurveyResultBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public SurveyResultActivity() {
        super(R.layout.activity_survey_result);
    }

    WinePickRepository winePickRepository = org.koin.java.KoinJavaComponent.inject(WinePickRepository.class).getValue();
    AuthManager authManager = org.koin.java.KoinJavaComponent.inject(AuthManager.class).getValue();

    /**
     * ViewModel 을 사용하지 않으므로 null 리턴
     */
    @Nullable
    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] alphabets = getResources().getStringArray(R.array.personality_alphabet);
        int index = java.util.Arrays.asList(alphabets).indexOf(authManager.getTestType());

        winePickRepository.getResult(index, (personalityType -> {
            onCreateAfterGetResult(personalityType);
            return Unit.INSTANCE;
        }), (() -> {
            finish();
            return Unit.INSTANCE;
        }));

        //getHashKey();

        // 백버튼 클릭 리스너
        ViewExtKt.setOnSingleClickListener(binding.backArrowButton, () -> {
            onBackPressed();
            return Unit.INSTANCE;
        });
    }

    public void onCreateAfterGetResult(PersonalityType personalityType) {
        StringBuilder title = new StringBuilder();
        title.append(personalityType.getPersonDetail());
        title.append(", ");

        StringBuilder matchTypeName = new StringBuilder();
        int myTypeResource = 0;
        int matchTypeResource = 0;
        switch (personalityType.getAnswerType()) {
            case "A":
                title.append(getResources().getString(R.string.type_a_name));
                matchTypeName.append(getResources().getString(R.string.type_e_detail));
                myTypeResource = R.drawable.img_test_mid_a;
                matchTypeResource = R.drawable.img_test_small_e;
                break;
            case "B":
                title.append(getResources().getString(R.string.type_b_name));
                matchTypeName.append(getResources().getString(R.string.type_f_detail));
                myTypeResource = R.drawable.img_test_mid_b;
                matchTypeResource = R.drawable.img_test_small_f;
                break;
            case "C":
                title.append(getResources().getString(R.string.type_c_name));
                matchTypeName.append(getResources().getString(R.string.type_d_detail));
                myTypeResource = R.drawable.img_test_mid_c;
                matchTypeResource = R.drawable.img_test_small_d;
                break;
            case "D":
                title.append(getResources().getString(R.string.type_d_name));
                matchTypeName.append(getResources().getString(R.string.type_c_detail));
                myTypeResource = R.drawable.img_test_mid_d;
                matchTypeResource = R.drawable.img_test_small_c;

                break;
            case "E":
                title.append(getResources().getString(R.string.type_e_name));
                matchTypeName.append(getResources().getString(R.string.type_a_detail));
                myTypeResource = R.drawable.img_test_mid_e;
                matchTypeResource = R.drawable.img_test_small_a;
                break;
            case "F":
                title.append(getResources().getString(R.string.type_f_name));
                matchTypeName.append(getResources().getString(R.string.type_b_detail));
                myTypeResource = R.drawable.img_test_mid_f;
                matchTypeResource = R.drawable.img_test_small_b;
                break;
            default:
                return;
        }
        // 이미지 설정
        binding.tendencyImage.setImageResource(myTypeResource);

        // 제목 설정
        binding.tendencyText2.setText(title.toString());

        // 본문 설정
        binding.tendencyContent.setText(personalityType.getDescription());

        // 나와 어울리는 와인 설정
        binding.myWineResultText.setText(personalityType.getMatchWine());
        ViewExtKt.setOnSingleClickListener(binding.myWineResultText, () -> {
            // TODO 추후 아래 내용 추가 구현 필요
            // Intent intent = new Intent(this, WineDetailActivity.class);
            // intent.putExtra("wineData", wineResult);
            // startActivity(intent);
            return Unit.INSTANCE;
        });

        // 나와 잘 맞는 타입 설정
        binding.myTypeImage2.setImageResource(matchTypeResource);
        binding.myTypeResultText.setText(matchTypeName.toString());
        // 나와 잘 맞는 타입 클릭 리스너
        ViewExtKt.setOnSingleClickListener(binding.myTypeImage2, () -> {
            // onBackPressed();
            return Unit.INSTANCE;
        });

        // 와인 추천 키워드1 설정
        String[] keyword1s = personalityType.getKeyword1().split(",");
        if (keyword1s.length != 0) binding.recommendMoveText1.setText(keyword1s[0]);
        ViewExtKt.setOnSingleClickListener(binding.recommendMoveButtonLinear1, () -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(Constant.STRING_EXTRA_SEARCH_FILTERS, personalityType.getKeyword1());
            startActivity(intent);
            return Unit.INSTANCE;
        });
        // 와인 추천 키워드2 설정
        String[] keyword2s = personalityType.getKeyword2().split(",");
        if (keyword2s.length != 0) binding.recommendMoveText2.setText(keyword2s[0]);
        ViewExtKt.setOnSingleClickListener(binding.recommendMoveButtonLinear2, () -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(Constant.STRING_EXTRA_SEARCH_FILTERS, personalityType.getKeyword2());
            startActivity(intent);
            return Unit.INSTANCE;
        });

        // 다시 테스트 하기 클릭 리스너
        ViewExtKt.setOnSingleClickListener(binding.btnReplaySurvey, () -> {
            initReSurveyNotiDialog();
            return Unit.INSTANCE;
        });

        // 공유버튼 클릭 리스너
        ViewExtKt.setOnSingleClickListener(binding.shareButton, () -> {
            ConfirmDialog dialog = new ConfirmDialog(
                    DisplayExtKt.dpToPx(241),
                    DisplayExtKt.dpToPx(202),
                    "카카오톡 공유로 이동합니다",
                    "결과를 카카오톡에서\n친구에게 공유해보세요!",
                    "취소",
                    null,
                    "이동하기",
                    ((dialogFragment) -> {
                        shareBtnClick(personalityType);
                        dialogFragment.dismiss();
                        return Unit.INSTANCE;
                    }),
                    false
            );
            dialog.show(this.getSupportFragmentManager(), "KakaoShare");

            return Unit.INSTANCE;
        });
    }

    public void initReSurveyNotiDialog() {
        ConfirmDialog dialog = new ConfirmDialog(
                DisplayExtKt.dpToPx(241),
                DisplayExtKt.dpToPx(202),
                "재검사 하시겠습니까?",
                "재검사시,\n이전 결과는 삭제됩니다.",
                "취소",
                null,
                "검사하기",
                ((dialogFragment) -> {
                    authManager.setTestType("N");
                    Intent intent = new Intent(this, SurveyActivity.class);
                    intent.putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return Unit.INSTANCE;
                }),
                false
        );
        dialog.show(this.getSupportFragmentManager(), "SurveyReset");
    }

    public void shareBtnClick(PersonalityType personalityType) {
        List<String> hashTags1 = SearchRepositoryKt.parseKeyword(personalityType.getKeyword1());
        List<String> hashTags2 = SearchRepositoryKt.parseKeyword(personalityType.getKeyword2());

        StringBuilder description = new StringBuilder();
        for (String hashTag1 : hashTags1) {
            description.append("#");
            description.append(hashTag1);
            description.append(" ");
        }
        for (String hashTag2 : hashTags2) {
            description.append("#");
            description.append(hashTag2);
            description.append(" ");
        }

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("내 와인 타입은? " + personalityType.getPersonDetail(),
                        "https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/winepick/kakao_share_img_" + personalityType.getAnswerType() + ".png",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(personalityType.getDescription())
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Timber.e("KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Timber.d("KeyHash%s", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Timber.d("KeyHash Unable to get MessageDigest. signature=%s", signature);
                Timber.e(e);
            }
        }
    }
}
