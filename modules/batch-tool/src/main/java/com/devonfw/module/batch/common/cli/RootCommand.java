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
    "@|white,bold java -jar ... -Dspring.datasource.url=<connection string>COMMAND SUBCOMMAND <params>|@%n",
    "To get further help for the desired COMMAND or SUBCOMMAND use the HELP command as follows:%n",
    "1. @|white,bold java -jar ... help COMMAND|@", "2. @|white,bold java -jar ... help COMMAND SUBCOMMAND|@%n",
    "1. will list available subcommands for the given command.",
    "2. will show available parameters for the given subcommand.%n", "@|underline Example|@%n",
    "List all jobs, using H2 database",
    "@|white,bold java -jar ... -Dspring.datasource.url=jdbc:h2:~/mts;AUTO_SERVER=TRUE jobs list|@%n" })
public class RootCommand implements Callable<Integer> {

  @Override
  public Integer call() {

    new CommandLine(this).usage(System.out);
    return -1;
  }

}
