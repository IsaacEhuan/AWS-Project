package com.siceiapp.crud.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.siceiapp.crud.dto.ProfesorDTO;
import com.siceiapp.crud.dto.request.PreProfesorRequest;
import com.siceiapp.crud.service.ProfesorService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/profesores")
@CrossOrigin(origins = {"*"})
public class ProfesorController {
    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }
    @GetMapping("")
    @Operation(summary = "Listar a todos los profesores")
    public ResponseEntity<List<ProfesorDTO>> getAllProfesores() {
        List<ProfesorDTO> profesores = this.profesorService.getProfesores();
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Listar a un profesor según su ID")
    public ResponseEntity<ProfesorDTO> getProfesorById(@PathVariable int id){
        ProfesorDTO newProfesor = this.profesorService.getProfesorById(id);
        if(newProfesor == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newProfesor,HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo profesor")
    public ResponseEntity<ProfesorDTO> createProfesor(@Valid @RequestBody PreProfesorRequest info){
        ProfesorDTO newProfesor = this.profesorService.createProfesor(info);
        return new ResponseEntity<>(newProfesor,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar profesor existente")
    public ResponseEntity<ProfesorDTO> editProfesorByid(@Valid @RequestBody PreProfesorRequest info, @PathVariable int id){
        ProfesorDTO newProfesor = this.profesorService.actualizar(id,info);
        return new ResponseEntity<>(newProfesor,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un profesor según su ID")
    public ResponseEntity<ProfesorDTO> delete(@PathVariable int id){
        ProfesorDTO newProfesor = this.profesorService.deleteProfesor(id);

        if (newProfesor != null) {
            return new ResponseEntity<>(newProfesor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
