package com.arya.bot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AryaController {

    @GetMapping("/status")
    public String status() {
        return "Arya is online! Hindi, English, and Marathi support coming soon.";
    }
}