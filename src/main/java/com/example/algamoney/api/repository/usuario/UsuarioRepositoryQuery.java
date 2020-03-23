package com.example.algamoney.api.repository.usuario;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.filter.UsuarioFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

public interface UsuarioRepositoryQuery {

    public Page<Usuario> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable);

}
