package com.naturegecko.application.exception;

import java.util.Date;
import org.springframework.http.HttpStatusCode;
import lombok.Data;

@Data
public class ExceptionResponseModel {

	public enum EXCEPTION_CODE {
		INTERNAL_SERVER_ERROR(5000), // Server error.
		CONFIGIURATION_ERROR(5001), // Configuration error such as setting wrong password.
		MINIO_ERROR(5002), MINIO_ILLEGAL_EXTENTION(5003),

		USER_DOES_NOT_EXIST(10001), // Can't find the user by any mean.

		REGISTERATION_TAKEN_USERNAME(11001), // This username is taken.
		REGISTERATION_TAKEN_EMAIL(11002), // This email is taken.
		REGISTERATION_INVALID_EMAIL_FORMAT(11003), // Email format is not valid.
		REGISTERATION_INVALID_USERNAME_FORMAT(11004), // Username format is not valid
		REGISTERATION_INVALID_PASSWORD_FORMAT(11005), // Password format not valid.
		REGISTERATION_PASSWORD_MISMATCH(11006), // Password does not match.

		AUTHEN_INCORRECT_CREDENTIALS(12001), // Incorrect username of password.
		AUTHEN_ACCOUNT_SUSPENDED(12002), // This user is banned.

		INVALID_COUNTRY_NAME(13101), //
		INVALID_COUNTRY_PRECIS(13102), //
		INVALID_COUNTRY_DOCUMENT_URL(13103), //
		COUNTRY_DOES_NOT_EXIST(13104), //
		INVALID_COUNTRY_PERMISSION(13105);//

		private final int codeValue;

		private EXCEPTION_CODE(int codeValue) {
			this.codeValue = codeValue;
		}

		public int getCodeValue() {
			return codeValue;
		}
	}

	private int excaptionCode;
	private EXCEPTION_CODE reason;
	private Date timeStamp;
	private String message;
	private String detail;
	private HttpStatusCode httpStatus;

	public ExceptionResponseModel(EXCEPTION_CODE reason, Date timeStamp, String message, String detail,
			HttpStatusCode httpStatus) {
		super();
		this.reason = reason;
		this.excaptionCode = reason.codeValue;
		this.timeStamp = timeStamp;
		this.message = message;
		this.detail = detail;
		this.httpStatus = httpStatus;
	}

}
