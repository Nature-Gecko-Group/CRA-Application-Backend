package com.naturegecko.application.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.naturegecko.application.documents.CountryDocument;

@Repository
public interface CountryRepository extends MongoRepository<CountryDocument, String> {

	@Query("{name:'?0'}")
	Optional<CountryDocument> findByName(String name);

}
