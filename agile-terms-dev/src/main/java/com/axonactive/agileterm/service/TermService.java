package com.axonactive.agileterm.service;

import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermName;
import com.axonactive.agileterm.rest.model.ResponseForUploadFile;
import com.axonactive.agileterm.rest.model.TermDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TermService {


    TermDto save(Term term);

    TermDto update(Integer id, TermName termNameRequest);

    TermDto findTermByTermId(Integer id);

    List<TermDto> getAllQuery();

    Boolean isDescriptionRequestListNotEmpty(Term term);

    ResponseForUploadFile uploadTermAndDescriptionExcelFile(MultipartFile multipartFile) throws IOException;

    List<String> get5TermNameLike(String searchInput);

    Boolean isExistedTermNameInDatabase ( String searchInput);

    void validateNewTermName (String termName);

    List<TermDto> find10RecentTerms();

    List<TermDto> getTop10PopularTerms();

}
