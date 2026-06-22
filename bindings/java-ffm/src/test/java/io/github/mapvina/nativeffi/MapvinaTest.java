package io.github.mapvina.nativeffi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.mapvina.nativeffi.geo.LatLng;
import io.github.mapvina.nativeffi.log.LogSeverity;
import io.github.mapvina.nativeffi.runtime.NetworkStatus;
import io.github.mapvina.nativeffi.test.NativeTestSupport;

final class MapvinaTest {
  @BeforeAll
  static void loadNativeLibrary() {
    NativeTestSupport.loadNativeLibrary();
  }

  @AfterEach
  void restoreProcessState() {
    Mapvina.clearLogCallback();
    Mapvina.restoreDefaultAsyncLogSeverities();
  }

  @Test
  void exposesCVersionAndSupportedBackends() {
    assertEquals(0, Mapvina.cVersion());
    assertNotNull(Mapvina.supportedRenderBackends());
  }

  @Test
  void getsAndSetsNetworkStatus() {
    var original = Mapvina.networkStatus();
    try {
      Mapvina.setNetworkStatus(NetworkStatus.OFFLINE);
      assertEquals(NetworkStatus.OFFLINE, Mapvina.networkStatus());
      Mapvina.setNetworkStatus(NetworkStatus.ONLINE);
      assertEquals(NetworkStatus.ONLINE, Mapvina.networkStatus());
    } finally {
      Mapvina.setNetworkStatus(original);
    }
  }

  @Test
  void configuresAsyncLogSeverities() {
    Mapvina.setAsyncLogSeverities(EnumSet.noneOf(LogSeverity.class));
    Mapvina.setAsyncLogSeverities(EnumSet.of(LogSeverity.INFO, LogSeverity.WARNING));
    assertThrows(
        IllegalArgumentException.class,
        () -> Mapvina.setAsyncLogSeverities(EnumSet.of(LogSeverity.UNKNOWN)));
    Mapvina.restoreDefaultAsyncLogSeverities();
  }

  @Test
  void convertsProjectedMeters() {
    var meters = Mapvina.projectedMetersForLatLng(new LatLng(0, 0));
    assertEquals(0.0, meters.northing(), 1e-9);
    assertEquals(0.0, meters.easting(), 1e-9);
    var coordinate = Mapvina.latLngForProjectedMeters(meters);
    assertEquals(0.0, coordinate.latitude(), 1e-9);
    assertEquals(0.0, coordinate.longitude(), 1e-9);
  }

  @Test
  void loggingCallbackCanBeInstalledAndCleared() {
    Mapvina.setLogCallback(record -> true);
    Mapvina.clearLogCallback();
  }
}
