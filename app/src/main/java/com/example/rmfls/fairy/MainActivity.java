package com.example.rmfls.fairy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmfls.fairy.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView fairytext;
    TextView speechtext;
    TextToSpeech tts;
    ImageView image;

    com.example.rmfls.fairy.TagManage tagManage = new com.example.rmfls.fairy.TagManage();//태그를 담고 단어와 태그 를 비교 해주는
    Intent it;
    String fres;
    String fairyName;

    boolean info = false;
    int textpage = 0;
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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");//음성인식 정보 담을 저장소

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//음성인식 객체
        mRecognizer.setRecognitionListener(recognitionListener);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {//음성합성객체
            @Override
            public void onInit(int status) {

                tts.setLanguage(Locale.KOREAN);
            }
        });

        it = getIntent();
        int select = it.getExtras().getInt("selectfairy");//선택한 동화 표시

        switch (select) {//동화목록 선택 동화 설정
            case 1:
                fairyName = "rabbit_tortoise";
                break;
            case 2:
                fairyName = "sun_moon";
                break;
            case 3:
                fairyName = "uturi";
                break;
        }

        it = new Intent(MainActivity.this, com.example.rmfls.fairy.Fairybase.class);//테그메니저에 태그 입력
        it.putExtra("tagmanage", tagManage);
        it.putExtra("selectfairy", select);
        startActivity(it);//테그메니저 시작

        image = (ImageView) findViewById(R.id.image);
        fairytext = (TextView) findViewById(R.id.fairytext);
        speechtext = (TextView) findViewById(R.id.speechtext);

        Button button = (Button) findViewById(R.id.speechButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info == true) {
                    rerepage();
                    info = false;
                    speechtext.setText("");
                    Toast.makeText(MainActivity.this, "본화면", Toast.LENGTH_LONG).show();
                }
                else {
                    mRecognizer.startListening(intent);//음성인식객체를 음성인식 정보소를 넣어서 실행
                    Toast.makeText(MainActivity.this, "음성인식", Toast.LENGTH_LONG).show();
                }
            }
        });
        ImageButton prebutton = (ImageButton) findViewById(R.id.prepage);
        prebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textpage != 0)
                    textpage--;
                rerepage();
            }
        });
        ImageButton nextbutton = (ImageButton) findViewById(R.id.nextpage);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textpage != 24)
                    textpage++;
                rerepage();
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
            speechtext.setText("너무 늦게 말하면 오류뜹니다");
        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];//유사단어 여러개 뽑힘
            mResult.toArray(rs);     //뽑힌 단어 입력

            speechtext.setText(rs[0]);//가장 정확하다고 생각되는 것 출력
            // text.append(rs[1]"\n");
            Answer(rs[0], speechtext);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    //해야 할거: 제대로 읽었으면 다음 구절 읽어오기, 태그 뽑기, 태그 비교, 비교후 주소 뽑아서 가져오기
    private void Answer(String input, TextView txt) {//음성 합성,재생 //// 입력한 음성과 그 텍스트
        String mail = input;
        String note[] = mail.split(" ");// 단어를 띄어쓰기 구분으로 자름
        String node = "";

        if (fairytext.getText().toString().equals(input) == true) {
            textpage++;
            rerepage();
        }

        for (int i = 0; i < note.length; i++) {//글자를 쪼개 단어를 추출
            node = tagManage.Compare(note[i]);
            if (node.equals("false")) {
            } else {
                it = new Intent(this, com.example.rmfls.fairy.Fairybase.class);
                it.putExtra("ftag", node);
                startActivityForResult(it, 0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                fres = data.getStringExtra("fres");//이미지 이름
                info(fres);
            }
        }
    }

    void rerepage() {
        if (fairyName.equals("rabbit_tortoise")) {
            fairytext.setText(getResources().getString(R.string.토끼와거북01 + textpage));
            if (textpage <= 2) {
                image.setImageResource(R.drawable.first);
            }
            else if (textpage <= 5 && textpage>2) {
                image.setImageResource(R.drawable.first1);
            }
            else if (textpage <= 8 && textpage>5) {
                image.setImageResource(R.drawable.first2);
            }
            else if (textpage <= 11 && textpage>8) {
                image.setImageResource(R.drawable.second);
            }
        }
        if (fairyName.equals("sun_moon")) {
//햇님달님 구현
        }
    }

    void info(String fress) {
        switch (fress) {
            case "rabbit":
                fairytext.setText(getResources().getString(R.string.rabbitname));
                speechtext.setText(getResources().getString(R.string.rabbitinfo));
                image.setImageResource(R.drawable.rabbit);
                break;
            case "tortoise":
                fairytext.setText(getResources().getString(R.string.tortoisename));
                speechtext.setText(getResources().getString(R.string.tortoiseinfo));
                image.setImageResource(R.drawable.tortoise);
                break;
        }
        info = true;
    }
}