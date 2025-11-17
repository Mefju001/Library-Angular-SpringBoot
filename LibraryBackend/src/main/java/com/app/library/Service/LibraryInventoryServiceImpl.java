package com.app.library.Service;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.Service.Interfaces.LibraryInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryInventoryServiceImpl implements LibraryInventoryService {
    @Override
    public LibraryBookResponse Add(LibraryBookRequest request) {
        return null;
    }

    @Override
    public LibraryBookResponse Update(int id, LibraryBookRequest libraryBookRequest) {
        return null;
    }

    @Override
    public List<LibraryBookResponse> findallbookandlibrary() {
        return List.of();
    }

    @Override
    public List<LibraryBookResponse> findbookinlibraries(String title) {
        return List.of();
    }
}
