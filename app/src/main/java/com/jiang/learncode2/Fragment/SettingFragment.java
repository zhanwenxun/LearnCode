package com.jiang.learncode2.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiang.learncode2.LoginActivity;
import com.jiang.learncode2.MessageEvent;
import com.jiang.learncode2.R;
import com.jiang.learncode2.SettingModule.RecordActivity;
import com.jiang.learncode2.SettingModule.FileActivity;
import com.jiang.learncode2.SettingModule.FunctionMessage;
import com.jiang.learncode2.SettingModule.ModifyPwdActivity;
import com.jiang.learncode2.SettingModule.message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */

public class SettingFragment extends Fragment{

    private List<FunctionMessage> messageList = new ArrayList<>();
    private View rootView;
    private Boolean flag;
    private String username = null;
    private TextView user;
    private Bundle savebundle;
    private  String result = null;
    private Boolean begined = false;
    private Timer timer;
    private Handler handler;
    private LayoutInflater inflater;
    private boolean status = true;
    private static final String picUrl = "https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwiBva_nieDhAhXQF4gKHXYVAR8QjRx6BAgBEAU&url=https%3A%2F%2Fbaike.baidu.com%2Fitem%2F%25E5%259B%25BE%25E7%2589%2587&psig=AOvVaw13NS0CaCAqcVLWRyByTw6g&ust=1555897982581245";



    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;


        rootView = inflater.inflate(R.layout.fragment_setting, container,false);

        //Toast.makeText(getContext(),""+111,Toast.LENGTH_LONG).show();


        InitViewButton();
        InitView();


        //动态刷新textView
        handler = new Handler()
        {

            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                if(begined == true)
                {
                    user = (TextView)getActivity().findViewById(R.id.text_user);
                    user.setText(""+msg.obj.toString());
                    //begined = false;
                    timer.cancel();
                }

            }
        };
        // Log.d("A",""+)
        return rootView;
    }


    private void InitViewButton() {
        /*Button button1 = (Button) rootView.findViewById(R.id.eye);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "我的关注", Toast.LENGTH_LONG).show();

            }
        });
        Button button2 = (Button) rootView.findViewById(R.id.shoucang);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "我的收藏", Toast.LENGTH_LONG).show();
            }
        });
        Button button3 = (Button) rootView.findViewById(R.id.caogao);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "我的草稿", Toast.LENGTH_LONG).show();

            }
        });
        Button button4 = (Button) rootView.findViewById(R.id.liulan);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "最近浏览", Toast.LENGTH_LONG).show();
            }
        });*/
        Button button5 = (Button) rootView.findViewById(R.id.ziliao);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FileActivity.class);
                if (savebundle == null){
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_LONG).show();

                }else {
                    intent.putExtra("username",String.valueOf(savebundle.get("username")));
                    startActivity(intent);
                }

                //Toast.makeText(getApplicationContext(), "我的资料", Toast.LENGTH_LONG).show();
            }
        });
        Button button6 = (Button) rootView.findViewById(R.id.tuichu);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "退出登录", Toast.LENGTH_LONG).show();
                //exit(0);
                status = false;
                user = (TextView)getActivity().findViewById(R.id.text_user);
                user.setText("Login/Register");
            }
        });


        Button button7 = (Button) rootView.findViewById(R.id.xiugaimima);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savebundle.getString("username")==null)
                {
                    Toast.makeText(getApplicationContext(),"请先登录账号！！！",Toast.LENGTH_SHORT).show();

                }else {
                    Intent mIntent = new Intent(getActivity().getApplicationContext(), ModifyPwdActivity.class);
                    mIntent.putExtra("username",savebundle.getString("username"));
                    startActivity(mIntent);
                }
                Toast.makeText(getApplicationContext(), "修改密码", Toast.LENGTH_LONG).show();
                //exit(0);

                //startActivityForResult(mIntent,1);
            }
        });
        Button button8 = (Button) rootView.findViewById(R.id.jilu);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (savebundle == null){
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_LONG).show();
                }else {

                    Intent mIntent = new Intent(getActivity(), RecordActivity.class);
                    mIntent.putExtra("username",savebundle.getString("username"));
                    Toast.makeText(getApplicationContext(), "发布记录", Toast.LENGTH_LONG).show();
                    startActivity(mIntent);
                }

            }
        });



    }

        //登录
    private void login() {

        flag = true;
        Intent mIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        status = true;
        startActivityForResult(mIntent,1);

    }


    //初始化界面
    private void InitView() {
        CircleImageView view = (CircleImageView) rootView.findViewById(R.id.headview);
        Glide.with(getContext()).load(picUrl).placeholder(R.drawable.profile).into(view);
        user = (TextView)getActivity().findViewById(R.id.text_user);

        //当存储的bundle为空时，提示用户进行登录，
        //不为空时，直接读取bundle中的用户数据，动态刷新在主界面上，并在刷新完成后关掉线程；
       if ( savebundle == null || status == false) {
           begined = true;
           timer = new Timer();
           timer.schedule(new TimerTask()
           {
               @Override
               public void run()
               {
                   // TODO Auto-generated method stub
                   Message message = new Message();
                   String s = "Login/Register";
                   message.obj = (String) s;

                   handler.sendMessage(message);
               }
           }, 100, 3);

       }
       else{
           //Log.d("save1",savebundle.getString("username"));
           //Log.d("save2",result);
           begined = true;
           timer = new Timer();
           timer.schedule(new TimerTask()
           {
               @Override
               public void run()
               {
                   // TODO Auto-generated method stub
                   Message message = new Message();
                   message.obj = (String) result;
                   //MessageEvent.what = Integer.parseInt((String)result);
                   handler.sendMessage(message);
               }
           }, 100, 3);        //从1000ms即1s开始，30ms为数字改变周期
       }

       view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   login();
               }
           });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        saveStateToArguments();

    }

    private void saveStateToArguments() {
        savebundle = savedState();
       // Log.d("save",""+savebundle.getString("username"));

    }

    //保存临时数据
    private Bundle savedState() {

        Bundle state = new Bundle();

        if (result == null){
            return null;
        }
        //Log.d("linshi","L1");
            state.putString("username",result);
        state.putBoolean("flag", true);

        return state;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        saveStateToArguments();


    }


    //intent返回的用户昵称等数据;
    //在之后forum界面使用时可直接在此调用
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        result = data.getExtras().getString("username");//得到新Activity 关闭后返回的数据
       // String result = data.getExtras().getString("username");
        //System.out.println(result);
        user = getActivity().findViewById(R.id.text_user);
        user.setText(result);
        savebundle = new Bundle();
        savebundle.putString("username",result);
        savebundle.putBoolean("flag",true);

        EventBus.getDefault().postSticky(
                new MessageEvent(String.valueOf(savebundle.getString("username"))));

    }

}
