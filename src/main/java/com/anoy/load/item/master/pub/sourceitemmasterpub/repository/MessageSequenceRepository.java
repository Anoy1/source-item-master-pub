package com.anoy.load.item.master.pub.sourceitemmasterpub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.MessageIdEntity;

@Repository
public interface MessageSequenceRepository extends JpaRepository<MessageIdEntity, Integer>{

	@Query(value = "select * from message_sequence ORDER BY MESSAGE_ID DESC LIMIT 1",nativeQuery = true)
	Integer findTopByOrderByIdDesc();

}
