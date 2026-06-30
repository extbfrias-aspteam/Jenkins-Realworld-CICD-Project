package functions.dto;

import java.util.List;

/**
 * Banco ASP Project: eiyu Class: CustomerSummary.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Jan 3, 2025 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Jan 3, 2025 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public record CustomerSummary(String responseCode, String responseMessage, String fullName, String personType,
		String personTypeDesc, String promotorNumber, String branchNumber, String registrationDate,
		String customerStatus, String customerStatusDesc, String maritalStatusDesc, String genre, String dateBirth,
		String nationality, String phoneNumber, String email, String rfc, String curp, List<SavingAccount> savingAccountsList) {
}
