package com.quick.beauty.quickbeautyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quick.beauty.quickbeautyapi.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

}
