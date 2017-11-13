package com.zdv.shangtongtianxia.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IMemberView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.ResponseBody;

/**
 * 认证
 */
public class FragmentVerify extends BaseFragment implements IMemberView {
    private static final String COOKIE_KEY = "cookie";

	@Bind(R.id.header_btn)
    ImageView header_btn;
	@Bind(R.id.header_title)
    TextView header_title;
	@Bind(R.id.header_btn_lay)
	LinearLayout header_btn_lay;

	@Bind(R.id.verify_card_et)
	EditText verify_card_et;
	@Bind(R.id.verify_name_et)
	EditText verify_name_et;

	@Bind(R.id.verify_pic1_iv)
	ImageView verify_pic1_iv;
	@Bind(R.id.verify_pic2_iv)
	ImageView verify_pic2_iv;

	@Bind(R.id.verify_card1_lay)
	LinearLayout verify_card1_lay;
	@Bind(R.id.verify_card2_lay)
	LinearLayout verify_card2_lay;

	@Bind(R.id.verify_success_lay)
	LinearLayout verify_success_lay;
	@Bind(R.id.verify_work_lay)
	LinearLayout verify_work_lay;

	@Bind(R.id.verify_submit_btn)
	Button verify_submit_btn;

	QueryPresent present;
	Utils util;
	private final int OCR_CODE_OCR_CAED = 1011;
	private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
	private int type = 0;
	private String path;
	private final String PIC1 = "pic1";
	private final String PIC2 = "pic2";
	Map<String, String> temp_info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_verify, container, false);
		ButterKnife.bind(FragmentVerify.this,view);
		temp_info = new HashMap<>();
		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		present.setView(FragmentVerify.this);
		RxView.clicks(verify_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit());
		RxView.clicks(verify_card1_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(0));
		RxView.clicks(verify_card2_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Photo(1));

		RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
		header_title.setText("实名认证");

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		CheckIsVerify();
	}

	private void CheckIsVerify() {
		showWaitDialog("请稍等");
		present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
		present.QueryIsVerify(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));
	}

	@Override
	public void refreshState() {
		super.refreshState();
		verify_card_et.setText("");
		verify_name_et.setText("");
		verify_name_et.setEnabled(true);
		verify_card_et.setEnabled(true);
		verify_card1_lay.setEnabled(true);
		verify_card2_lay.setEnabled(true);
		temp_info.clear();
		Picasso.with(getContext()).load(R.drawable.verify_icon1)
				.placeholder(R.drawable.verify_icon1)
				.error(R.drawable.download_failed)
				.into(verify_pic1_iv);
		Picasso.with(getContext()).load(R.drawable.verify_icon2)
				.placeholder(R.drawable.verify_icon2)
				.error(R.drawable.download_failed)
				.into(verify_pic2_iv);
		verify_submit_btn.setText("确认提交");
		RxView.clicks(verify_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit());

		CheckIsVerify();
	}

	private void Photo(int type) {
		Acp.getInstance(getContext()).request(new AcpOptions.Builder()
						.setPermissions(Manifest.permission.CAMERA)
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
						.build(),
				new AcpListener() {
					@Override
					public void onGranted() {
						Constant.photo_idx = type;
						switch (type) {
							case 0:
								openCamera(type);
								break;
							case 1:
								openCamera(type);
								break;
						}
					}

					@Override
					public void onDenied(List<String> permissions) {
						VToast.toast(getContext(),"请打开摄像头权限");
					}
				});

	}

	public void openCamera(int type_) {
		type = type_;
		Intent intent = new Intent(getContext(), CameraActivity.class);
		path = util.getSaveFile(getContext()).getAbsolutePath();
		intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, path);
		switch (type) {
			case 0:
				intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
				startActivityForResult(intent, OCR_CODE_OCR_CAED);
				break;
			case 1:
				intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
				startActivityForResult(intent, OCR_CODE_OCR_CAED);
				break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		KLog.v("resultCode" + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == OCR_CODE_OCR_CAED) {
				KLog.v("onActivityResult" + path);
					uploadImg(type, path);
				}
			}
			if (requestCode == REQUEST_THUMBNAIL) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				String path_thumb = util.saveImageToGallery(getContext(), bitmap);
					uploadImg(type, path_thumb);
			}
	}


	public void uploadImg(int type, String path) {
		KLog.v(path);
		showWaitDialog("请稍等");
		upTobmob(type, path);
		switch (type) {
			case 0:
			case 3:
				showWaitDialog("请稍等");
				// new Thread(() -> trygetCardNum(type, path)).start();
				new Thread(() -> baiduOcr(type, path)).start();
				break;
			default:
				showWaitDialog("请稍等");
				upTobmob(type, path);
				break;
		}
	}

	private void upTobmob(int type, String path) {
		BmobFile bmobFile = new BmobFile(new File(path));
		bmobFile.uploadblock(getContext(), new UploadFileListener() {
			@Override
			public void onProgress(Integer value) {
				KLog.v(value + "%");
				// showWaitDialog("请稍等，正在上传到服务器:" + value + "%");
			}

			@Override
			public void onSuccess() {
				hideWaitDialog();
				String ret_path = bmobFile.getFileUrl(getContext());
				KLog.v(ret_path);
				File local_file = new File(path);
				VToast.toast(getContext(), "上传文件成功");
				switch (type) {
					case 0:
						//   verify_card_card1_finished.setVisibility(View.VISIBLE);
						temp_info.put(PIC1, ret_path);
						Picasso.with(getContext()).load(local_file.getAbsoluteFile())
								.placeholder(R.drawable.verify_icon1)
								.error(R.drawable.download_failed)
								.into(verify_pic1_iv);
						break;
					case 1:
						// verify_card_card2_finished.setVisibility(View.VISIBLE);
						temp_info.put(PIC2, ret_path);
						Picasso.with(getContext()).load(local_file.getAbsoluteFile())
								.placeholder(R.drawable.verify_icon2)
								.error(R.drawable.download_failed)
								.into(verify_pic2_iv);
						break;
				}
			}

			@Override
			public void onFailure(int i, String s) {
				hideWaitDialog();
				KLog.v("i" + i + "--" + s);
				VToast.toast(getContext(), "上传文件失败：" + s);
			}
		});
	}

	private void baiduOcr(int type, String filePath){
		File u_file = new File(filePath);
		KLog.v("baiduOcr"+u_file.getAbsolutePath());
		switch (type){
			case 0:
				IDCardParams id_param =  new IDCardParams();
				id_param.setImageFile(u_file);
				id_param.setIdCardSide(IDCardParams.ID_CARD_SIDE_FRONT);
				id_param.setDetectDirection(true);
				OCR.getInstance().recognizeIDCard(id_param, new OnResultListener<IDCardResult>() {
					@Override
					public void onResult(IDCardResult result) {
						KLog.v(result.toString());
						KLog.v(result.getName().getWords());
						if(result.getName()!=null && !result.getName().getWords().equals("")) {
							verify_name_et.setText(result.getName().getWords());
						}
						if(result.getIdNumber()!=null && !result.getIdNumber().getWords().equals("")) {
							verify_card_et.setText(result.getIdNumber().getWords());
						}
						upTobmob(type, filePath);
						VToast.toast(getContext(), "请检查获取的信息是否正确");
					}
					@Override
					public void onError(OCRError error) {
						KLog.v(error.toString());
						upTobmob(type, filePath);
						VToast.toast(getContext(), "未能获取身份信息,请手动输入");
					}
				});
				break;
		}

	}
	@Override
	public void Back() {
		super.Back();
		listener.gotoProfile();
	}

	private void Submit() {
		if(verify_name_et.getText().toString().trim().equals("")){
			VToast.toast(getContext(),"请输入身份证姓名");
			return;
		}
		if(!util.IDCardValidate(verify_card_et.getText().toString().trim()).equals("")){
			VToast.toast(getContext(),"请输入身份证号码");
			return;
		}
		showWaitDialog("请稍等");
		present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
		present.QueryVerifyMember(util.UrlEnco(Constant.WDT_SECRET),Constant.user_info.optString("code"),verify_name_et.getText().toString().trim()
		,temp_info.get(PIC1),temp_info.get(PIC2),verify_card_et.getText().toString().trim());
	}

	@Override
	public void ResolveActorMember(ResponseBody info) {

	}

	@Override
	public void ResolveApplyActorMember(ResponseBody info) {

	}

	@Override
	public void ResolveMemberSign(ResponseBody info) {

	}

	@Override
	public void ResolveMessage(ResponseBody info) {

	}

	@Override
	public void ResolveActorFirm(ResponseBody info) {

	}

	@Override
	public void ResolveMemberPayDetail(ResponseBody info) {

	}

	@Override
	public void ResolveVerifyMember(ResponseBody info) {
		if (info.source() == null) {
			VToast.toast(getContext(), "网络错误，请重试!");
			hideWaitDialog();
			return;
		}
		JSONObject jsonObject = null;
		try {
			String res = info.string();
			KLog.v(res);
			jsonObject = new JSONObject(res);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			VToast.toast(getContext(), "网络超时");
			hideWaitDialog();
			return;
		}

		if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
			VToast.toast(getContext(), "提交成功");
			Back();
		} else {
			VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
		}
		hideWaitDialog();
	}

	@Override
	public void ResolveTeamMember(ResponseBody info) {

	}

	@Override
	public void ResolveIsVerify(ResponseBody info) {
		if (info.source() == null) {
			//VToast.toast(getContext(), "网络错误，请重试!");
			hideWaitDialog();
			return;
		}
		JSONObject jsonObject = null;
		try {
			String res = info.string();
			KLog.v(res);
			jsonObject = new JSONObject(res);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		//	VToast.toast(getContext(), "网络超时");
			hideWaitDialog();
			return;
		}

		if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
			JSONObject jsonObject1 = null;
			try {
				jsonObject1 = new JSONObject(jsonObject.optString("content"));
				String ischeck = jsonObject1.optString("ischeck");
				String idcard = jsonObject1.optString("idcard");
				String actual_name = jsonObject1.optString("actual_name");
				String front_img = jsonObject1.optString("front_img");
				String back_img = jsonObject1.optString("back_img");
				if(ischeck.equals("2")){
					VToast.toast(getContext(), "您已经认证完成");
					verify_name_et.setEnabled(false);
					verify_card_et.setEnabled(false);
					verify_card1_lay.setEnabled(false);
					verify_card2_lay.setEnabled(false);
					verify_submit_btn.setText("返回");
					RxView.clicks(verify_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
					verify_card_et.setText(idcard);
					verify_name_et.setText(actual_name);
					Picasso.with(getContext()).load(front_img)
							.placeholder(R.drawable.verify_icon1)
							.error(R.drawable.verify_icon1)
							.into(verify_pic1_iv);
					Picasso.with(getContext()).load(back_img)
							.placeholder(R.drawable.verify_icon2)
							.error(R.drawable.verify_icon2)
							.into(verify_pic2_iv);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}


		} else if (jsonObject.optString(Constant.ERRCODE).equals("304")){
			//VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
			new MaterialDialog.Builder(getContext())
					.title("提示")
					.content("您的资料正在审核中，请耐心等待")
					.positiveText(R.string.bga_pp_confirm)
					.autoDismiss(true)
					.cancelable(false)
					.onPositive((materialDialog, dialogAction) -> {
						Back();
					})
					.show();
		}else {

		}
		hideWaitDialog();
	}
}
