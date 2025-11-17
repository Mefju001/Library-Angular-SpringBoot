package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Response.LibraryBookResponse;

import java.util.List;

public interface LibraryInventoryService {
    LibraryBookResponse Add(LibraryBookRequest request);
    LibraryBookResponse Update(int id, LibraryBookRequest libraryBookRequest);
    List<LibraryBookResponse> findallbookandlibrary();
    List<LibraryBookResponse> findbookinlibraries(String title);
}
