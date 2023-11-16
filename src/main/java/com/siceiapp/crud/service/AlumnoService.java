package com.siceiapp.crud.service;

import com.siceiapp.crud.dto.AlumnoDTO;
import com.siceiapp.crud.dto.request.PreAlumnoRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.*;
@Service
public class AlumnoService {
        private List<AlumnoDTO> alumnos = new ArrayList<>();

    public AlumnoService(){
        this.alumnos= new ArrayList<>();
    }

    public List<AlumnoDTO> getAlumnos() {
        return alumnos;
    }

    //Obtener los alumnos según su ID
    public AlumnoDTO getAlumnoById(int id){
        for (AlumnoDTO alumno : alumnos) {
            if (alumno.getId() == id) {
                return alumno;
            }
        }
        return null;
    }

    //Funcion para crear un nuevo alumno
    public AlumnoDTO createAlumno(PreAlumnoRequest alumnoAuxiliar){
        AlumnoDTO alumnoNuevo = new AlumnoDTO(alumnoAuxiliar.getId(), alumnoAuxiliar.getNombres(), alumnoAuxiliar.getApellidos(), alumnoAuxiliar.getMatricula(), alumnoAuxiliar.getPromedio());
        alumnos.add(alumnoNuevo);
        return alumnoNuevo;
    }

    //Funcion para edtar un alumno existente
    public AlumnoDTO actualizar(int id, PreAlumnoRequest alumnoAuxiliar){
        Optional<AlumnoDTO> alumnoExistente = alumnos.stream().filter(a -> a.getId() == id).findFirst();
        alumnoExistente.ifPresent(value -> {
            value.setNombres(alumnoAuxiliar.getNombres());
            value.setApellidos(alumnoAuxiliar.getApellidos());
            value.setMatricula(alumnoAuxiliar.getMatricula());
            value.setPromedio(alumnoAuxiliar.getPromedio());
        });
        return alumnoExistente.orElse(null);
    }

    //Funcion para eliminar un alumno
    public AlumnoDTO deleteAlumno(int id){
        AlumnoDTO alumnoParaEliminar = null;
        for (AlumnoDTO alumno : alumnos) {
            if (alumno.getId() == id) {
                alumnoParaEliminar = alumno;
                break; // Salir del bucle una vez que se encuentre el alumno con el ID buscado
            }
        }

        if (alumnoParaEliminar != null) {
            alumnos.remove(alumnoParaEliminar);
            System.out.println("El alumno " + id + " ha sido eliminado correctamente.");
        } else {
            System.out.println("El alumno" + id + "no se ha encontrado");
        }
        return alumnoParaEliminar;
    }


}
