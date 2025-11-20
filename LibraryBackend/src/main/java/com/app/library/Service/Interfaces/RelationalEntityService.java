package com.app.library.Service.Interfaces;

import com.app.library.Entity.Author;
import com.app.library.Entity.Publisher;

public interface RelationalEntityService {
    public Author getOrCreateAuthor(String name, String surname);
    public Publisher getOrCreatePublisher(String name);
}
