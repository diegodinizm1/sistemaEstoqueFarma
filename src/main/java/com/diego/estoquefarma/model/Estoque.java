package com.diego.estoquefarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "estoque")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estoque {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_lote")
    private Lote lote;

    @Column(name = "quantidade_disponivel", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeDisponivel;
}
