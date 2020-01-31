package com.devonfw.module.batch.common.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Root of all (sub-) commands
 *
 */
@Component
@Command(name = "", subcommands = { HelpCommand.class, JobsCommand.class, ExecutionsCommand.class },

    synopsisHeading = "", synopsisSubcommandLabel = "", description = { "The general form of execution is:",
    "@|white,bold java -Dspring.datasource.url=<connection string> -jar " + RootCommand.JAR_NAME
        + " COMMAND SUBCOMMAND <params>|@%n",
    "To get further help for the desired COMMAND or SUBCOMMAND use the HELP command as follows:%n",
    "1. @|white,bold java -D... -jar ... help COMMAND|@",
    "2. @|white,bold java -D... -jar ... help COMMAND SUBCOMMAND|@%n",
    "1. will list available subcommands for the given command.",
    "2. will show available parameters for the given subcommand.%n", "@|underline Example|@%n",
    "List all jobs, using H2 database",
    "@|white,bold java -D'spring.datasource.url=jdbc:h2:~/mts;AUTO_SERVER=TRUE' -jar " + RootCommand.JAR_NAME
        + " jobs list|@%n" })
public class RootCommand implements Callable<Integer> {
  public final static String JAR_NAME = "devon4j-batch-tool.jar";

  @Override
  public Integer call() {

    new CommandLine(this).usage(System.out);
    return -1;
  }

}
