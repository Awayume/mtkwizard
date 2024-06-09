package jp.awayume.mtkwizard.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.awayume.mtkwizard.app.Manifest;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Manifest manifest = new Manifest();
        Main.logger.debug(
            String.format("MTK Wizard (version %s, revision %s) started.", manifest.version, manifest.revision)
        );
    }
}
