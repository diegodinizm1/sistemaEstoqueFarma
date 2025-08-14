package com.diego.estoquefarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "medicamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(name = "descricao_detalhada")
    private String descricaoDetalhada;

    @Column(name = "unidade_medida")
    private String unidadeMedida;

    @Column(nullable = false)
    private boolean ativo = true;
}
