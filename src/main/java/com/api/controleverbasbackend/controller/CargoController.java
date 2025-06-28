package com.api.controleverbasbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.controleverbasbackend.dto.DadosListagemCargo;
import com.api.controleverbasbackend.service.CargoService;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemCargo>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable) {

        Page<DadosListagemCargo> page = cargoService.listar(pageable);
        return ResponseEntity.ok(page);
    }
}
