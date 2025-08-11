package com.diego.estoquefarma.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedicamentoViewController {

    @GetMapping("/view/medicamentos")
    public String medicamentosPage() {
        return "medicamentos"; // Retorna o nome do arquivo medicamentos.html
    }
}