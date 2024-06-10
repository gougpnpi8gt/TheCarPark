package com.work.thecarpark.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/park")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControllerPark {
    @GetMapping()
    public String index() {
        return "main/index";
    }
}