package com.diego.estoquefarma.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RelatorioSaidaPivoteadoDTO(
        String nomeMedicamento,
        String numeroLote,
        Map<String, BigDecimal> quantidadePorSetor // Ex: {"UTI": 10, "Enfermaria A": 5}
) {}