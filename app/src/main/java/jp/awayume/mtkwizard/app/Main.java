// SPDX-FileCopyrightText: 2024 Awayume <dev@awayume.jp>
// SPDX-License-Identifier: GPL-3.0-only

package jp.awayume.mtkwizard.app;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import jp.awayume.mtkwizard.app.Manifest;


@Command(
    name = "mtkwizard",
    versionProvider = Manifest.VersionProvider.class,
    description = "A tool to flash MediaTek devices",
    footer = "This software is an Open Source Software.\n"
            + "Please report issues at https://github.com/Awayume/mtkwizard/issues",
    mixinStandardHelpOptions = true
)
public class Main implements Runnable {
    private static final Logger logger = LogManager.getLogger(Main.class);
    @Spec
    @SuppressWarnings("initialization")
    private CommandSpec commandSpec;

    public static void main(String[] args) {
        Manifest manifest = new Manifest();
        Main.logger.debug(
            String.format("MTK Wizard (version %s, revision %s) started.", manifest.version, manifest.revision)
        );

        Main.logger.debug(String.format("The command line arguments is: %s", Arrays.toString(args)));
        Main.logger.debug("Execute a command");
        int exitCode = new CommandLine(new Main()).execute(args);
        Main.logger.debug(String.format("The exit code is: %d", exitCode));
        System.exit(exitCode);
    }

    @Override
    public void run() {
        this.commandSpec.commandLine().printVersionHelp(System.out);
    }
}
