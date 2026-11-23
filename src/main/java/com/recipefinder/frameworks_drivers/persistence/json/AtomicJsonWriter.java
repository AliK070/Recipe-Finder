package com.recipefinder.frameworks_drivers.persistence.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/** Writes JSON through a temporary sibling file to avoid replacing good data with a partial write. */
public final class AtomicJsonWriter {
    public void write(Path target, String json) throws IOException {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(json, "json");

        Path absoluteTarget = target.toAbsolutePath();
        Path parent = absoluteTarget.getParent();
        Files.createDirectories(parent);

        String prefix = absoluteTarget.getFileName().toString();
        if (prefix.length() < 3) {
            prefix = "json";
        }
        Path temporaryFile = Files.createTempFile(parent, prefix, ".tmp");
        try {
            Files.writeString(
                    temporaryFile,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            try {
                Files.move(
                        temporaryFile,
                        absoluteTarget,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, absoluteTarget, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
