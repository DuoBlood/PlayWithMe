package com.chervatiuk.playwithme.checks;

import java.lang.reflect.Method;

public class SelinuxFlag implements Check {

  @Override
  public boolean execute() {
    try {
      final Class<?> c = Class.forName("android.os.SystemProperties");
      final Method get = c.getMethod("get", String.class);
      final String selinux = (String) get.invoke(c, "ro.build.selinux");
      return "1".equals(selinux);
    } catch (Exception ignored) {
      return false;
    }
  }
}
