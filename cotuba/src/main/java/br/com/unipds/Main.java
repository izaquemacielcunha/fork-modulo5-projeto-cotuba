package br.com.unipds;

import br.com.unipds.commandline.CommandLineParams;
import br.com.unipds.commandline.CommandLineParser;
import br.com.unipds.directory.Directory;
import br.com.unipds.directory.DirectoryInfos;
import br.com.unipds.filecreator.EpubCreator;
import br.com.unipds.filecreator.PdfCreator;
import br.com.unipds.filecreator.markdown.Markdown;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.commonmark.parser.Parser;

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


            if ("pdf".equals(directoryInfos.formato())) {
                PdfCreator pdfWiter = new PdfCreator(directoryInfos, new Markdown(directoryInfos.diretorioDosMD(), Parser.builder().build()));
                pdfWiter.create();
            } else if ("epub".equals(directoryInfos.formato())) {
                EpubCreator epubCreator = new EpubCreator(directoryInfos, new Markdown(directoryInfos.diretorioDosMD(), Parser.builder().build()));
                epubCreator.create();
            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + directoryInfos.formato());
            }

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