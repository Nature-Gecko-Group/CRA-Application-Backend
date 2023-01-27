package com.naturegecko.application.utilities;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naturegecko.application.configuration.MinIoConfiguration;
import com.naturegecko.application.exception.ExceptionFoundation;
import com.naturegecko.application.exception.ExceptionResponseModel.EXCEPTION_CODE;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;

@Service
public class MinIoStorageService {

	private final MinioClient minioClient;
	@SuppressWarnings("unused")
	private final MinIoConfiguration minioConfiguration;

	@Value("${minio.bucketname}")
	private String bucketname;

	@Value("${minio.maximumfilesize}")
	private long maximumFileSize;

	public MinIoStorageService(MinioClient minioClient, MinIoConfiguration minioConfiguration) {
		this.minioClient = minioClient;
		this.minioConfiguration = minioConfiguration;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Ping Bucket
	public boolean pingBucket(String bucketName) {
		try {
			return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODE.MINIO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ MINIO_ERROR ] MinIo bucket is unreachable.");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Get stat object
	public StatObjectResponse getStatobjectFromName(String objectName) {
		StatObjectResponse statObject;
		try {
			statObject = minioClient.statObject(StatObjectArgs.builder().bucket(bucketname).object(objectName).build());
			return statObject;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODE.MINIO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, objectName);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Get image from storage
	public Resource getImageFromStorage(String trackName) {
		getStatobjectFromName(trackName);
		try {
			InputStream stream = minioClient
					.getObject(GetObjectArgs.builder().bucket(bucketname).object(trackName).build());
			return new InputStreamResource(stream);
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODE.MINIO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ MINIO_ERROR ] ");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Upload Image to storage
	public String uploadImage(MultipartFile file) {
		String imamgeFileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

		// Will check for allowed extention here.

		try {
			InputStream fileStream = new BufferedInputStream(file.getInputStream());
			String imageFileName = RandomStringGenerator.generateName(12, "FLAG-COUNTRY-") + imamgeFileExtension;
			minioClient.putObject(PutObjectArgs.builder().bucket(bucketname).object(imageFileName)
					.stream(fileStream, fileStream.available(), -1).contentType(file.getContentType()).build());
			return imageFileName;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODE.INTERNAL_SERVER_ERROR, null,
					"[ INTERNAL_SERVER_ERROR ] File save failed at Minio Upload method.");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Delete image from storage
	public String deleteImageFromStorage(String imageName) {
		getStatobjectFromName(imageName);
		try {
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketname).object(imageName).build());
			return imageName;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODE.MINIO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ MINIO_ERROR ] Can't delete this object.");
		}
	}

}
