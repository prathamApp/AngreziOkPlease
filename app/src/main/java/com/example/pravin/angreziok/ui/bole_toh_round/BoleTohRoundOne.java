package com.example.pravin.angreziok.ui.bole_toh_round;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playMusic;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playTTS;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.sdCardPathString;


public class BoleTohRoundOne extends BaseFragment implements BoleTohContract.BoleTohRoundOneView {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.ll_mic)
    LinearLayout layout_mic;
    @BindView(R.id.iv_image1)
    ImageView iv_image1;
    @BindView(R.id.iv_image2)
    ImageView iv_image2;
    @BindView(R.id.iv_image3)
    ImageView iv_image3;
    @BindView(R.id.iv_image4)
    ImageView iv_image4;
    @BindView(R.id.ib_speaker)
    ImageButton ib_speaker;

    @BindView(R.id.bt_temp_skip)
    Button bt_temp_skip;

    JSONArray questionData;
    GenericModalGson gsonPicGameData;
//    ArrayList <GenericModalGson> gsonPicGameData = new ArrayList<GenericModalGson>();
    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    int readQuestionNo;
    String ttsQuestion;
    float speechRate = 1.0f;
    int[] integerArray = new int[4];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bole_toh_round_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setOnClickListeners();
        doInitialWork();
/*        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
        customCountDownTimer.startTimer(10000);*/
    }

    private void setOnClickListeners() {

        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.success();
            }
        });

        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
            }
        });

        bt_temp_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD_Utility.showFragment(getActivity(), new BoleTohRoundTwo(), R.id.cl_bole_toh,
                        null, BoleTohRoundTwo.class.getSimpleName());
            }
        });

        iv_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image1.startAnimation(pop);*/
                checkAnswer(1);
            }
        });

        iv_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(2);
            }
        });

        iv_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(3);
            }
        });

        iv_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(4);
            }
        });


    }

    @Override
    public int[] getUniqueRandomNumber(int min, int max, int numSize) {
        int[] tempArray;
        if ((max - min) > numSize) {
            tempArray = new int[numSize];
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int i = min; i < max; i++)
                list.add(new Integer(i));

            Collections.shuffle(list);
            for (int i = 0; i < numSize; i++) {
                System.out.println("===== : " + list.get(i));
                tempArray[i] = list.get(i);
            }
            return tempArray;
        } else
            return null;
    }

    @Override
    public void doInitialWork() {
        gsonPicGameData = fetchJsonData("RoundOneGameOne");
        integerArray = getUniqueRandomNumber(0, gsonPicGameData.getNodelist().size(), 4);
    }

    public GenericModalGson fetchJsonData(String jasonName) {
        GenericModalGson returnGsonData = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString+"JsonFiles/"+jasonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObj = new JSONObject(new String(buffer));
            Gson gson = new Gson();
            returnGsonData = gson.fromJson(jsonObj.toString(), GenericModalGson.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnGsonData;
    }

    public void showImages(int[] integerArray) {
        try {
            readQuestionNo = getRandomNumber(0, 4);
            String imagePath = sdCardPathString + "PicGameImages/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            for (int i = 0; i < 4; i++) {
                resTextArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceText"));
                resImageArray.add("PicGameImages/" + questionData.getJSONObject(integerArray[i]).getString("resourceImage"));
                resAudioArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceAudio"));
                resIdArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceId"));
            }
            Bitmap[] bitmap = {BitmapFactory.decodeFile(imagePath + resImageArray.get(0))};
            iv_image1.setImageBitmap(bitmap[0]);
            bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(1))};
            iv_image2.setImageBitmap(bitmap[0]);
            bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(2))};
            iv_image3.setImageBitmap(bitmap[0]);
            bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(3))};
            iv_image4.setImageBitmap(bitmap[0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    readQuestion(readQuestionNo);
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readQuestion(int questionToRead) {
        ttsQuestion = resTextArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate);
            playTTS.ttsFunction("Where Is "+ttsQuestion, "hin", speechRate);
//            playMusic("StoriesAudio/"+ resAudioArray.get(questionToRead));
    }

    @Override
    public void checkAnswer(int imageViewNum) {
        String imageString = resTextArray.get(imageViewNum - 1);

        if (imageString.equalsIgnoreCase(ttsQuestion)) {
            playMusic("correct.mp3");

        } else {
            playMusic("wrong.mp3");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showImages(integerArray);
            }
        }, 1000);
    }
}


