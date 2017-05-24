package com.doodlemobile.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.StreamUtils;
import com.doodlemobile.mobilesafe.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 显示载入界面的Activity
 * 
 * @author hhw
 */
public class SplashActivity extends Activity {

	/**
	 * 版本更新的状态码
	 */
	protected static final int UPDATE_VERSION = 100;

	/**
	 * 进入主界面的状态码
	 */
	protected static final int ENTER_HOME = 101;

	/**
	 * URL异常的状态码
	 */
	protected static final int URL_ERROR = 102;

	/**
	 * IO异常的状态码
	 */
	protected static final int IO_ERROR = 103;

	/**
	 * JSON异常的状态码
	 */
	protected static final int JSON_ERROR = 104;

	private TextView tv_version_name;
	private RelativeLayout rl_root;

	private int mLocalVersionCode;
	private String mVersionDes;
	private String mDownloadUrl;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				showUpdateDialog();
				break;

			case ENTER_HOME:
				enterHome();
				break;

			case URL_ERROR:
				ToastUtils.show(getApplicationContext(), "URL异常！");
				enterHome();
				break;

			case IO_ERROR:
				ToastUtils.show(getApplicationContext(), "读取异常！");
				enterHome();
				break;

			case JSON_ERROR:
				ToastUtils.show(getApplicationContext(), "JSON解析异常！");
				enterHome();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉Activity头，太麻烦，换主题比较方便
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		initUI();
		initData();
		initAnimation();
		initDB();
	}

	/**
	 * 初始化数据库
	 */
	private void initDB() {
		InitCallerLocDB("CallerLoc.db");
	}

	/**
	 * 初始化电话归属地的数据库
	 * 
	 * @param dbName
	 *            数据库全称
	 */
	private void InitCallerLocDB(String dbName) {
		File dir = getFilesDir();
		File db = new File(dir, dbName);

		if (!db.exists()) {
			InputStream in = null;
			FileOutputStream fos = null;
			try {
				in = getAssets().open(dbName);
				fos = new FileOutputStream(db);
				StreamUtils.copy(in, fos);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				StreamUtils.close(in, fos);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 添加淡入的动画效果
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(3000);
		rl_root.startAnimation(alphaAnimation);
	}

	/**
	 * 弹出对话框，提示用户更新
	 */
	protected void showUpdateDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新");
		builder.setMessage(mVersionDes);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadApk();
			}
		});
		builder.setNegativeButton("稍后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * 下载APK
	 */
	protected void downloadApk() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String target = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "mobilesafe.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(mDownloadUrl, target,
					new RequestCallBack<File>() {

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							File file = arg0.result;
							installApk(file);
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}

						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							super.onLoading(total, current, isUploading);
						}
					});
		}
	}

	/**
	 * 安装apk
	 * 
	 * @param file
	 *            要安装的文件
	 */
	protected void installApk(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivityForResult(intent, 10);
	}

	/**
	 * 进入应用程序主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);

		finish();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tv_version_name.setText("版本名称：" + getVersionName());
		mLocalVersionCode = getVersionCode();
		if (SPUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
			checkVersion();
		} else {
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
	}

	/**
	 * 检查版本
	 */
	private void checkVersion() {
		new Thread() {

			public void run() {
				long start = System.currentTimeMillis();
				Message msg = Message.obtain();
				try {
					// 可以用于模拟器访问电脑tomcat
					URL url = new URL("http://10.0.2.2:8080/upadte.json");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						String json = StreamUtils.streamToString(in);
						JSONObject obj = new JSONObject(json);

						// String versionName = obj.getString("versionName");
						mVersionDes = obj.getString("versionDes");
						int versionCode = obj.getInt("versionCode");
						mDownloadUrl = obj.getString("downloadUrl");

						if (versionCode > mLocalVersionCode) {
							msg.what = UPDATE_VERSION;
						} else {
							msg.what = ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {
					long end = System.currentTimeMillis();
					if (end - start < 4000) {
						try {
							Thread.sleep(4000 - end + start);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 从清单文件中获取版本号
	 * 
	 * @return 应用版本号，返回0代表异常
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 从清单文件中获取版本名称
	 * 
	 * @return 应用版本名称，返回null代表异常
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
	}

}
