package com.chervatiuk.playwithme.checks;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class SelinuxFlag implements Check {

  @Override
  public boolean execute() {
    try {
      final Class<?> c = Class.forName("android.os.SystemProperties");
      final Method get = c.getMethod("get", String.class);
      final String selinux = (String) get.invoke(c, "ro.build.selinux");
      return "1".equals(selinux) || isSELinuxEnforcing();
    } catch (Exception ignored) {
      return false;
    }
  }

  private boolean isSELinuxEnforcing() {
    Boolean enforcing = null;
    final File f = new File("/sys/fs/selinux/enforce");
    if (f.exists()) {
      try (final InputStream is = new FileInputStream("/sys/fs/selinux/enforce")) {
        enforcing = (is.read() == '1');
      } catch (Exception ignored) {
      }
    }
    if (enforcing == null) {
      enforcing = true;
    }
    return enforcing;
  }
}
