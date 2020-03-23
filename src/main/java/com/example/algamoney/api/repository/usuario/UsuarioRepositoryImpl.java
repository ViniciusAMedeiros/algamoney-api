package com.example.algamoney.api.repository.usuario;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Usuario> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        //Criar as restrições
        Predicate[] predicates =  criarRestricoes(usuarioFilter, builder,  root);
        criteria.where(predicates);

        TypedQuery<Usuario> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
    }

    private Predicate[] criarRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder builder,
                                        Root<Usuario> root) {

        List<Predicate> predicates = new ArrayList<>();

        if(usuarioFilter.getCodigo() != null){
            predicates.add(builder.equal(root.get("codigo"),usuarioFilter.getCodigo()));
        }

        if(!usuarioFilter.getNome().isEmpty()){
            predicates.add(builder.like(root.get("nome"),usuarioFilter.getNome() + '%'));
        }

        if(!usuarioFilter.getEmail().isEmpty()){
            predicates.add(builder.like(root.get("email"), usuarioFilter.getEmail() + '%'));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRestritrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRestritrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRestritrosPorPagina);

    }

    private Long total(UsuarioFilter usuarioFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

