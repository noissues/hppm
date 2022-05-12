package com.hppm.service;

import java.io.IOException;

import com.hppm.model.dto.NotationDTO;
import com.hppm.model.dto.PageDTO;

/**
 * @see ;
 */
public interface NotationService {
    
    void addNotation(NotationDTO notationDTO) throws IOException;
    
    void deleteNotation(Long notationId) throws IOException;
    
    void updateNotation(NotationDTO notationDTO) throws IOException;
    
    PageDTO<NotationDTO> queryNotationListPage(String notationCategory, String searchParam, int page, int size);

    NotationDTO getNotationDetail(long id);
}
