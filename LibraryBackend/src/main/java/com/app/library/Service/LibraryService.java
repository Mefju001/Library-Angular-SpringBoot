package com.app.library.Service;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;

import java.util.List;

public interface LibraryService {
    List<LibraryResponse> findall();

    LibraryResponse findbyid(Integer id);

    List<LibraryResponse> findlibrarybylocation(String location);

    List<LibraryBookResponse> findallbookandlibrary();

    List<LibraryBookResponse> findbookinlibraries(String title);

    LibraryResponse addlibrary(LibraryRequest library);

    LibraryBookResponse addbooktolibrary(LibraryBookRequest request);

    LibraryResponse updatelibrary(Integer id, LibraryRequest library);

    LibraryBookResponse updatebookandlibrary(LibraryBookRequest libraryBookRequest);

    void deletelibrary(Integer id);

    void deletebookandlibrary(Integer id);
}
