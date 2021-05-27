package com.math.bifurcation.health;

import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * @author Leonid Cheremshantsev
 */
public abstract class HealthIndicator extends DiskSpaceHealthIndicator {

    public HealthIndicator(File path, DataSize threshold) {
        super(path, threshold);
    }

    public String toHuman(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
}
