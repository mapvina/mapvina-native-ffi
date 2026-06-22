package io.github.mapvina.nativeffi.error;

/** Status categories reported by the native Mapvina C ABI. */
public enum MapvinaStatus {
  OK(0),
  INVALID_ARGUMENT(-1),
  INVALID_STATE(-2),
  WRONG_THREAD(-3),
  UNSUPPORTED(-4),
  NATIVE_ERROR(-5),
  UNKNOWN(Integer.MIN_VALUE);

  private final int nativeCode;

  MapvinaStatus(int nativeCode) {
    this.nativeCode = nativeCode;
  }

  /** Returns the C ABI status value for known statuses. */
  public int nativeCode() {
    return nativeCode;
  }

  public static MapvinaStatus fromNative(int nativeCode) {
    return switch (nativeCode) {
      case 0 -> OK;
      case -1 -> INVALID_ARGUMENT;
      case -2 -> INVALID_STATE;
      case -3 -> WRONG_THREAD;
      case -4 -> UNSUPPORTED;
      case -5 -> NATIVE_ERROR;
      default -> UNKNOWN;
    };
  }
}
