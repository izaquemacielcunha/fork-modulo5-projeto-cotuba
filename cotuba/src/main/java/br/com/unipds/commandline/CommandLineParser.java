package br.com.unipds.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class CommandLineParser {

    private final CommandLineParams params;

    public CommandLineParser(CommandLineParams params) {
        this.params = params;
    }

    public CommandLine parse() {
        return getCommandLine();
    }

    private CommandLine getCommandLine() {

        var opcaoDeDiretorioDosMD = new Option("d", "dir", true,
                "Diretório que contém os arquivos md. Default: diretório atual.");
        params.options().addOption(opcaoDeDiretorioDosMD);

        var opcaoDeFormatoDoEbook = new Option("f", "format", true,
                "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
        params.options().addOption(opcaoDeFormatoDoEbook);

        var opcaoDeArquivoDeSaida = new Option("o", "output", true,
                "Arquivo de saída do ebook. Default: book.{formato}.");
        params.options().addOption(opcaoDeArquivoDeSaida);

        var opcaoModoVerboso = new Option("v", "verbose", false,
                "Habilita modo verboso.");
        params.options().addOption(opcaoModoVerboso);

        try {
            CommandLine cmd = params.cmdParser().parse(params.options(), params.args());
            return cmd;
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            params.ajuda().printHelp("cotuba", params.options());
            throw new RuntimeException("Erro ao analisar a linha de comando", ex);
        }
    }
}
