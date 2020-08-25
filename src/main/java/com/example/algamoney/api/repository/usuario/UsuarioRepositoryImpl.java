package com.example.algamoney.api.repository.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.model.Usuario_;
import com.example.algamoney.api.repository.filter.UsuarioFilter;

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
            predicates.add(builder.equal(root.get(Usuario_.codigo),usuarioFilter.getCodigo()));
        }

        if(usuarioFilter.getNome() != null){
            predicates.add(builder.like(builder.lower(root.get(Usuario_.nome)),usuarioFilter.getNome().toLowerCase() + "%"));
        }

        if(usuarioFilter.getEmail() != null){
            predicates.add(builder.like(builder.lower(root.get(Usuario_.email)), usuarioFilter.getEmail().toLowerCase() + "%"));
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

