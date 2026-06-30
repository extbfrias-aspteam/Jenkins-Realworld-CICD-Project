package functions.dto;

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
public record PubSubStorageDto(String bucket,String contentType,String crc32c,String etag,String generation,String id,String kind,String md5Hash,String mediaLink,String metageneration,String name,String selfLink,String size,String storageClass,String timeCreated,String timeStorageClassUpdated,String updated){}