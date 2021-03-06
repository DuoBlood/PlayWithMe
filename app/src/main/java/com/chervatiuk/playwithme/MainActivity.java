package com.chervatiuk.playwithme;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.chervatiuk.playwithme.checks.BusyboxBinary;
import com.chervatiuk.playwithme.checks.Check;
import com.chervatiuk.playwithme.checks.InstalledAppPackages;
import com.chervatiuk.playwithme.checks.RWPaths;
import com.chervatiuk.playwithme.checks.RunningProcesses;
import com.chervatiuk.playwithme.checks.SafetyNet;
import com.chervatiuk.playwithme.checks.SelinuxFlag;
import com.chervatiuk.playwithme.checks.SuBinary;
import com.chervatiuk.playwithme.checks.SuExecution;
import com.chervatiuk.playwithme.checks.TestKeyBuild;

import java.security.SecureRandom;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private SwitchCompat switchBinaryBusybox;
  private SwitchCompat switchInstalledPackages;
  private SwitchCompat switchRunngunProcesses;
  private SwitchCompat switchRWPaths;
  private SwitchCompat switchSafetyNet;
  private SwitchCompat switchSELinuxFlag;
  private SwitchCompat switchBinarySu;
  private SwitchCompat switchExecuteSu;
  private SwitchCompat switchTestKey;
  private SwitchCompat switchMagisk;

  private IsolatedService isolatedService;
  private boolean bound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();
  }

  private void initViews() {
    switchBinaryBusybox = findViewById(R.id.switch_binary_busybox);
    switchInstalledPackages = findViewById(R.id.switch_installed_apps);
    switchRunngunProcesses = findViewById(R.id.switch_running_processes);
    switchRWPaths = findViewById(R.id.switch_rw_paths);
    switchSafetyNet = findViewById(R.id.switch_sefetyNet);
    switchSELinuxFlag = findViewById(R.id.switch_selinux);
    switchBinarySu = findViewById(R.id.switch_binary_su);
    switchExecuteSu = findViewById(R.id.switch_exec_su);
    switchTestKey = findViewById(R.id.switch_test_key);
    switchMagisk = findViewById(R.id.switch_magisk);

    switchRunngunProcesses.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Toast.makeText(MainActivity.this,
                    "This check is working only on Android < API 26",
                    Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
              }
            }
          }
        });
    switchSafetyNet.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
              Toast.makeText(MainActivity.this,
                  "This check has not implemented yet",
                  Toast.LENGTH_SHORT).show();
              buttonView.setChecked(false);
            }
          }
        });

    findViewById(R.id.button_check).setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(final View v) {
        easterEgg();
        return false;
      }
    });
    findViewById(R.id.button_check).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        check(switchBinaryBusybox, new BusyboxBinary());
        check(switchInstalledPackages, new InstalledAppPackages(MainActivity.this));
        check(switchRunngunProcesses, new RunningProcesses(MainActivity.this));
        check(switchRWPaths, new RWPaths());
        check(switchSafetyNet, new SafetyNet());
        check(switchSELinuxFlag, new SelinuxFlag());
        check(switchBinarySu, new SuBinary());
        check(switchExecuteSu, new SuExecution());
        check(switchTestKey, new TestKeyBuild());
        check(switchMagisk, new Check() {
          @Override
          public boolean execute() {
            boolean value = false;
            if (bound) {
              try {
                value = isolatedService.isMagiskPresent();
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            } else {
              Toast.makeText(MainActivity.this,
                  "Isolated Service not bound",
                  Toast.LENGTH_SHORT).show();
            }
            return value;
          }
        });
      }
    });
  }

  private void check(final SwitchCompat switchCompat, final Check check) {
    if (!switchCompat.isEnabled()) {
      return;
    }
    if (switchCompat.isChecked()) {
      if (check.execute()) {
        switchCompat.setTextColor(Color.RED);
      } else {
        switchCompat.setTextColor(Color.GREEN);
      }
    } else {
      switchCompat.setTextColor(Color.GRAY);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    Intent intent = new Intent(this, CheckService.class);
    getApplicationContext().bindService(intent, isolatedServiceConnection, BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (!bound) {
      return;
    }
    unbindService(isolatedServiceConnection);
    bound = false;
  }


  private ServiceConnection isolatedServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      isolatedService = IsolatedService.Stub.asInterface(iBinder);
      bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      bound = false;
    }
  };

  private void easterEgg() {
    Random random = new SecureRandom();
    String[] phrases = getResources().getStringArray(R.array.mountain_king);
    int i = random.nextInt(phrases.length - 1);
    Toast.makeText(MainActivity.this, phrases[i], Toast.LENGTH_SHORT).show();
  }

}
