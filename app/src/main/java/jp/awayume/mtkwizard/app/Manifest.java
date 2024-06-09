package jp.awayume.mtkwizard.app;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.jar.Attributes;
// import java.util.jar.Manifest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class Manifest {
    public String buildType;
    public String version;
    public String revision;
    public ZonedDateTime createdAt;
    private static final Logger logger = LogManager.getLogger(Manifest.class);

    public Manifest() {
        java.util.jar.Manifest manifest;
        try {
            InputStream manifestStream = ClassLoader.getSystemResourceAsStream("META-INF/MANIFEST.MF");
            if (manifestStream == null) {
                throw new IOException("The JAR Manifest not found");
            }
            manifest = new java.util.jar.Manifest(manifestStream);
        } catch (IOException e) {
            this.logger.fatal("An Unexpected exception occured", e);
            throw new RuntimeException("An Unexpected exception occured", e);
        }
        Attributes manifestAttributes = manifest.getMainAttributes();
        this.buildType = manifestAttributes.getValue("Build-Type");
        this.version = manifestAttributes.getValue("Version");
        this.revision = manifestAttributes.getValue("Revision");
        this.createdAt = ZonedDateTime.parse(manifestAttributes.getValue("Created-At"));
    }
}
