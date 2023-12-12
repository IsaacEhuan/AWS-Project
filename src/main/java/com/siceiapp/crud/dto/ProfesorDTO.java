package com.siceiapp.crud.dto;

import com.siceiapp.crud.schema.ProfesorSchema;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProfesorDTO {
    private int id;
    private int numeroEmpleado;
    private String nombres;
    private String apellidos;
    private  int horasClase;

    public static ProfesorDTO getFromSchema(ProfesorSchema profesorSchema){
        return ProfesorDTO
                .builder()
                .id(profesorSchema.getId())
                .nombres(profesorSchema.getNombres())
                .apellidos(profesorSchema.getApellidos())
                .horasClase(profesorSchema.getHorasClase())
                .numeroEmpleado(profesorSchema.getNumeroEmpleado())
                .build();
    }
    
}
