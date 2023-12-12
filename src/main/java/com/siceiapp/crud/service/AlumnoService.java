package com.siceiapp.crud.service;

import com.siceiapp.crud.Entity.Sesion;
import com.siceiapp.crud.dto.AlumnoDTO;
import com.siceiapp.crud.dto.request.PreAlumnoRequest;
import com.siceiapp.crud.exceptions.BusinessException;
import com.siceiapp.crud.repositories.AlumnoRepository;
import com.siceiapp.crud.repositories.BucketRepository;
import com.siceiapp.crud.repositories.SNSRepository;
import com.siceiapp.crud.repositories.SesionRepository;
import com.siceiapp.crud.schema.AlumnoSchema;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
@Service
public class AlumnoService {
    private final AlumnoRepository alumnoRepository;
    private final SesionRepository sesionRepository;
    private final BucketRepository bucketRepository;
    private final SNSRepository snsRepository;
    
    public AlumnoService(AlumnoRepository alumnoRepository, SesionRepository sesionRepository, BucketRepository bucketRepository, SNSRepository snsRepository) {
        this.alumnoRepository = alumnoRepository;
        this.sesionRepository = sesionRepository;
        this.bucketRepository = bucketRepository;
        this.snsRepository = snsRepository;
    }

    public List<AlumnoDTO> getAlumnos() {
        return alumnoRepository
                .findAll()
                .stream()
                .map(AlumnoDTO::getFromSchema)
                .toList();
    }

    //Obtener los alumnos según su ID
    public AlumnoDTO getAlumnoById(int id){
        Optional<AlumnoSchema> alumno = alumnoRepository
                .findById(id);
        return alumno.map(AlumnoDTO::getFromSchema).orElse(null);
    }

    //Funcion para crear un nuevo alumno
    public AlumnoDTO createAlumno(PreAlumnoRequest alumnoAuxiliar){
         if(alumnoRepository.findById(alumnoAuxiliar.getId()).isPresent()){
            throw BusinessException
                    .builder()
                    .message("Mismo id")
                    .build();
        }

        AlumnoSchema alumnoSchema = new AlumnoSchema();
        alumnoSchema.setNombres(alumnoAuxiliar.getNombres());
        alumnoSchema.setApellidos(alumnoAuxiliar.getApellidos());
        alumnoSchema.setMatricula(alumnoAuxiliar.getMatricula());
        alumnoSchema.setPromedio(alumnoAuxiliar.getPromedio());
        alumnoSchema.setPassword(alumnoAuxiliar.getPassword());

        AlumnoSchema alumnoGuardado = alumnoRepository.save(alumnoSchema);

        return AlumnoDTO.getFromSchema(alumnoGuardado);
    }

    //Funcion para edtar un alumno existente
    public AlumnoDTO actualizar(int id, PreAlumnoRequest alumnoAuxiliar){
        Optional<AlumnoSchema> alumnoExistente = alumnoRepository.findById(id);
        if (alumnoExistente.isPresent()) {
            AlumnoSchema alumno = alumnoExistente.get();
            alumno.setNombres(alumnoAuxiliar.getNombres());
            alumno.setApellidos(alumnoAuxiliar.getApellidos());
            alumno.setMatricula(alumnoAuxiliar.getMatricula());
            alumno.setPromedio(alumnoAuxiliar.getPromedio());

            AlumnoSchema alumnoActualizado = alumnoRepository.save(alumno);

            return AlumnoDTO.getFromSchema(alumnoActualizado);
        } else {
            return null;
        }
    }

    //Funcion para eliminar un alumno
    public AlumnoDTO deleteAlumno(int id){
        Optional<AlumnoSchema> alumnoAEliminar = alumnoRepository.findById(id);
        if (alumnoAEliminar.isPresent()) {
            alumnoRepository.deleteById(id);
            System.out.println("Alumno con ID " + id + " eliminado correctamente.");
            return AlumnoDTO.getFromSchema(alumnoAEliminar.get());
        } else {
            System.out.println("No se encontró ningún alumno con el ID: " + id);
            return null;
        }
    }

    //Función para crear la sesión del alumno
    public Sesion createSesion(int alumnoId,String info){
        Optional<AlumnoSchema> alumnoOptional = alumnoRepository.findById(alumnoId);
        Sesion sesion = new Sesion();
        if(alumnoOptional.isPresent()) {
            AlumnoSchema alumno = alumnoOptional.get();

            // Compara las contraseñas
            if (info.equals(alumno.getPassword())) {
                String uuid = UUID.randomUUID().toString();
                sesion.setId(uuid);
                sesion.setAlumnoId(alumnoId);  // Asigna el alumno a la sesión si es necesario
                sesion.setActive(Boolean.TRUE);
                sesion.setFecha(System.currentTimeMillis());
                sesion.setSessionString(getRandomString(128));
                sesionRepository.save(sesion);
                return sesion;
            } else {
                // Contraseña incorrecta
                return null;
            }
        } else {
            // No se encontró al alumno con el ID proporcionado
            return null;
        }
    }

    //Función para verificar la sesión
    public Sesion verifySesion(String sessionString){
        Sesion sesionAux = sesionRepository.getSessionBySessionString(sessionString);
        if (sesionAux != null && sesionAux.getActive()) {
            return sesionAux;
        }
        return null;
    }

    //Función para salir de la sesión
    public Sesion logOut(String sessionString){
        Sesion sesionAux = sesionRepository.getSessionBySessionString(sessionString);
        if (sesionAux != null && sesionAux.getActive()) {
            sesionAux.setActive(false);
            sesionRepository.save(sesionAux);
            return sesionAux;
        }
        return null;
    }

    public static String getRandomString(int length) {

        int leftLimit = 97; // letra 'a'
        int rightLimit = 122; // letra 'z'
        Random random = new Random();

        return  random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public AlumnoDTO uploadPhoto(int id, MultipartFile file){
        String filename = file.getOriginalFilename();
        try {
            File tempFile = convertMultiPartToFile(file);
            Optional<AlumnoSchema> alumnoExistente = alumnoRepository.findById(id);
            if (alumnoExistente.isPresent()) {
                AlumnoSchema alumno = alumnoExistente.get();
                alumno.setFotoPerfilUrl("https://s3.amazonaws.com/a17000553/"+filename); //URL del bucket creado en AWS
                AlumnoSchema alumnoActualizado = alumnoRepository.save(alumno);
                bucketRepository.uploadFileToS3( "a17000553", filename,tempFile); //Nombre del Bucket
                return AlumnoDTO.getFromSchema(alumnoActualizado);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public AlumnoDTO sendEmail(int id){
        Optional<AlumnoSchema> alumnoExistente = alumnoRepository.findById(id);
        if (alumnoExistente.isPresent()) {
            AlumnoSchema alumno = alumnoExistente.get();
            snsRepository.sendEmailToTopic("Promedio: "+alumno.getPromedio()+"\nNombres: "+ alumno.getNombres()+"\nApellidos: "+alumno.getApellidos(),"sicei");
            return AlumnoDTO.getFromSchema(alumno);
        }else{
            return null;
        }
    }
}
