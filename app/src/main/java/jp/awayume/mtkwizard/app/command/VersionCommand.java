// SPDX-FileCopyrightText: 2024 Awayume <dev@awayume.jp>
// SPDX-License-Identifier: GPL-3.0-only

package jp.awayume.mtkwizard.app.command;

import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import picocli.CommandLine.Command;

import jp.awayume.mtkwizard.app.Manifest;


/**
 * A class for the "version" command.
 *
 * @author Awayume {@literal <dev@awayume.jp>}
 * @since 0.1
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
@Command(name = "version", description = "Print version information and exit.", hidden = true)
public final class VersionCommand implements Runnable {
    private final static Logger logger = LogManager.getLogger(VersionCommand.class);

    /**
     * Print version information and exit.
     */
    @Override
    public void run() {
        this.logger.debug("Executing the 'version' command");
        final Manifest manifest = new Manifest();
        final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        final String buildType = manifest.getBuildType();
        final String buildTypeStr;
        if (buildType.equals("release")) {
            buildTypeStr = "";
        } else {
            buildTypeStr = String.format(" (%s)", buildType);
        }
        final String jvmInfo = String.format(
            "%s %s (%s %s)",
            System.getProperty("java.vm.name"),
            System.getProperty("java.version"),
            System.getProperty("java.vm.vendor"),
            System.getProperty("java.vm.version")
        );
        final String osInfo = String.format(
            "%s %s (%s)", System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
        );
        final String info = "\n------------------------------------------------------------\n"
                      + String.format("MTK Wizard version %s%s\n", manifest.getVersion(), buildTypeStr)
                      + "------------------------------------------------------------\n\n"
                      + String.format("Build time:   %s\n", manifest.getBuildTime().format(dtFormatter)).replace("Z", "UTC")
                      + String.format("Revision:     %s\n\n", manifest.getRevision())
                      + String.format("JVM:          %s\n", jvmInfo)
                      + String.format("OS:           %s\n\n", osInfo);
        @SuppressWarnings("PMD.SystemPrintln")
        final Runnable runner = () -> { System.out.println(info); };
        runner.run();
        this.logger.debug("Execution of the 'version' command completed");
    }
}