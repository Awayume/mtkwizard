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
    private static String buildType;
    @SuppressWarnings("nullness:initialization")
    private static String version;
    @SuppressWarnings("nullness:initialization")
    private static String revision;
    @SuppressWarnings("nullness:initialization")
    private static ZonedDateTime createdAt;
    private static boolean isLoaded;
    private static final Logger logger = LogManager.getLogger(Manifest.class);

    /**
     * Loads the manifest and parses it.
     *
     * Once the manifest is loaded, its values are cached.
     */
    @SuppressWarnings({"StaticAssignmentInConstructor", "PMD.AssignmentToNonFinalStatic", "PMD.AvoidThrowingRawExceptionTypes"})
    public Manifest() {
        if (this.isLoaded) {
            this.logger.debug("The jar manifest has already loaded so using the cached values");
        } else {
            this.logger.debug("Loading the jar manifest...");
            @SuppressWarnings({"PMD.LooseCoupling", "PMD.PrematureDeclaration"})
            final Attributes attributes;
            try (InputStream manifestStream = ClassLoader.getSystemResourceAsStream("META-INF/MANIFEST.MF")) {
                if (manifestStream == null) {
                    this.logger.fatal("Failed to load the jar manifest");
                    throw new RuntimeException(new IOException("Failed to load the jar manifest"));
                }
                attributes = new java.util.jar.Manifest(manifestStream).getMainAttributes();
            } catch (IOException e) {
                this.logger.fatal("An unexpected exception occured", e);
                throw new RuntimeException(e);
            }
            this.buildType = attributes.getValue("Build-Type");
            this.version = attributes.getValue("Version");
            this.revision = attributes.getValue("Revision");
            this.createdAt = ZonedDateTime.parse(attributes.getValue("Created-At"));
            this.isLoaded = true;
            this.logger.debug("Loaded");
        }
    }

    /**
     * Returns the build type.
     *
     * @return The build type
     */
    public String getBuildType() {
        return this.buildType;
    }

    /**
     * Returns the version.
     *
     * @return The version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the revision.
     *
     * @return The revision
     */
    public String getRevision() {
        return this.revision;
    }

    /**
     * Returns the build time.
     *
     * @return The build time
     */
    public ZonedDateTime getBuildTime() {
        return this.createdAt;
    }

    /**
     * The version provider for Picocli.
     *
     * @author Awayume {@literal <dev@awayume.jp>}
     * @since 0.1
     */
    public static final class VersionProvider implements IVersionProvider {
        /**
         * Parses the jar manifest and returns the formatted version string.
         *
         * @return The formatted version string
         */
        @Override
        public String[] getVersion() throws Exception {
            final Manifest manifest = new Manifest();
            return new String[] {
                String.format("MTK Wizard version %s", manifest.version),
                "Copyright (C) 2024 Awayume",
            };
        }
    }
}
