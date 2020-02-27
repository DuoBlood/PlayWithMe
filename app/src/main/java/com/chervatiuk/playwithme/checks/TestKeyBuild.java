package com.chervatiuk.playwithme.checks;

public class TestKeyBuild implements Check {

  @Override
  public boolean execute() {
    final String buildTags = android.os.Build.TAGS;
    return buildTags != null && buildTags.contains("test-keys");
  }
}
