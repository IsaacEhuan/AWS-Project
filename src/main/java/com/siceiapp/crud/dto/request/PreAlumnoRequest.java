package com.siceiapp.crud.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PreAlumnoRequest {
    
    private int id;
    
    @NotNull
    @NotBlank
    private String nombres;
    
    @NotNull
    @NotBlank
    private String apellidos;

    @Pattern(regexp = "^[a-zA-Z]+\\d+$", message = "La matrícula debe comenzar con una letra mayúscula o minúscula segida de almenos un número")
    private String matricula;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El promedio ingresado debe ser un número  mayor o igual a 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El promedio ingrsado debe ser un número  menor o igual a 100")

    private double promedio;
    
    private String fotoPerfilUrl;

    private String password;
}
