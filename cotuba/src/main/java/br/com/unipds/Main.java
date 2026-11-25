package br.com.unipds;

import br.com.unipds.commandline.CommandLineParams;
import br.com.unipds.commandline.CommandLineParser;
import br.com.unipds.cotuba.CotubaParams;
import br.com.unipds.cotuba.CotubaService;
import br.com.unipds.directory.Directory;
import br.com.unipds.directory.DirectoryInfos;
import br.com.unipds.markdown.MarkdownRender;
import br.com.unipds.markdown.MarkdownRepository;
import br.com.unipds.properties.PropertiesReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Properties;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {

        boolean isModoVerboso = true;

        try {
            CommandLineParser commandLineParser = new CommandLineParser(
                    new CommandLineParams(args, new Options(), new HelpFormatter(), new DefaultParser())
            );
            CommandLine cmd = commandLineParser.parse();

            Directory directory = new Directory(cmd);
            DirectoryInfos directoryInfos = directory.buildDirectoryInfos();
            isModoVerboso = directoryInfos.isModoVerboso();

            MarkdownRepository markdownRepository = new MarkdownRepository(directoryInfos.diretorioDosMD());
            MarkdownRender markdownRender = new MarkdownRender(Parser.builder().build(), HtmlRenderer.builder().build(), markdownRepository);

            PropertiesReader propertiesReader = new PropertiesReader(directoryInfos.diretorioDosMD(), new Properties());

            CotubaService cotubaService = new CotubaService(new CotubaParams(directoryInfos, markdownRender, propertiesReader));
            cotubaService.generateEbook();

            IO.println("Arquivo gerado com sucesso: " + directoryInfos.arquivoDeSaida());
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (isModoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }
}