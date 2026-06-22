package io.github.mapvina.nativeffi.error;

import java.util.Objects;

/** Base unchecked exception for errors reported by the native Mapvina C ABI. */
public class MapvinaException extends RuntimeException {
  private final MapvinaStatus status;
  private final int nativeStatusCode;
  private final String diagnostic;

  public MapvinaException(MapvinaStatus status, int nativeStatusCode, String diagnostic) {
    super(message(status, nativeStatusCode, diagnostic));
    this.status = Objects.requireNonNull(status, "status");
    this.nativeStatusCode = nativeStatusCode;
    this.diagnostic = diagnostic == null ? "" : diagnostic;
  }

  public MapvinaStatus status() {
    return status;
  }

  public int nativeStatusCode() {
    return nativeStatusCode;
  }

  public String diagnostic() {
    return diagnostic;
  }

  public static MapvinaException forStatus(
      MapvinaStatus status, int nativeStatusCode, String diagnostic) {
    return switch (status) {
      case INVALID_ARGUMENT -> new InvalidArgumentException(nativeStatusCode, diagnostic);
      case INVALID_STATE -> new InvalidStateException(nativeStatusCode, diagnostic);
      case WRONG_THREAD -> new WrongThreadException(nativeStatusCode, diagnostic);
      case UNSUPPORTED -> new UnsupportedFeatureException(nativeStatusCode, diagnostic);
      case NATIVE_ERROR -> new NativeErrorException(nativeStatusCode, diagnostic);
      case OK, UNKNOWN -> new MapvinaException(status, nativeStatusCode, diagnostic);
    };
  }

  private static String message(MapvinaStatus status, int nativeStatusCode, String diagnostic) {
    var detail =
        diagnostic == null || diagnostic.isBlank() ? "No native diagnostic available." : diagnostic;
    return "%s (%d): %s".formatted(status, nativeStatusCode, detail);
  }
}
