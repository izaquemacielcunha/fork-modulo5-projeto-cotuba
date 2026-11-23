package br.com.unipds.directory;

import java.nio.file.Path;

public record DirectoryInfos(
        Path diretorioDosMD,
        String formato,
        Path arquivoDeSaida,
        boolean isModoVerboso)
{ }
