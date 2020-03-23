package com.example.algamoney.api.repository;

import java.util.Optional;

import com.example.algamoney.api.repository.filter.UsuarioFilter;
import com.example.algamoney.api.repository.usuario.UsuarioRepositoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;

@Service
public interface UsuarioRepository extends JpaRepository<Usuario,Long >, UsuarioRepositoryQuery {

	public Optional<Usuario> findByEmail(String email);

    Page<Usuario> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable);
}
