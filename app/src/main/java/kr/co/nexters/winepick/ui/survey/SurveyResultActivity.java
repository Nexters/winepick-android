package kr.co.nexters.winepick.ui.survey;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

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
import java.util.Map;
import kotlin.Unit;
import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.databinding.ActivitySurveyResultBinding;
import kr.co.nexters.winepick.ui.base.BaseActivity;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import kr.co.nexters.winepick.ui.component.ConfirmDialog;
import kr.co.nexters.winepick.util.DisplayExtKt;


public class SurveyResultActivity extends BaseActivity<ActivitySurveyResultBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public SurveyResultActivity() {
        super(R.layout.activity_survey_result);
    }

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
        setContentView(R.layout.activity_survey_result);
        //getHashKey();
    }

    public void initReSurveyNotiDialog(View view) {
        new ConfirmDialog(
                DisplayExtKt.dpToPx(241),
                DisplayExtKt.dpToPx(202),
                "재검사 하시겠습니까?",
                "재검사시,\n이전 결과는 삭제됩니다.",
                "아니요",
                null,
                "예",
                ((dialogFragment) -> {
                    Intent intent = new Intent(this, SurveyActivity.class);
                    startActivity(intent);
                    return Unit.INSTANCE;
                }),
                false
        );
    }

    public void kakaoLink(View view) {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("내 와인 타입은? 센치한 도도형",
                        "https://i.imgur.com/wFJcffR.png#.YEumL3qWoqg.link",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("#우아한 #쌉싸름한 #화이트와인")
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
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}
