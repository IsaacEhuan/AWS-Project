package com.siceiapp.crud.service;

import com.siceiapp.crud.dto.ProfesorDTO;
import com.siceiapp.crud.dto.request.PreProfesorRequest;
import com.siceiapp.crud.exceptions.BusinessException;
import com.siceiapp.crud.repositories.ProfesorRepository;
import com.siceiapp.crud.schema.ProfesorSchema;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<ProfesorDTO> getProfesores() {
        return profesorRepository
                .findAll()
                .stream()
                .map(ProfesorDTO::getFromSchema)
                .toList();
    }

    //Obtener el profesor según su ID
    public ProfesorDTO getProfesorById(int id) {
        Optional<ProfesorSchema> profesor = profesorRepository
                .findById(id);
        return profesor.map(ProfesorDTO::getFromSchema).orElse(null);
    }
    //Crear un nuevo profesor
    public ProfesorDTO createProfesor(PreProfesorRequest profesorAuxiliar){
        if(profesorRepository.findById(profesorAuxiliar.getId()).isPresent()){
            throw BusinessException
                    .builder()
                    .message("Mismo id")
                    .build();
        }

        ProfesorSchema profesorSchema = new ProfesorSchema();
        profesorSchema.setNombres(profesorAuxiliar.getNombres());
        profesorSchema.setApellidos(profesorAuxiliar.getApellidos());
        profesorSchema.setHorasClase(profesorAuxiliar.getHorasClase());
        profesorSchema.setNumeroEmpleado(profesorAuxiliar.getNumeroEmpleado());

        ProfesorSchema profesorGuadado = profesorRepository.save(profesorSchema);

        return ProfesorDTO.getFromSchema(profesorGuadado);
    }

    //Actualizar datos de un profesor existente
    public ProfesorDTO actualizar(int id, PreProfesorRequest profesorAuxiliar){
        Optional<ProfesorSchema> profesorExistente = profesorRepository.findById(id);

        if (profesorExistente.isPresent()) {
            ProfesorSchema profesor = profesorExistente.get();
            profesor.setNumeroEmpleado(profesorAuxiliar.getNumeroEmpleado());
            profesor.setNombres(profesorAuxiliar.getNombres());
            profesor.setApellidos(profesorAuxiliar.getApellidos());
            profesor.setHorasClase(profesorAuxiliar.getHorasClase());

            ProfesorSchema profesorActualizado = profesorRepository.save(profesor);

            return ProfesorDTO.getFromSchema(profesorActualizado);
        } else {
            return null;
        }
    }

    //Eliminar a un profesor
    public ProfesorDTO deleteProfesor(int id){
        Optional<ProfesorSchema> profesorAEliminar = profesorRepository.findById(id);

        if (profesorAEliminar.isPresent()) {
            profesorRepository.deleteById(id);
            return ProfesorDTO.getFromSchema(profesorAEliminar.get());
        } else {
            return null;
        }
    }
}
