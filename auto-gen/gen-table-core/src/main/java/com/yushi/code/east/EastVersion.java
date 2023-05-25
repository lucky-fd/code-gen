package com.yushi.code.east;

/** Copy from {@link org.springframework.boot.SpringBootVersion}.<br> */
public final class EastVersion {

  private EastVersion() {}

  /**
   * Return the full version string of the present Wind codebase, or {@code null} if it cannot be
   * determined.
   *
   * @return the version of Wind or {@code null}
   * @see Package#getImplementationVersion()
   */
  public static String getVersion() {
    Package pkg = EastVersion.class.getPackage();
    return (pkg != null) ? pkg.getImplementationVersion() : "";
  }
}
