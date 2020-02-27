package com.chervatiuk.playwithme.checks;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Works on Android version < 8.0 (Oreo API 26)
 */
public class RunningProcesses implements Check {

  private final Context context;

  public RunningProcesses(final Context context) {
    this.context = context;
  }

  @Override
  public boolean execute() {
    final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    boolean returnValue = false;
    if (am == null) return false;
    // Get currently running application processes
    final List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(300);

    if (list != null) {
      String tempName;
      for (int i = 0; i < list.size(); ++i) {
        tempName = list.get(i).process;
        if (tempName.contains("supersu") || tempName.contains("superuser")) {
          returnValue = true;
        }
      }
    }
    return returnValue;
  }
}
