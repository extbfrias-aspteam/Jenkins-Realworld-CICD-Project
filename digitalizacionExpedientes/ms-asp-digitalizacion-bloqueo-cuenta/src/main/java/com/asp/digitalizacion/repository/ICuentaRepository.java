package com.asp.digitalizacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asp.digitalizacion.model.entity.CuentaEntity;

public interface ICuentaRepository extends JpaRepository<CuentaEntity, String> {
	
	Optional<CuentaEntity> findByClabeAndActivo(String clabe, Boolean activo);
	
	Optional<CuentaEntity> findByClabeAndPbluAndActivo(String clabe, Integer pblu, Boolean activo);

}
