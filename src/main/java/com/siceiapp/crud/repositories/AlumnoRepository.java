package com.siceiapp.crud.repositories;

import com.siceiapp.crud.schema.AlumnoSchema;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<AlumnoSchema, Integer>{
    @NotNull
    List<AlumnoSchema> findAll();
    Optional<AlumnoSchema> findById(int id);
}
