package com.sys.readh.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.sys.readh.R;

public class InputView extends FrameLayout {
    private int inputIcon;
    private String inputHit;
    private boolean inputPass;
    private View mview;
    private ImageView imageView;
    private EditText editText;
    public InputView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs){
        if(attrs ==null) return ;
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.inputView);
        inputIcon = typedArray.getResourceId(R.styleable.inputView_input_icon,R.mipmap.logo);
        inputHit = typedArray.getString(R.styleable.inputView_input_hit);
        inputPass = typedArray.getBoolean(R.styleable.inputView_input_pass,false);
        typedArray.recycle();
        //获取布局
        mview =  LayoutInflater.from(context).inflate(R.layout.input_view,this,false);
        imageView = mview.findViewById(R.id.iv_icon);
        imageView.setImageResource(inputIcon);
        editText = mview.findViewById(R.id.iv_text);
        editText.setHint(inputHit);
        editText.setInputType(inputPass? InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD: InputType.TYPE_CLASS_TEXT);

        addView(mview);
    }
   public String getInputStr(){
        return  editText.getText().toString().trim();
   }



}