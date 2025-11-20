package com.app.library.Specification;

import com.app.library.DTO.Request.BookCriteria;
import com.app.library.Entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BookSpecification {
    public static Specification<Book> buildSpecification(BookCriteria criteria) {
        Specification<Book> specification = Specification.where(null);
        if(StringUtils.hasText(criteria.Title())){
            specification = specification.and(titleContains(criteria.Title()));
        }
        if(StringUtils.hasText(criteria.genre_name())){
            specification = specification.and(genreNameContains(criteria.genre_name()));
        }
        if(StringUtils.hasText(criteria.publisher_name())){
            specification = specification.and(publisherNameContains(criteria.publisher_name()));
        }
        return specification;
    }
    private static Specification<Book>titleContains(String title) {
       return (root, criteriaQuery, criteriaBuilder) ->
               criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%"+title.toLowerCase()+"%");
    }
    private static Specification<Book>genreNameContains(String genreName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("genre").get("name"),"%"+genreName+"%");
    }
    private static Specification<Book>publisherNameContains(String publisherName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("publisher").get("name"),"%"+publisherName+"%");
    }

}
