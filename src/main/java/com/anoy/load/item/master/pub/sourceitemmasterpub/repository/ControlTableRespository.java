package com.anoy.load.item.master.pub.sourceitemmasterpub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;

@Repository
public interface ControlTableRespository extends JpaRepository<ControlEntity, String>{

}
