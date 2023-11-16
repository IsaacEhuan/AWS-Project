package com.siceiapp.crud.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.siceiapp.crud.dto.AlumnoDTO;
import com.siceiapp.crud.dto.request.PreAlumnoRequest;
import com.siceiapp.crud.service.AlumnoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alumnos")
@CrossOrigin(origins = {"*"})
public class AlumnoController {
    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping("")
    @Operation(summary = "Listar a todos los alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAllAlumnos() {
        List<AlumnoDTO> alumnos = this.alumnoService.getAlumnos();
    return new ResponseEntity<>(alumnos,HttpStatus.OK);
}

    @GetMapping("/{id}")
    @Operation(summary = "Listar a un alumno segpun su id")
    public ResponseEntity<AlumnoDTO> getAlumnoById(@PathVariable int id){
        AlumnoDTO newAlumno = this.alumnoService.getAlumnoById(id);
        if(newAlumno == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newAlumno,HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo alumno")
    public ResponseEntity<?> createAlumno(@Valid @RequestBody PreAlumnoRequest info){
        AlumnoDTO newAlumno = this.alumnoService.createAlumno(info);
        return new ResponseEntity<>(newAlumno, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar alumno existente")
    public ResponseEntity<AlumnoDTO> editAlumnoByid(@Valid @RequestBody PreAlumnoRequest info, @PathVariable int id){
        AlumnoDTO newAlumno = this.alumnoService.actualizar(id,info);
        return new ResponseEntity<>(newAlumno,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estudiante según su ID")
    public ResponseEntity<AlumnoDTO> delete(@PathVariable int id){
        AlumnoDTO alumnoAEliminar = this.alumnoService.deleteAlumno(id);

        if (alumnoAEliminar != null) {
            return new ResponseEntity<>(alumnoAEliminar, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
