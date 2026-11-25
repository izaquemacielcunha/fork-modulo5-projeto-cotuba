package br.com.unipds.directory;

import br.com.unipds.ebook.EbookType;

import java.nio.file.Path;

public record DirectoryInfos(
        Path diretorioDosMD,
        EbookType formato,
        Path arquivoDeSaida,
        boolean isModoVerboso)
{ }
