package com.zdv.shangtongtianxia.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IPayView;
import com.zdv.shangtongtianxia.view.IUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/8 18:42
 */

public class FragmentScan extends BaseFragment implements IUserView, IPayView, EasyPermissions.PermissionCallbacks, QRCodeView.Delegate {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private final int DECODE_BITMAP_SUCCESS = 101;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    boolean lightOpen = false;
    @Bind(R.id.mQRCodeView)
    ZBarView mQRCodeView;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;

    @Bind(R.id.iv_flashlight)
    ImageView iv_flashlight;
    String codeStr = "";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DECODE_BITMAP_SUCCESS:
                    if (TextUtils.isEmpty(codeStr)) {
                        mQRCodeView.startCamera();
                        mQRCodeView.showScanRect();
                        mQRCodeView.startSpotDelay(500);
                        VToast.toast(getContext(), "未发现二维码");
                    } else {
                        Login();
                    }
                    break;
            }
        }
    };
    private boolean denyCamera = true;

    private void Pay(String obj) {
        showWaitDialog("正在交易");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryScanQrcode(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"), obj);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(FragmentScan.this, view);

        return view;
    }


    @Override
    public void Back() {
        super.Back();
        codeStr = "";
        mQRCodeView.stopSpot();
        mQRCodeView.hiddenScanRect();
        mQRCodeView.stopCamera();
        listener.gotoMain();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpotDelay(500);
    }

    @Override
    public void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpotDelay(500);
    }

    private void initDate() {
        mQRCodeView.setDelegate(this);
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentScan.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

    }

    protected void initView() {

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(iv_flashlight).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> FlashLight());
        RxView.clicks(header_edit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> openGallery());
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpotDelay(500);
    }

    private void openGallery() {
        startActivityForResult(BGAPhotoPickerActivity.newIntent(getContext(), null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }

    private void FlashLight() {

        if (!lightOpen) {
            lightOpen = true;
            mQRCodeView.openFlashlight();
        } else {
            lightOpen = false;
            mQRCodeView.closeFlashlight();
        }
    }

    public void queryPay(String qrcode){
        codeStr= qrcode;
        Login();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 识别图片中的二维码还有问题，占时不要用
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            executor.execute(() -> {
                Bitmap bitmap = getDecodeAbleBitmap(picturePath);
                String qrcode = QRCodeDecoder.syncDecodeQRCode(bitmap);
                KLog.v("qrcode--" + qrcode);
                Message msg = handler.obtainMessage();
                codeStr = qrcode;
                msg.what = DECODE_BITMAP_SUCCESS;
                handler.sendMessage(msg);
            });

        } else {
            mQRCodeView.startCamera();
            mQRCodeView.showScanRect();
            mQRCodeView.startSpotDelay(500);
        }
    }

    private void Login() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), sp.getString("user_name", ""),
                util.getMD5(sp.getString("user_pw", "")));
    }

    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     * @return
     */
    private static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onStop() {
        lightOpen = false;
        mQRCodeView.destroyDrawingCache();
        mQRCodeView.hiddenScanRect();
        mQRCodeView.startSpot();
        mQRCodeView.closeFlashlight();
        mQRCodeView.stopCamera();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        KLog.v("result:" + result);
        Message msg = handler.obtainMessage();
        codeStr = result;
        msg.what = DECODE_BITMAP_SUCCESS;
        handler.sendMessage(msg);
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        KLog.e("打开相机出错");
        VToast.toast(getContext(),"没有相机使用权限");
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void ResolveSearchMemberOrder(ResponseBody info) {

    }

    @Override
    public void ResolveQRCode(ResponseBody info) {

    }

    @Override
    public void ResolveScanQrcode(ResponseBody info) {
        hideWaitDialog();

        if(info.source()==null){
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            info.close();
            KLog.v(res);
            jsonObject = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            codeStr = "";
            VToast.toast(getContext(), "交易成功");
            Login();
        }else{
            listener.restartScan();
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    @Override
    public void ResolveChargeSTCoin(ResponseBody info) {

    }

    @Override
    public void ResolveTradeHistory(ResponseBody info) {

    }

    @Override
    public void ResolveSetPayPassword(ResponseBody info) {

    }

    @Override
    public void ResolveVerifyPayPassword(ResponseBody info) {

    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {

    }

    @Override
    public void ResolveLoginInfo(ResponseBody info) {
        hideWaitDialog();
        if(info.source()==null){
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);
            Constant.user_info = new JSONObject(jsonObject.optString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            if(!codeStr.equals("")) {
                Pay(codeStr);
            }else{
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("得到一笔收入,是否查看余额?")
                        .positiveText(R.string.bga_pp_confirm)
                        .negativeText(R.string.cancle)
                        .autoDismiss(true)
                        .cancelable(false)
                        .onPositive((materialDialog, dialogAction) -> {
                             listener.gotoCoin();
                        })
                        .onNegative((materialDialog, dialogAction) -> {

                        })
                        .show();
                listener.restartScan();
            }
        }else{
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    @Override
    public void ResolveRegisterInfo(ResponseBody info) {

    }

    @Override
    public void ResolveForgetInfo(ResponseBody info) {

    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {

    }

}
