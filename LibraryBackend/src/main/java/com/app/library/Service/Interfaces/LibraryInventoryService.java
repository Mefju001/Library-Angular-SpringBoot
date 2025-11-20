package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Response.LibraryBookResponse;

import java.util.List;

public interface LibraryInventoryService {
    LibraryBookResponse Add(LibraryBookRequest request,Integer Stock);
    LibraryBookResponse Update(int id, LibraryBookRequest libraryBookRequest,Integer stock);
    void Delete(Integer id);
    List<LibraryBookResponse> findallbookandlibrary();
    List<LibraryBookResponse> findbookByTitleInLibraries(String title);
}
