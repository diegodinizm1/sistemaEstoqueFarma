package com.diego.estoquefarma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovimentacaoViewController {

    @GetMapping("/view/movimentacoes")
    public String movimentacoesPage() {
        return "movimentacoes";
    }
}