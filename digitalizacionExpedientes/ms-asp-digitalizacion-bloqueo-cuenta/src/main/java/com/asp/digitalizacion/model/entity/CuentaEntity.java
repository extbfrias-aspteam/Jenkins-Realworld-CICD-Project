package com.asp.digitalizacion.model.entity;



import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cuenta", catalog = "asp_azul", schema = "public")
public class CuentaEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558524996103127186L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clabe", nullable = false)
	private String  clabe;
	
	private Integer pblu;
	
	private String  bloqueo;
	
	private Boolean activo;
	
	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaModificacion;

	@Column(name = "usuario_actualizacion")
	private String usuarioModificacion;
	
	

}
