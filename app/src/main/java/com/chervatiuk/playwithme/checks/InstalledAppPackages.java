package com.chervatiuk.playwithme.checks;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.List;

public class InstalledAppPackages implements Check {

  private static final String[] ROOT_PACKAGES = {
      "com.thirdparty.superuser",
      "eu.chainfire.supersu",
      "com.koushikdutta.superuser",
      "com.zachspong.temprootremovejb",
      "com.ramdroid.appquarantine",
      "com.topjohnwu.magisk",
      "com.noshufou.android.su",
      "com.noshufou.android.su.elite",
      "com.yellowes.su",
      "com.kingroot.kinguser",
      "com.kingo.root",
      "com.smedialink.oneclickroot",
      "com.zhiqupk.root.global",
      "com.alephzain.framaroot"

  };

  private final Context context;

  public InstalledAppPackages(final Context context) {
    this.context = context;
  }

  @Override
  public boolean execute() {
    final List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
    if (packs.isEmpty()) return false;
    for (PackageInfo pi : packs) {
      for (String s : ROOT_PACKAGES) {
        if (s.equalsIgnoreCase(pi.packageName)) {
          return true;
        }
      }
    }
    return false;
  }
}
