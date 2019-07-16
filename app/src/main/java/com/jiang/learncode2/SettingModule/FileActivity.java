package com.jiang.learncode2.SettingModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.learncode2.DataBase.bean.Question;
import com.jiang.learncode2.DataBase.bean.UserFile;
import com.jiang.learncode2.MainActivity;
import com.jiang.learncode2.R;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

public class FileActivity extends AppCompatActivity {

    private AppCompatActivity mActivity;
    private final int EX_FILE_PICKER_RESULT = 0xfa01;
    private String username;
    private static File curPath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_file);



        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        System.out.println("username"+username);

        //设置“更多”按钮点击事件
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExFilePicker exFilePicker = new ExFilePicker();
                exFilePicker.setCanChooseOnlyOneItem(true); //设置仅能单选文件
                exFilePicker.setQuitButtonEnabled(true);

                exFilePicker.setStartDirectory(Environment.getExternalStorageDirectory().getPath());
                exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
                exFilePicker.start(mActivity, EX_FILE_PICKER_RESULT);
            }
        });

        //设置“上传文件”按钮点击事件
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText filePath = (EditText) findViewById(R.id.filePath);
                final String path = filePath.getText().toString();
                uploadFile(path);
            }
        });

        //设置“下载文件”按钮点击事件
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //模拟获取用户名，需要修改代码！！！！！
                //已修改
                searchFile(username);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText filePath = (EditText) findViewById(R.id.filePath);
        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                String path = result.getPath();
                List<String> names = result.getNames();
                for (int i = 0; i < names.size(); i++) {
                    File f = new File(path, names.get(i));
                    try {
                        Uri uri = Uri.fromFile(f); //这里获取了真实可用的文件资源
                        Toast.makeText(mActivity, "选择文件:" + uri.getPath(), Toast.LENGTH_SHORT).show();
                        filePath.setText(uri.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //上传文件方法
    protected void uploadFile(String path) {
        //模拟获取用户名，需要修改代码！！！！！
        //已修改
        final String name = username;
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(FileActivity.this, "上传文件成功" + bmobFile.getFileUrl(), Toast.LENGTH_LONG).show();
                    UserFile userFile = new UserFile();
                    userFile.setF_U_name(name);
                    userFile.setFile(bmobFile);
                    userFile.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(FileActivity.this, "添加数据成功，：", Toast.LENGTH_SHORT).show();
                                System.out.println("添加数据成功");
                            } else {
                                Toast.makeText(FileActivity.this, "添加数据失败", Toast.LENGTH_SHORT).show();
                                System.out.println("添加数据失败");
                            }
                        }
                    });
                } else {
                    Toast.makeText(FileActivity.this, "文件上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找文件方法
    protected void searchFile(String name) {

        final BmobQuery<UserFile> query = new BmobQuery<UserFile>();
        //query.addWhereEqualTo("Q_topic", "1111");
        query.addWhereEqualTo("F_U_name",String.valueOf(name));

        query.findObjects(new FindListener<UserFile>() {
            @Override
            public void done(List<UserFile> list, BmobException e) {
                if (e == null) {

                    if (list != null && list.size()>0) {
                        for (int i = 0; i<list.size(); i++) {
                            UserFile userFile = list.get(i);
                            String name1 = userFile.getFile().getFilename();
                            String url = userFile.getFile().getUrl();
                            Toast.makeText(FileActivity.this, name1 + url + "", Toast.LENGTH_LONG).show();
                            BmobFile file = new BmobFile(name1, "", url);
                            System.out.println(url);
                            downloadFile(file);
                        }
                    } else {
                        Toast.makeText(FileActivity.this, "查询成功，但无数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //下载文件方法
    protected void downloadFile(BmobFile file) {
        File HOME_PATH=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LearnCode",file.getFilename());
        if (curPath == null)//为空才重新设置目录，这样可以打开上次打开的目录
        {
            curPath = HOME_PATH;
            if(!curPath.exists())
            {
                curPath.mkdirs();
            }
        }
        file.download(HOME_PATH, new DownloadFileListener() {
            @Override
            public void onStart() {
                Toast.makeText(FileActivity.this, "开始下载", Toast.LENGTH_LONG).show();
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    System.out.println("下载成功,保存路径:" + savePath);
                    Toast.makeText(FileActivity.this, "下载成功,保存路径:" + savePath, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FileActivity.this, "下载失败：" + e.getErrorCode() + "," + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }
        });
    }
}
