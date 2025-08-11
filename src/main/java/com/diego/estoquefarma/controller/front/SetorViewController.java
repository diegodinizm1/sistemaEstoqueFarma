package com.diego.estoquefarma.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SetorViewController {

    @GetMapping("/view/setores")
    public String setoresPage() {
        return "setores";
    }
}