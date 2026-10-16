package com.asp.eiyu.api.admdocument.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO <T>  {
	private String eiyuResponseCode;
	private String descripcion;
	private T content;
	
}
