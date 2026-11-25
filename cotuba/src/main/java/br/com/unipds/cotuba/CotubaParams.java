package br.com.unipds.cotuba;

import br.com.unipds.directory.DirectoryInfos;
import br.com.unipds.markdown.MarkdownRender;
import br.com.unipds.properties.PropertiesReader;

public record CotubaParams(
        DirectoryInfos directoryInfos,
        MarkdownRender markdownRender,
        PropertiesReader propertiesReader
) { }