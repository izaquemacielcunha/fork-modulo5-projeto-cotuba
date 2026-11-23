package br.com.unipds.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;

public class Directory {

    private final CommandLine cmd;

    public Directory(CommandLine cmd) {
        this.cmd = cmd;
    }

    public DirectoryInfos buildDirectoryInfos() {
        Path diretorioDosMD;
        String formato;
        Path arquivoDeSaida;
        boolean modoVerboso;

        try {

            String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

            if (nomeDoDiretorioDosMD != null) {
                diretorioDosMD = Paths.get(nomeDoDiretorioDosMD);
                if (!Files.isDirectory(diretorioDosMD)) {
                    throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
                }
            } else {
                diretorioDosMD = Paths.get("");
            }

            String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

            if (nomeDoFormatoDoEbook != null) {
                formato = nomeDoFormatoDoEbook.toLowerCase();
            } else {
                formato = "pdf";
            }

            String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");

            if (nomeDoArquivoDeSaidaDoEbook != null) {
                arquivoDeSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
            } else {
                arquivoDeSaida = Paths.get("book." + formato.toLowerCase());
            }
            if (Files.isDirectory(arquivoDeSaida)) {
                Files.walk(arquivoDeSaida).sorted(Comparator.reverseOrder())
                        .map(Path::toFile).forEach(File::delete);
            } else {
                Files.deleteIfExists(arquivoDeSaida);
            }

            modoVerboso = cmd.hasOption("verbose");

            return new DirectoryInfos(diretorioDosMD, formato, arquivoDeSaida, modoVerboso);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o diretório.", e);
        }

    }
}