package com.diego.estoquefarma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AutenticacaoViewController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você saiu do sistema com sucesso.");
        }
        return "login";
    }
}