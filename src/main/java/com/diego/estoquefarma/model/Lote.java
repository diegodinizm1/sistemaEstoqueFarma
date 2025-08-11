package com.diego.estoquefarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "lotes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_medicamento", "numeroLote"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    @Column(name = "numero_lote", nullable = false)
    private String numeroLote;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;
}
