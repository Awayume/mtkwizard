// SPDX-FileCopyrightText: 2024 Awayume <dev@awayume.jp>
// SPDX-License-Identifier: GPL-3.0-only

package jp.awayume.mtkwizard.app;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import jp.awayume.mtkwizard.app.Manifest;
import jp.awayume.mtkwizard.app.command.VersionCommand;


/**
 * The entry point of this software.
 *
 * @author Awayume {@literal <dev@awayume.jp>}
 * @since 0.1
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.ShortClassName", "PMD.UnnecessaryFullyQualifiedName"})
@Command(
    name = "mtkwizard",
    versionProvider = Manifest.VersionProvider.class,
    description = "A tool to flash MediaTek devices.\n",
    footer = "\nThis software is an Open Source Software.\n"
            + "Please report issues at https://github.com/Awayume/mtkwizard/issues",
    subcommands = {
        VersionCommand.class,
    }
)
public final class Main implements Runnable {
    private static final Logger logger = LogManager.getLogger(Main.class);
    @SuppressWarnings("nullness:initialization")
    @Spec
    private CommandSpec commandSpec;
    @SuppressWarnings("UnusedVariable")
    @Option(
        names = {"-h", "--help"},
        description = "Display help information about the specified command.",
        usageHelp = true
    )
    private boolean helpRequested;
    @Option(names = {"-V", "--version"}, description = "Print version information and exit.")
    private boolean versionRequested;

    /**
     * The entry point of this software.
     *
     * @param args The command line arguments
     */
    public static void main(final String[] args) {
        final Manifest manifest = new Manifest();
        Main.logger.debug(
            String.format(
                "MTK Wizard (version %s, revision %s) started.", manifest.getVersion(), manifest.getRevision()
            )
        );

        Main.logger.debug(String.format("The command line arguments is: %s", Arrays.toString(args)));
        final CommandLine commandLine = new CommandLine(new Main());
        // Add the 'help' command
        // NOTE: I hope this will be added automatically in the future
        final CommandSpec helpSpec = CommandSpec.forAnnotatedObject(new HelpCommand());
        helpSpec.usageMessage().hidden(true);
        commandLine.addSubcommand("help", new CommandLine(helpSpec));

        Main.logger.debug("Executing the command line");
        final int exitCode = commandLine.execute(args);
        Main.logger.debug(String.format("The exit code is: %d", exitCode));
        System.exit(exitCode);
    }

    /**
     * Executes a command.
     */
    @Override
    public void run() {
        if (versionRequested) {
            // NOTE: I hope this will be added automatically in the future
            this.logger.debug("The option '--version' is detected; Executing the 'version' command");
            @SuppressWarnings("PMD.UnnecessaryVarargsArrayCreation")
            final Runnable runner = () -> {
                this.commandSpec.commandLine().execute(new String[] {"version"});
            };
            runner.run();
        } else {
            this.logger.debug("No command is detected; Executing the 'help' command");
            this.commandSpec.commandLine().printVersionHelp(System.out);
        }
    }
}
