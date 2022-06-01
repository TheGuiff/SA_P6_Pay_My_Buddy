package com.paymybuddy.dal.repository;

import com.paymybuddy.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<User, Long> {
}
