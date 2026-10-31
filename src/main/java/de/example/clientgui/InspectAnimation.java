package de.example.clientgui;

public final class InspectAnimation {
    private static long startNanos = -1;
    private InspectAnimation() {}

    public static void trigger() {
        if (AuroraConfig.inspect) startNanos = System.nanoTime();
    }
    public static float progress() {
        if (startNanos < 0) return -1f;
        float elapsed = (System.nanoTime() - startNanos) / 1_000_000_000f;
        if (elapsed > 1.55f) {
            startNanos = -1;
            return -1f;
        }
        return elapsed / 1.55f;
    }
}
