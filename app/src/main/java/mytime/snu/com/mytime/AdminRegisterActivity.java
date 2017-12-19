package mytime.snu.com.mytime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminRegisterActivity extends AppCompatActivity {

	EditText etUsername, etPassword, etCnfPassword;
	Button btnRegister;
	private DatabaseReference mRootRef;
	private DatabaseReference mAdminRef;
	private Intent openIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_register);
		initViews();

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String username = etUsername.getText().toString();
				String pwd = etPassword.getText().toString();
				String cnfPwd = etCnfPassword.getText().toString();

				if(username.equals(""))	{
					etUsername.setError("Username field cannot be empty");
				}
				else if(pwd.equals(""))	{
					etPassword.setError("Password field cannot be empty");
				}
				else if(cnfPwd.equals(""))	{
					etCnfPassword.setError("Confirm your password here");
				}
				else	{
					if(!pwd.equals(cnfPwd))	{
						Toast.makeText(getBaseContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
					}
					else	{
						mAdminRef.child(username).setValue(pwd);
						Toast.makeText(getBaseContext(), "Created new Admin. Login to Continue", Toast.LENGTH_SHORT).show();
						if((openIntent=new Intent(getBaseContext(), UserLoginPageActivity.class))!=null) {
							startActivity(openIntent);
						}
					}
				}
			}
		});
	}

	public void initViews()	{
		btnRegister = findViewById(R.id.btnRegister);
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);
		etCnfPassword = findViewById(R.id.etCnfPassword);
		mRootRef = FirebaseDatabase.getInstance().getReference();
		mAdminRef = mRootRef.child("Admin");
	}

	@Override
	public void onBackPressed() {
		if((openIntent=new Intent(getBaseContext(), UserLoginPageActivity.class))!=null) {
			startActivity(openIntent);
		}
	}
}
