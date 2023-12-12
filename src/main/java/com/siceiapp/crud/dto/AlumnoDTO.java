package com.siceiapp.crud.dto;

import com.siceiapp.crud.schema.AlumnoSchema;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlumnoDTO {
    private int id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private double promedio;
    private String fotoPerfilUrl;
    private String password;

    public AlumnoDTO(String nombres, String apellidos, String matricula, double promedio) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.promedio = promedio;
    }

    public static AlumnoDTO getFromSchema(AlumnoSchema alumnoSchema){
        return AlumnoDTO
                .builder()
                .id(alumnoSchema.getId())
                .nombres(alumnoSchema.getNombres())
                .apellidos(alumnoSchema.getApellidos())
                .matricula(alumnoSchema.getMatricula())
                .promedio(alumnoSchema.getPromedio())
                .password(alumnoSchema.getPassword())
                .fotoPerfilUrl(alumnoSchema.getFotoPerfilUrl())
                .build();
    }

}
