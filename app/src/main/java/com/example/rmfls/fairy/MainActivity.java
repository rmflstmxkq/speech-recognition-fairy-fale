package com.example.rmfls.fairy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView textView;
    TextToSpeech tts;

    TagManage tagManage = new TagManage();//태그를 담고 단어와 태그 를 비교 해주는
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(getApplicationContext(), "미지원", Toast.LENGTH_LONG).show();//사용 가능한지 확인
        }
        if (ContextCompat.checkSelfPermission(this,//사용권한을 얻기 위한 팝업
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
        textView = (TextView) findViewById(R.id.textView);//택스트 찾아서 인식

        Button button = (Button) findViewById(R.id.button01);//버튼 찾아서 인식

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 버튼 누르며 실행
                mRecognizer.startListening(intent);
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            textView.setText("너무 늦게 말하면 오류뜹니다");
            Answer("너무 늦게 말하면 오류뜹니다", textView);
        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];//유사단어 여러개 뽑힘
            mResult.toArray(rs);     //뽑힌 단어 입력

            textView.setText(rs[0]);//가장 정확하다고 생각되는 것 출력
            // text.append(rs[1]"\n");
            Answer(rs[0], textView);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    private void Answer(String input, TextView txt) {//음성 합성,재생
        String mail = input;
        String note[] = mail.split(" ");// 단어를 띄어쓰기 구분으로 자름
        String page = "";
        String node = "";

        for (int i = 0; i < note.length; i++) {//글자를 쪼개 단어를 추출 page에 기입
            node = tagManage.Compare(note[i]);
            if (node.equals("false")) {
            } else
                page = page + "#" + node;
        }
        //Toast.makeText(this, page, Toast.LENGTH_LONG).show();//테그 출력

        Intent it = new Intent(this, Fairybase.class);
        it.putExtra("tagmanage", tagManage);
        startActivity(it);

        // String bb=fdb.;
        // Toast.makeText(this, bb, Toast.LENGTH_LONG).show();

       /* try {
            if(input.equals("")){
            txt.append("\n");
            tts.speak(input, TextToSpeech.QUEUE_FLUSH, null);
            }
        }catch(Exception e){}*/
    }
}