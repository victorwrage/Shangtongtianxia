package com.zdv.shangtongtianxia.customView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.util.Constant;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Hello_world on 2017/8/12.
 */

public class QrCodeShareDialog {

    private Context context;
    private Dialog mCameraDialog;
    private String code;

    public QrCodeShareDialog(Context context, String code) {
        this.context = context;
        this.code = code;
        init();
    }

    private void init() {
        mCameraDialog = new Dialog(context, R.style.my_dialog);
        View root = LayoutInflater.from(context).inflate(
                R.layout.dialog_qrcode_share, null);
        ImageView iv = (ImageView) root.findViewById(R.id.iv_qrcode);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        Bitmap qrBitmap = QRCodeEncoder.syncEncodeQRCode("STTX#" + code + "#" + Constant.user_info.optString("code"), 250, Color.parseColor("#000000"), bitmap);
        iv.setImageBitmap(qrBitmap);

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        Activity activity = (Activity) context;
        WindowManager windowManager = activity.getWindowManager();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = 250; // 宽度
        root.measure(0, 0);
        lp.height = 250;
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.setCanceledOnTouchOutside(true);
    }

    public void show() {
        mCameraDialog.show();
    }

    public void dismiss() {
        mCameraDialog.dismiss();
    }

}
