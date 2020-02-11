package com.devonfw.module.batch.common.cli;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

/**
 * Spring Application Runner, which executes our CLI application via PicoCli.
 *
 */
@Component
public class PicoCliRunner implements ExitCodeGenerator, CommandLineRunner {

  @Inject
  private RootCommand topCommand;

  @Inject
  private IFactory factory;

  private int exitCode;

  @Override
  public void run(String... args) throws Exception {

    this.exitCode = new CommandLine(this.topCommand, this.factory).execute(args);
  }

  @Override
  public int getExitCode() {

    return this.exitCode;
  }

}
