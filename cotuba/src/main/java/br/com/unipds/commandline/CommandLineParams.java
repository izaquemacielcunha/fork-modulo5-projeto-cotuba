package br.com.unipds.commandline;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public record CommandLineParams(
        String[] args,
        Options options,
        HelpFormatter ajuda,
        CommandLineParser cmdParser
) { }