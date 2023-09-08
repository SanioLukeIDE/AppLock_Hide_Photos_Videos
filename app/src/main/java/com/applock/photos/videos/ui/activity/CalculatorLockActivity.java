package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Utility.hideApp;
import static com.applock.photos.videos.utils.Utility.unHideApp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityCalculatorLockBinding;
import com.applock.photos.videos.service.CameraService;
import com.applock.photos.videos.singletonClass.MyApplication;
import com.applock.photos.videos.utils.SharePreferences;
import com.applock.photos.videos.utils.Utility;

public class CalculatorLockActivity extends AppCompatActivity {

    ActivityCalculatorLockBinding binding;
    boolean isChange, isHide;
    CalculatorLockActivity activity;
    SharePreferences sharePreferences;
    String newPassword = null, packageName, componentName;
    int hiddenPage;
    Animation clickAnimation;
    int wrongBiometricCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator_lock);
        activity = CalculatorLockActivity.this;
        sharePreferences = MyApplication.getPreferences();

        clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ripple_effect);

        isChange = getIntent().getBooleanExtra("change", false);
        isHide = getIntent().getBooleanExtra("isHide", false);
        hiddenPage = getIntent().getIntExtra("hiddenPage", 0);
        packageName = getIntent().getStringExtra("package");
        componentName = getIntent().getStringExtra("name");

        if (isChange){
            binding.tvHint.setText(getString(R.string.enter_new_password));
            binding.etPassword.setText("");
            binding.etPassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        } else {
            if (!sharePreferences.getCalculatorLockPassword().equals("1234")){
                binding.tvHint.setText(getString(R.string.enter_your_password));
                binding.etPassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            } else binding.etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        setPasswordField();

        binding.btnOk.setOnClickListener(v -> {
            v.startAnimation(clickAnimation);
            String password = binding.etPassword.getText().toString();

            if (TextUtils.isEmpty(password)){
                Toast.makeText(activity, "Please enter your password.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() != 4){
                Toast.makeText(activity, "Password must of length 4.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isChange){
                if (newPassword == null){
                    newPassword = password;
                    binding.tvHint.setText(getString(R.string.confirm_password));
                    binding.etPassword.setText("");
                } else if (newPassword.equals(password)){
                    sharePreferences.setCalculatorLockPassword(newPassword);
                    Toast.makeText(activity, "Password Changed Successfully.", Toast.LENGTH_SHORT).show();
                    Utility.finishActivity(activity);
                } else {
                    Toast.makeText(activity, "Password not matched.", Toast.LENGTH_SHORT).show();
                }
            } else if (sharePreferences.getCalculatorLockPassword().equals(password)){
                Toast.makeText(activity, "Correct Password.", Toast.LENGTH_SHORT).show();
                if (hiddenPage == 1) {
                    startActivity(new Intent(getApplicationContext(), HiddenImagesActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("type", 1));
                    Utility.finishActivity(activity);
                } else setHideUnHideApp();
            } else {
                wrongBiometricCount++;
                Toast.makeText(activity, "Incorrect Password.", Toast.LENGTH_SHORT).show();
                if (MyApplication.getPreferences().getAttemptFailedCount() == wrongBiometricCount){
                    if (MyApplication.getPreferences().isIntruderDetectorOnOff()){
                        activity.startService(new Intent(activity, CameraService.class));
                        wrongBiometricCount = 0;
                    }
                }
            }

        });

    }

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Utility.finishActivity(activity);
            }
        }
    });

    private void setHideUnHideApp() {
        if (isHide){
            try {
                hideApp(activity, ComponentName.unflattenFromString(componentName));
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", packageName, null);
                intent.setData(uri);
                startActivity(intent);
                activityResult.launch(intent);
//                String s = e.getLocalizedMessage();
//                Log.e( "onItemClicked: ", s);
//                activity.requestPermissions();
            }
            sharePreferences.setHideApps(packageName);
        } else {
            try {
                unHideApp(getApplicationContext(), ComponentName.unflattenFromString(componentName));
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", packageName, null);
                intent.setData(uri);
                activityResult.launch(intent);
            }
        }
    }

    private void setPasswordField() {

        binding.btnClear.setOnClickListener(v -> {
            v.startAnimation(clickAnimation);
            if (binding.etPassword.getText().length() > 0) {
                binding.etPassword.setText(binding.etPassword.getText().subSequence(0, binding.etPassword.getText().length() - 1));
            }
        });
        binding.btnClean.setOnClickListener(v -> {
            v.startAnimation(clickAnimation);
            if (binding.etPassword.getText().length() > 0) {
                binding.etPassword.setText("");
            }
        });

        binding.btn0.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "0");
        });
        binding.btn1.setOnClickListener(view -> {
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "1");
        });
        binding.btn2.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "2");
        });
        binding.btn3.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "3");
        });
        binding.btn4.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "4");
        });
        binding.btn5.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "5");
        });
        binding.btn6.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "6");
        });
        binding.btn7.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "7");
        });
        binding.btn8.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "8");
        });
        binding.btn9.setOnClickListener(view -> {
            view.startAnimation(clickAnimation);
            EditText editText = binding.etPassword;
            editText.setText(binding.etPassword.getText() + "9");
        });

    }
}