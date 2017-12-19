package mytime.snu.com.mytime;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Response;



//import com.google.analytics.tracking.android.EasyTracker;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class UserLoginPageActivity extends AppCompatActivity {

	Button btnLogin;

	EditText etNetID, etPassword;

	private Handler handler;

	Intent openIntent;

	LinearLayout layoutLoginParent;

	private static final String TAG = "UserLoginPageActivity";
	private String NetID, password;

	TextView txtTitle;
	TextView txtNewAdmin;

	private ProgressBar pbar;
	private DatabaseReference mRootRef;
	private DatabaseReference mAdminRef;
	private ArrayList<AdminModel> admins;
	private boolean isAdmin=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login_page);
		getSupportActionBar().setTitle("Login Page");
		initViews();

//		Typeface titleTextFace = Typeface.createFromAsset(getAssets(), "soulmaker_in_mountains.ttf");
		Typeface titleTextFace = Typeface.createFromAsset(getAssets(), "sunshine_boulevard.ttf");
		txtTitle.setTypeface(titleTextFace);
		loginPageAnim(txtTitle);

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NetID = etNetID.getText().toString();
				password = etPassword.getText().toString();

				confirmLogin();

				handler = new Handler(new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {

						pbar.setVisibility(View.GONE);
						layoutLoginParent.setVisibility(View.VISIBLE);

						if (msg.what == 1) {
							etNetID.setError("Invalid NetId");
							Toast.makeText(getBaseContext(), "Please check your NetID and password", Toast.LENGTH_SHORT).show();
						}
						else if (msg.what == 2) {
							etPassword.setError("Invalid password");
							Toast.makeText(getBaseContext(), "Please check your NetID and password", Toast.LENGTH_SHORT).show();
						}
						else if (msg.what == 3)	{
							pbar.setVisibility(View.GONE);
							layoutLoginParent.setVisibility(View.VISIBLE);
						}
						return false;
					}
				});
/*
				if((openIntent=new Intent(getBaseContext(), AdminDashboardActivity.class))!=null)
					startActivity(openIntent);
*/
			}
		});

		txtNewAdmin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if((openIntent=new Intent(getBaseContext(), AdminRegisterActivity.class))!=null)
					startActivity(openIntent);
			}
		});
	}

	private boolean isValid() {
		if(NetID.length() < 5) {
			etNetID.setError("Invalid NetID");
			return false;
		}
		if(password.length() == 0) {
			etPassword.setError("Empty password");
			return false;
		}

		return true;
	}

	Call post(Callback callback) throws IOException {
		OkHttpClient client = getUnsafeOkHttpClient();
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		client.setCookieHandler(cookieManager);
		RequestBody requestBody = new FormEncodingBuilder()
			.add("user_id", NetID)
			.add("user_password", password)
			.build();
		Request request = new Request.Builder()
			.url("https://studentmaintenance.webapps.snu.edu.in/students/public/studentslist/studentslist/loginauth")
			.post(requestBody)
			.build();
		Call call = client.newCall(request);
		call.enqueue(callback);
		return call;
	}
	private void confirmStudentLogin() {

//		final ProgressDialog dialog = new ProgressDialog(getBaseContext());
//
//		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		dialog.setMessage("Loading...");
//		dialog.setIndeterminate(true);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();


		try {
			post(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {
					Log.e(TAG, "Details confirmation failed. " + e.toString());

					pbar.setVisibility(View.GONE);
					layoutLoginParent.setVisibility(View.VISIBLE);

//					dialog.dismiss();
				}

				@Override
				public void onResponse(Response response) throws IOException {
					/**
					 * The student details website uses class "label label-danger"
					 * to show error in authentication. If this class is refereed
					 * then there is an error authenticating the user.
					 */
//					dialog.dismiss();
					String responseData = response.body().string();
//					Log.e(TAG,responseData);
					if (responseData.contains("alert alert-danger")) {
						Log.e(TAG, "Bad login credentials");

						/**
						 * If true then wrong SNU Net ID.
						 * Else wrong password.
						 */
						if (responseData.contains("You are not a valid user of the system")) {
							Message message = handler.obtainMessage(1);
							message.sendToTarget();
						} else {
							Message message = handler.obtainMessage(2);
							message.sendToTarget();
						}
						return;
					}

					else {
						String split1 = responseData.split("<strong>Welcome!</strong> ")[1];
						String split2 = split1.split("  ")[0];
						openIntent = new Intent(getBaseContext(), UserDashboardActivity.class);
						openIntent.putExtra("UserName",split2);
						openIntent.putExtra("NetID",NetID);
						openIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						Message message = handler.obtainMessage(3);
						message.sendToTarget();
						startActivity(openIntent);
					}
				}
			});
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	private static OkHttpClient getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
			};

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient okHttpClient = new OkHttpClient();
			okHttpClient.setSslSocketFactory(sslSocketFactory);
			okHttpClient.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			return okHttpClient;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void confirmLogin() {
		layoutLoginParent.setVisibility(View.GONE);
		pbar.setVisibility(View.VISIBLE);

		mRootRef = FirebaseDatabase.getInstance().getReference();
		mAdminRef = mRootRef.child("Admin");
//						FETCH KEYS AND VALUES OF ADMIN SEPARATELY AND CHECK FOR THEIR VALIDITY


		mAdminRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				admins = new ArrayList<>();
				admins.clear();
				for(DataSnapshot postSnapShot : dataSnapshot.getChildren())	{
					AdminModel am=new AdminModel();
					am.username = postSnapShot.getKey();
					am.password = postSnapShot.getValue(String.class);
//					Log.e("ADMIN_ACC",am.username+"  "+am.password);
					admins.add(am);
				}

				for(AdminModel am : admins)	{
					if(NetID.equals(am.username) && password.equals(am.password))	{
						isAdmin=true;
						if((openIntent=new Intent(getBaseContext(), AdminDashboardActivity.class))!=null) {
							pbar.setVisibility(View.GONE);
							layoutLoginParent.setVisibility(View.VISIBLE);
							openIntent.putExtra("AdminName",NetID);
							startActivity(openIntent);
						}
					}
				}
				if(isValid() && !isAdmin) {
					confirmStudentLogin();
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	public void loginPageAnim(View view){
		Animation fadeAnim =
			AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loginpage_animation);
		view.startAnimation(fadeAnim);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isAdmin=false;
		etNetID.setText("");
		etPassword.setText("");
	}

	void initViews()	{
		btnLogin = findViewById(R.id.btnLogin);
		txtNewAdmin = findViewById(R.id.txtNewAdmin);
		etNetID = findViewById(R.id.etNetID);
		etPassword = findViewById(R.id.etPassword);
		layoutLoginParent = findViewById(R.id.layoutLoginParent);
		pbar = findViewById(R.id.pbar);
		txtTitle = findViewById(R.id.txtTitle);
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}
}
