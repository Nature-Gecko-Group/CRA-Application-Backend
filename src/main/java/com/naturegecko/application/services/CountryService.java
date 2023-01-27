package com.naturegecko.application.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.naturegecko.application.documents.CountryDocument;
import com.naturegecko.application.documents.UserAccountsDocument;
import com.naturegecko.application.documents.sub.StatusEnum.COUNTRY_STATUS;
import com.naturegecko.application.exception.ExceptionFoundation;
import com.naturegecko.application.exception.ExceptionResponseModel.EXCEPTION_CODE;
import com.naturegecko.application.forms.CountryForm;
import com.naturegecko.application.repositories.CountryRepository;
import com.naturegecko.application.utilities.JWTokenService;
import com.naturegecko.application.utilities.MinIoStorageService;
import com.naturegecko.application.utilities.StringValidationService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CountryService {

	@Autowired
	private JWTokenService tokenService;
	@Autowired
	private MinIoStorageService minIoStorageService;

	@Autowired
	private CountryRepository countryRepository;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Create new country
	public CountryDocument createNewCountry(CountryForm form, HttpServletRequest request) {

		// Get user detail from token.
		UserAccountsDocument user = tokenService.getUserAccountFromRequest(request);
		CountryDocument newCountry = new CountryDocument();

		// Validate and set country name.
		if (StringValidationService.validateCountryName(form.getName())) {
			newCountry.setName(form.getName());
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_NAME, HttpStatus.I_AM_A_TEAPOT,
					"[ INVALID_COUNTRY_NAME ] The country name must contain at least 6 characters and not more than 70 characters. The name must be in English alphabet.");
		}

		// Validate and set country precis
		if (StringValidationService.validateCountryPrecis(form.getPrecis())) {
			newCountry.setPrecis(form.getPrecis());
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_PRECIS, HttpStatus.I_AM_A_TEAPOT,
					"[ INVALID_COUNTRY_PRECIS ] Precis can only be in English and have 3 latters.");
		}

		// Validate the document URL.
		if (StringValidationService.validateCountryDocument(form.getDocumentURL())) {
			newCountry.setDocumentURL(form.getDocumentURL());
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_DOCUMENT_URL, HttpStatus.I_AM_A_TEAPOT,
					"[ INVALID_COUNTRY_DOCUMENT_URL ] Please use Google document to create the detailed document for your country.");
		}

		newCountry.setAuthorities(form.getAuthorities());
		newCountry.setPrimaryColor(form.getPrimaryColor());
		newCountry.setSecondaryColor(form.getPrimaryColor());
		newCountry.setDescription(form.getDescription());

		newCountry.setStatus(COUNTRY_STATUS.PENDING);
		newCountry.setCountryFlag("/country/flag/default-flag.jpg");

		newCountry.setOwnedBy(user.getId());
		newCountry = countryRepository.save(newCountry);

		return newCountry;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Get available country status
	public List<String> getAvailableStatus() {
		return Stream.of(COUNTRY_STATUS.values()).map(COUNTRY_STATUS::name).collect(Collectors.toList());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Edit country information
	public CountryDocument editCountry(CountryForm form, HttpServletRequest request) {
		UserAccountsDocument user = tokenService.getUserAccountFromRequest(request);

		CountryDocument country = countryRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODE.COUNTRY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND,
						"[ COUNTRY_DOES_NOT_EXIST ] This country does not exist in our databsae."));

		// Validate if this is the owner or has admin authority.
		if (user.getId().equals(country.getOwnedBy()) || user.getRoles().toString().contains("STAFF")) {
			throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_PERMISSION, HttpStatus.UNAUTHORIZED,
					"[ INVALID_COUNTRY_PERMISSION ] Only the owner of this country can edit this country.");
		}

		// Validate and set country name.
		if (form.getName() != null && form.getName() != "") {
			if (StringValidationService.validateCountryName(form.getName())) {
				country.setName(form.getName());
			} else {
				throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_NAME, HttpStatus.I_AM_A_TEAPOT,
						"[ INVALID_COUNTRY_NAME ] The country name must contain at least 6 characters and not more than 70 characters. The name must be in English alphabet.");
			}
		}

		// Validate and set country precis
		if (form.getName() != null && form.getPrecis() != "") {
			if (StringValidationService.validateCountryPrecis(form.getPrecis())) {
				country.setPrecis(form.getPrecis());
			} else {
				throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_PRECIS, HttpStatus.I_AM_A_TEAPOT,
						"[ INVALID_COUNTRY_PRECIS ] Precis can only be in English and have 3 latters.");
			}
		}

		// Validate the document URL.
		if (form.getDocumentURL() != null && form.getDocumentURL() != "") {
			if (StringValidationService.validateCountryDocument(form.getDocumentURL())) {
				country.setDocumentURL(form.getDocumentURL());
			} else {
				throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_DOCUMENT_URL, HttpStatus.I_AM_A_TEAPOT,
						"[ INVALID_COUNTRY_DOCUMENT_URL ] Please use Google document to create the detailed document for your country.");
			}
		}
		country.setAuthorities(form.getAuthorities() == null ? country.getAuthorities() : form.getAuthorities());
		country.setPrimaryColor(
				form.getPrimaryColor() == null || form.getPrimaryColor() == "" ? country.getPrimaryColor()
						: form.getPrimaryColor());
		country.setSecondaryColor(
				form.getPrimaryColor() == null || form.getPrimaryColor() == "" ? country.getPrimaryColor()
						: form.getPrimaryColor());
		country.setDescription(form.getDescription() == null || form.getDescription() == "" ? country.getDescription()
				: form.getDescription());

		country = countryRepository.save(country);
		return country;
	}

	// Delete country

	// Update country flag.
	public void updateFlag(MultipartFile file, String id, HttpServletRequest request) {
		UserAccountsDocument user = tokenService.getUserAccountFromRequest(request);

		CountryDocument country = countryRepository.findById(id)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODE.COUNTRY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND,
						"[ COUNTRY_DOES_NOT_EXIST ] This country does not exist in our databsae."));

		// Validate if this is the owner or has admin authority.
		if (user.getId().equals(country.getOwnedBy()) || user.getRoles().toString().contains("STAFF")) {
			throw new ExceptionFoundation(EXCEPTION_CODE.INVALID_COUNTRY_PERMISSION, HttpStatus.UNAUTHORIZED,
					"[ INVALID_COUNTRY_PERMISSION ] Only the owner of this country can edit a flag for this country.");
		}
		
		String imageName = minIoStorageService.uploadImage(file);

	}

}
