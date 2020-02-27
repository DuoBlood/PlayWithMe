package com.chervatiuk.playwithme.checks;

public class BusyboxBinary extends Binary {

  private static final String BINARY_BUSYBOX = "busybox";

  @Override
  protected String fileName() {
    return BINARY_BUSYBOX;
  }
}
