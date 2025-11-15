package com.app.library.Service.QueryServices;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import com.app.library.Repository.BookRepository;
import com.app.library.Service.QueryServices.DTO.SortFields;
import com.app.library.Specification.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookQueryServices
{
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;


    @Autowired
    public BookQueryServices(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;


    }
    public Page<BookResponse>FindBooksByCriteria(BookCriteria criteria)
    {
        Pageable pageable = PageRequest.of(criteria.page(),criteria.size());
        Specification<Book>spec = BookSpecification.buildSpecification(criteria);
        if(criteria.sortByField() != null&&criteria.direction()!=null)
        {
            Sort.Direction direction = criteria.direction().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            String sortField = SortFields.findFieldInMap(criteria.sortByField());
            if(sortField!=null) {
                Sort sort = Sort.by(direction, sortField);
                pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
            }
            else {
                throw new IllegalArgumentException("Unknown sort field: " + criteria.sortByField());
            }
            }
        Page<Book> books = bookRepository.findAll(spec, pageable);
        return books.map(bookMapper::ToBookResponse);
    }
}
