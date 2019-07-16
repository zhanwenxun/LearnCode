package com.jiang.learncode2.Fragment;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.learncode2.HomeModule.textwarrior.android.RecentFiles;
import com.jiang.learncode2.HomeModule.textwarrior.common.ReadThread;
import com.jiang.learncode2.HomeModule.textwarrior.common.WriteThread;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.activity.FileListActivity;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.activity.SettingActivity;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.interfaces.CompileCallback;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.interfaces.ExecCallback;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.interfaces.UnzipCallback;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.CFileNameFilter;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.ConstantPool;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.Setting;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.ShellUtils;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.Utils;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.view.SymbolView;
import com.jiang.learncode2.HomeModule.textwarrior.simpleC.view.TextEditor;
import com.jiang.learncode2.R;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private static final boolean DEBUG = false;
    private TextEditor editor;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    //SharedPreference
    private  static  final String KEY_FIRST_RUN="isFirstRun";
    private  static  final String KEY_APP_VERSION="appVersion";
    //Bundle
    private  static  final String KEY_FILE_PATH="filePath";
    private  static  final String KEY_FILE_CONTENT="fileContent";
    private  static  final String KEY_IS_MULTI_COMPILE="isMultiCompile";
    private  static  final String KEY_MULTI_COMPILE_FILES_NAME="multiCompileFilesName";
    private final  int MSG_INIT=0x100;
    private Setting setting;
    private SymbolView symbolView;

    private RecentFiles recentFiles;

    private  boolean isMultiFileCompile=false;
    private  String multiFilesName="";
    private LayoutInflater inflater;
    private Bundle savedState;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //onSaveInstanceState(savedInstanceState);
        // Inflate the layout for this fragment
        this.inflater = inflater;
        setHasOptionsMenu(true);//需要添加这行代码
        editor =new TextEditor(inflater.getContext());
        View view = editor;
        setting = Setting.getInstance(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("setting", MODE_PRIVATE);

        if (savedState != null) {
            editor.requestFocus();

            String openedFilePath = savedState.getString(KEY_FILE_PATH, "");
            if (!openedFilePath.equals("")) {
                editor.open(openedFilePath);
                setSubtitle(new File(openedFilePath).getName());
            } else {
                //没有打开文件时
                editor.setText(savedState.getString(KEY_FILE_CONTENT, ""));
            }
            multiFilesName = savedState.getString(KEY_MULTI_COMPILE_FILES_NAME, "");
            isMultiFileCompile = savedState.getBoolean(KEY_IS_MULTI_COMPILE, false);
        }
        init();
        return view;
    }

    private void setSubtitle(String title)
    {
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null)
        {
            actionBar.setSubtitle(title);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        savedState = savedState();
        Log.d("save",""+savedState.getString(KEY_FILE_CONTENT));
        editor.setText(savedState.getString(KEY_FILE_CONTENT, ""));


    }

    //保存临时数据
    private Bundle savedState() {
        Bundle state = new Bundle();
        Log.d("linshi","1");
        if(editor.getOpenedFile()!=null) {
            state.putString(KEY_FILE_PATH, editor.getOpenedFile().getAbsolutePath());
        }
        if(!editor.getText().toString().equals("")){
            Log.d("note",""+editor.getText().toString());
            state.putString(KEY_FILE_CONTENT, editor.getText().toString());
        }
        state.putBoolean(KEY_IS_MULTI_COMPILE, isMultiFileCompile);
        state.putString(KEY_MULTI_COMPILE_FILES_NAME, multiFilesName);
        return state;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Resume","resume");
        setting.update();//加载设置
        editor.setDark(setting.isDarkMode());
        showSymbolView();
        editor.setAutoCompete(setting.isAutoCompete());
        editor.setShowLineNumbers(setting.isShowLineNumber());
        editor.invalidate();

    }


    private void showSymbolView()
    {
        if(setting.isShowSymbolView()){
            symbolView.setVisible(true);
        }
        else
        {
            symbolView.setVisible(false);
        }
    }

    private void autoSave()
    {
        if(setting.isAutoSave()&&editor.getOpenedFile()!=null) {
            editor.save(editor.getOpenedFile().getAbsolutePath());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        autoSave();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog!=null)
            progressDialog.dismiss();
    }

    private  void init()
    {
        if(sharedPreferences.getBoolean(KEY_FIRST_RUN,true)
                ||!sharedPreferences.getString(KEY_APP_VERSION,"").equals(Utils.getAppVersion(getContext()))) {
            progressDialog=ProgressDialog.show(getContext(),"","初始化中，请等待...",false,false);
            new Thread() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream=getActivity().getAssets().open("gcc.zip");
                        //解压bin
                        Utils.unzip(inputStream, getActivity().getFilesDir(), new UnzipCallback() {
                            @Override
                            public void onResult(boolean success) {
                                if(success) {
                                    File binDir1=new File(getActivity().getFilesDir()+File.separator+"gcc"+File.separator+"bin");
                                    File binDir2=new File(getActivity().getFilesDir()+File.separator+"gcc"+File.separator+"arm-linux-androideabi"+File.separator+"bin");
                                    File binDir3=new File(getActivity().getFilesDir()+File.separator+"gcc"+File.separator+"libexec/gcc/arm-linux-androideabi/6.1.0");
                                    for(File f:binDir1.listFiles())
                                    {
                                        if(f.isFile())
                                            Utils.changeToExecutable(f);
                                    }
                                    for(File f:binDir2.listFiles())
                                    {
                                        if(f.isFile())
                                            Utils.changeToExecutable(f);
                                            Utils.changeToExecutable(f);
                                    }
                                    for(File f:binDir3.listFiles())
                                    {
                                        if(f.isFile())
                                            Utils.changeToExecutable(f);
                                    }
                                    try {
                                        //解压lib
                                        InputStream libInputStream = getActivity().getAssets().open("bin.zip");
                                        Utils.unzip(libInputStream, getActivity().getFilesDir(), new UnzipCallback() {
                                            @Override
                                            public void onResult(boolean success) {
                                                if(success){
                                                    File f=new File(getActivity().getFilesDir()+"/indent");
                                                    Utils.changeToExecutable(f);
                                                    handler.sendMessage(Message.obtain(handler,MSG_INIT,true));
                                                }
                                                else{
                                                    handler.sendMessage(Message.obtain(handler,MSG_INIT,false));
                                                }
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else
                                {
                                    handler.sendMessage(Message.obtain(handler,MSG_INIT,false));
                                }
                            }
                        });//解压bin

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        //加载头文件名
        ArrayList<String> cHeader=Utils.getCHeader(getContext());
        String[] arr1=new String[cHeader.size()];
        editor.addNames(cHeader.toArray(arr1));

        ArrayList<String> cppHeader=Utils.getCppHeader(getContext());
        String[] arr2=new String[cppHeader.size()];
        editor.addNames(cppHeader.toArray(arr2));

        View rootView=getActivity().getWindow().getDecorView();
        symbolView=new SymbolView(getContext(),rootView);
        symbolView.setOnSymbolViewClick(new SymbolView.OnSymbolViewClick() {
            @Override
            public void onClick(View view, String text) {
                if(text.equals("--|")){
                    editor.paste("  ");//两个空格
                }else {
                    editor.paste(text);
                }
            }
        });
        recentFiles=new RecentFiles(getContext());
        //外部打开文件
        Intent intent=getActivity().getIntent();
        externalOpenFile(intent);
        log("----------->onCreate");
    }

    /**
     * 外部应用打开文件
     * @param intent
     */
    private  void externalOpenFile(Intent intent)
    {
        if(intent.getAction().equals(Intent.ACTION_VIEW))
        {
            String path=intent.getData().getPath();
            openFile(path);
        }
    }

    protected void onNewIntent(Intent intent) {
        //Intent intent=getIntent();
        //log("----------->onNewIntent");
        externalOpenFile(intent);

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==MSG_INIT)
            {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                if(!(boolean)msg.obj)
                {
                    new AlertDialog.Builder(getContext())
                            .setMessage("初始化失败，请打开软件重试")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().finish();
                                    System.exit(0);
                                }
                            })
                            .create()
                            .show();
                }else{
                    //写入
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean(KEY_FIRST_RUN,false);
                    editor.putString(KEY_APP_VERSION,Utils.getAppVersion(getContext()));
                    editor.apply();
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== ConstantPool.OK_SELECT_RESULT_CODE) {
            if(data!=null) {
                String path = data.getStringExtra("path");
                openFile(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO: Implement this method
        switch (item.getItemId())
        {
            //打开文件
            case R.id.menu_open:
                selectFile();
                break;
            //保存
            case  R.id.menu_save:
                save();
                break;
            //运行
            case R.id.menu_run:
                run();
                break;
            //另存为
            case R.id.menu_save_as:
                saveAs();
                break;
            //关闭文件
            case R.id.menu_close_file:
                closeFile();
                break;
            //重做
            case R.id.menu_redo:
                editor.redo();
                break;
            //撤销
            case R.id.menu_undo:
                editor.undo();
                break;
            //设置
            case R.id.menu_setting:
                preferences();
                break;
            case R.id.menu_recent_file:
                recent();
                break;
            case R.id.menu_indent_code:
                indent();
                break;
            case R.id.menu_compile_option:
                compileOption();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void compileOption()
    {
        View view=getLayoutInflater().inflate(R.layout.compile_option_layout,null);
        CheckBox checkBox=(CheckBox) view.findViewById(R.id.multi_file_check_box);
        final EditText editText=(EditText) view.findViewById(R.id.multi_file_edit);
        if(editor.getOpenedFile()==null) {
            isMultiFileCompile=false;
            multiFilesName="";
            checkBox.setEnabled(false);
        }
        checkBox.setChecked(isMultiFileCompile);
        editText.setText(multiFilesName);

        editText.setEnabled(checkBox.isChecked());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                editText.setEnabled(check);
                if(check) {
                    isMultiFileCompile = check;
                    if (editor.getOpenedFile() != null) {
                        String temp = "";
                        File[] sameDirFiles = editor.getOpenedFile().getParentFile().listFiles(new CFileNameFilter());
                        for (File f : sameDirFiles) {
                            temp += (f.getName()+" ");
                        }
                        editText.setText(temp);
                    }
                }else{
                    File path=null;
                    if((path=editor.getOpenedFile())!=null) {
                        multiFilesName = path.getName();
                        editText.setText(multiFilesName);
                    }
                }
            }
        });

        new AlertDialog.Builder(getContext())
                .setTitle("编译选项")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        multiFilesName=editText.getText().toString();
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();
    }
    /**
     *缩进代码
     */
    private  void indent()
    {
        if(editor.getText().toString().equals(""))
            return;
        final File tempIndent= new File(getActivity().getFilesDir()+File.separator+ConstantPool.INDENT_FILE_NAME);

        //写出缓存文件
        WriteThread writeThread=new WriteThread(editor.getText().toString(),tempIndent.getAbsolutePath(),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==WriteThread.MSG_WRITE_OK)
                {
                    //执行缩进
                    Utils.execBin(getContext(), new File(getActivity().getFilesDir() + File.separator + "indent"),ConstantPool.INDENT_ARGS+" "+ tempIndent.getAbsolutePath(), new ExecCallback() {
                        @Override
                        public void onResult(ShellUtils.CommandResult result) {
                            ReadThread readThread=new ReadThread(tempIndent.getAbsolutePath(),new Handler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    if(msg.what==ReadThread.MSG_READ_OK)
                                    {
                                        editor.replaceAll(msg.obj.toString());
                                    }
                                }
                            });
                            readThread.start();
                        }
                    });
                }
            }
        });
        writeThread.start();

    }

    /**
     * 打开一个文本文件
     * @param path
     */
    private  void openFile(String path)
    {
        //这里很重要！文件不要重复打开！！！
        if(editor.getOpenedFile()!=null) {
            if (!path.equals(editor.getOpenedFile().getAbsolutePath()))
                autoSave();
        }
        editor.open(path);
        recentFiles.addRecentFile(path);
        recentFiles.save();
        setSubtitle(editor.getOpenedFile().getName());

        multiFilesName=new File(path).getName();
        isMultiFileCompile=false;
    }

    /**
     * 最近打开文件
     */
    private  void recent(){
        final List<String> recentFilesList=recentFiles.getRecentFiles();

        final String[] recentFilesArray=new String[recentFilesList.size()];
        Iterator<String> iterator=recentFilesList.iterator();
        for(int i=0;iterator.hasNext();i++){
            String temp=iterator.next();
            recentFilesArray[i]=new File(temp).getName();
        }
        new AlertDialog.Builder(getContext()).setTitle("最近打开文件")
                .setItems(recentFilesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {
                        openFile(recentFilesList.get(idx));
                    }
                })
                .create()
                .show();

    }


    private void preferences()
    {
        startActivity(new Intent(getActivity().getApplicationContext(), SettingActivity.class));
    }
    private  void selectFile()
    {
        startActivityForResult(new Intent(getActivity().getApplicationContext(), FileListActivity.class),0);
    }

    /**
     * 关闭文件
     */
    private void closeFile()
    {
        autoSave();
        editor.setOpenedFile(null);
        setSubtitle(null);
        editor.setText("");
    }

    /**
     * 保存
     */
    private  void save()
    {
        if(editor.getOpenedFile()!=null) {
            editor.save(editor.getOpenedFile().getAbsolutePath());

        }
        else
        {
            saveAs();
        }
    }

    /**
     * 另存为
     */
    private  void saveAs()
    {
        final EditText editText=new EditText(getContext());
        AlertDialog alertDialog=new AlertDialog.Builder(getContext())
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(editText.getText())) {
                            File f = new File(ConstantPool.FILE_PATH);
                            File newFile=new File(f.getAbsolutePath() + File.separator + editText.getText());
                            editor.save(newFile.getAbsolutePath());
                            editor.setOpenedFile(newFile.getAbsolutePath());
                            setSubtitle(newFile.getName());
                        } else {
                            showToast("请输入文件名");
                        }
                    }
                })
                .setTitle("输入文件名(不要忘记.c后缀)")
                .setView(editText)
                .create();
        alertDialog.show();
    }

    /**
     * 选择编译器编译
     * @param context
     * @param files
     * @param compileCallback
     * @param gccCompile 是否gcc编译
     */
    private void compile(Context context, File[] files, CompileCallback compileCallback, boolean gccCompile)
    {
        if(gccCompile)
        {
            Utils.gccCompile(context,files,compileCallback);
        }
        else
        {
            Utils.gplusplusCompile(context,files,compileCallback);
        }
    }
    /**
     * 编译
     * @param files 要编译的文件
     */
    private  void compile(final File[] files){
        final boolean gccCompile=setting.isGccCompile();
        final ProgressDialog compileDialog=ProgressDialog.show(getContext(),"","正在编译...",false,false);
        compileDialog.show();
        //thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //headler
                compile(getContext(), files, new CompileCallback() {
                    public void onCompileResult (final ShellUtils.CommandResult result){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String info = null;
                                if (result.result == 0) {
                                    if (compileDialog != null)
                                        compileDialog.dismiss();
                                    showToast("编译成功");
                                    Utils.execBin(getContext());

                                } else {
                                    if (compileDialog != null)
                                        compileDialog.dismiss();
                                    info = result.getMsg();
                                    View view = LayoutInflater.from(getContext()).inflate(R.layout.compile_error_layout, null);
                                    TextView infoTextView = (TextView) view.findViewById(R.id.console_msg);
                                    infoTextView.setText(info);
                                    infoTextView.setTextColor(Color.BLACK);
                                    ScrollView scrollView = new ScrollView(getContext());
                                    scrollView.addView(view, LinearLayout.LayoutParams.MATCH_PARENT);
                                    Matcher matcher = Pattern.compile(":(\\d+):").matcher(info);
                                    final String[] pos = new String[1];
                                    if (matcher.find())
                                        pos[0] = matcher.group(1);
                                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                            .setTitle("编译失败")
                                            .setView(scrollView)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (pos[0] != null)
                                                        editor.gotoLine(Integer.parseInt(pos[0]));

                                                }
                                            })
                                            .create();
                                    alertDialog.show();
                                }
                            }
                        });
                    }

                },gccCompile);
            }
        }).start();

    }

    /**
     * 运行
     */
    File[] compileFiles=null;
    private void run()
    {
        File openFile=null;
        //判断是否打开了文件,编译前!!一定要!!保存
        if(editor.getOpenedFile()!=null) {
            //判断是否多文件编译
            openFile=editor.getOpenedFile();
            if(!isMultiFileCompile) {
                compileFiles=new File[1];
                compileFiles[0]=editor.getOpenedFile();
            }
            else{
                String[] fileNames=multiFilesName.split(" ");
                compileFiles=new File[fileNames.length];
                for(int i=0;i<fileNames.length;i++){
                    compileFiles[i]=new File(editor.getOpenedFile().getParent()+File.separator+fileNames[i]);
                    //log("----------->"+compileFiles[i].getAbsolutePath());
                }
            }
        }else
        {
            compileFiles=new File[1];
            openFile=compileFiles[0] = new File(getActivity().getFilesDir()+File.separator+ConstantPool.TEMP_FILE_NAME);

        }

        WriteThread writeThread=new WriteThread(editor.getText().toString(),openFile.getAbsolutePath(),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==WriteThread.MSG_WRITE_OK)
                {
                    compile(compileFiles);
                }
            }
        });
        writeThread.start();

    }

    private void log(String text) {
        if(DEBUG)
            System.out.println("MainActivity:"+text);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);

    }

    private Toast toast;
    private void showToast(CharSequence text)
    {
        if(toast==null)
        {
            toast=Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(text);
        }
        toast.show();
    }

}
