package topico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroJSONDto.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Oct 13, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Oct 13, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PubSubStorageDto {

	private String bucket;
	private String contentType;
	private String crc32c;
	private String etag;
	private String generation;
	private String id;
	private String kind;
	private String md5Hash;
	private String mediaLink;
	private String metageneration;
	private String name;
	private String selfLink;
	private String size;
	private String storageClass;
	private String timeCreated;
	private String timeStorageClassUpdated;
	private String updated;

}
