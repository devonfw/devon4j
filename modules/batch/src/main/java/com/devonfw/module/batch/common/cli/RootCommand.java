package com.devonfw.module.batch.common.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Root of all (sub-) commands
 *
 */
@Component
@Command(name = "", subcommands = { JobsCommand.class, ExecutionsCommand.class }, synopsisSubcommandLabel = "COMMAND")
public class RootCommand implements Callable<Integer> {

  @Override
  public Integer call() {

    new CommandLine(this).usage(System.out);
    return -1;
  }

}
