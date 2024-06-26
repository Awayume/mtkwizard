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
@Command(name = "version", description = "Print version information and exit.", hidden = true)
public class VersionCommand implements Runnable {
    private final static Logger logger = LogManager.getLogger(VersionCommand.class);

    /**
     * Print version information and exit.
     */
    @Override
    public void run() {
        this.logger.debug("Executing the 'version' command");
        Manifest manifest = new Manifest();
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        String buildType;
        if (manifest.buildType.equals("release")) {
            buildType = "";
        } else {
            buildType = String.format(" (%s)", manifest.buildType);
        }
        String jvm = String.format(
            "%s %s (%s %s)",
            System.getProperty("java.vm.name"),
            System.getProperty("java.version"),
            System.getProperty("java.vm.vendor"),
            System.getProperty("java.vm.version")
        );
        String os = String.format(
            "%s %s (%s)", System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
        );
        String info = "\n------------------------------------------------------------\n"
                      + String.format("MTK Wizard version %s%s\n", manifest.version, buildType)
                      + "------------------------------------------------------------\n\n"
                      + String.format("Build time:   %s\n", manifest.createdAt.format(dtFormatter)).replace("Z", "UTC")
                      + String.format("Revision:     %s\n\n", manifest.revision)
                      + String.format("JVM:          %s\n", jvm)
                      + String.format("OS:           %s\n\n", os);
        System.out.println(info);
        this.logger.debug("Execution of the 'version' command completed");
    }
}