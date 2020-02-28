package com.chervatiuk.playwithme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.chervatiuk.playwithme.checks.Magisk;

public class CheckService extends Service {

  public CheckService() {
  }

  private IsolatedService mBinder = new IsolatedService.Stub() {
    @Override
    public boolean isMagiskPresent() {
      return new Magisk().execute();
    }
  };

  @Override
  public IBinder onBind(Intent intent) {
    return (IBinder) mBinder;
  }



}
