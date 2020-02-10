package com.example.algamoney.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;

@Service
public interface UsuarioRepository extends JpaRepository<Usuario,Long >{

	public Optional<Usuario> findByEmail(String email);
}
