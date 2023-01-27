package com.naturegecko.application.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.naturegecko.application.documents.UserAccountsDocument;

@Repository
public interface UserAccountsRepository extends MongoRepository<UserAccountsDocument, String> {

	@Query("{userEmail:'?0'}")
	Optional<UserAccountsDocument> findByEmail(String email);

	@Query("{username:'?0'}")
	@Collation("{ locale: 'en_US', strength: 2 }")
	Optional<UserAccountsDocument> findByUsername(String username);

	public long count();
}