package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;

import java.util.List;

public interface LibraryService {
    List<LibraryResponse> findall();
    LibraryResponse findbyid(Integer id);
    Library findByAddress(String id);
    List<LibraryResponse> findlibrarybylocation(String location);
    LibraryResponse addlibrary(LibraryRequest library);
    LibraryResponse updatelibrary(Integer id, LibraryRequest library);
    void deletelibrary(Integer id);
}
