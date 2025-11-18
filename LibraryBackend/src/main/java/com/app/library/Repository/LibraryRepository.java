package com.app.library.Repository;

import com.app.library.Entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {
    Library findLibraryByLocationContainingIgnoreCaseAndAddressContainsIgnoreCase(String location, String address);
    Library findLibraryByAddressContainingIgnoreCase(String address);
    List<Library> findLibraryByLocation(String location);
}
