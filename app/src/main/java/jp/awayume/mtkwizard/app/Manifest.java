// SPDX-FileCopyrightText: 2024 Awayume <dev@awayume.jp>
// SPDX-License-Identifier: GPL-3.0-only

package jp.awayume.mtkwizard.app;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.jar.Attributes;
// import java.util.jar.Manifest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import picocli.CommandLine.IVersionProvider;


/**
 * A class to parse the jar manifest.
 *
 * @author Awayume {@literal <dev@awayume.jp>}
 * @since 0.1
 */
public final class Manifest {
    @SuppressWarnings("nullness:initialization")
    public static String buildType;
    @SuppressWarnings("nullness:initialization")
    public static String version;
    @SuppressWarnings("nullness:initialization")
    public static String revision;
    @SuppressWarnings("nullness:initialization")
    public static ZonedDateTime createdAt;
    private static boolean isLoaded = false;
    private static final Logger logger = LogManager.getLogger(Manifest.class);

    /**
     * Loads the manifest and parses it.
     *
     * Once the manifest is loaded, its values are cached.
     */
    @SuppressWarnings({"StaticAssignmentInConstructor", "PMD.AvoidThrowingRawExceptionTypes"})
    public Manifest() {
        if (!this.isLoaded) {
            this.logger.debug("Loading the jar manifest...");
            java.util.jar.Manifest manifest;
            try {
                InputStream manifestStream = ClassLoader.getSystemResourceAsStream("META-INF/MANIFEST.MF");
                if (manifestStream == null) {
                    throw new IOException("The jar manifest not found");
                }
                manifest = new java.util.jar.Manifest(manifestStream);
            } catch (IOException e) {
                this.logger.fatal("An unexpected exception occured", e);
                throw new RuntimeException("An unexpected exception occured", e);
            }
            Attributes manifestAttributes = manifest.getMainAttributes();
            this.buildType = manifestAttributes.getValue("Build-Type");
            this.version = manifestAttributes.getValue("Version");
            this.revision = manifestAttributes.getValue("Revision");
            this.createdAt = ZonedDateTime.parse(manifestAttributes.getValue("Created-At"));
            this.isLoaded = true;
            this.logger.debug("Loaded");
        } else {
            this.logger.debug("The jar manifest has already loaded so using the cached values");
        }
    }

    /**
     * The version provider for Picocli.
     *
     * @author Awayume {@literal <dev@awayume.jp>}
     * @since 0.1
     */
    public static class VersionProvider implements IVersionProvider {
        /**
         * Parses the jar manifest and returns the formatted version string.
         *
         * @return The formatted version string
         */
        @Override
        public String[] getVersion() throws Exception {
            Manifest manifest = new Manifest();
            return new String[] {
                String.format("MTK Wizard version %s", manifest.version),
                "Copyright (C) 2024 Awayume",
            };
        }
    }
}
