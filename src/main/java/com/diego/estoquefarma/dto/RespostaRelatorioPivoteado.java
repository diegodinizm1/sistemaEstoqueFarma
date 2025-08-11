package com.diego.estoquefarma.dto;

import java.util.List;

public record RespostaRelatorioPivoteado(
        List<String> nomeSetores, // Lista de todos os setores para os cabeçalhos da tabela
        List<RelatorioSaidaPivoteadoDTO> dados // Os dados já agrupados
) {}