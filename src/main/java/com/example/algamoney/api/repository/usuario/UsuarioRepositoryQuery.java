package com.example.algamoney.api.repository.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryQuery {	

    public Page<Usuario> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable);

}
