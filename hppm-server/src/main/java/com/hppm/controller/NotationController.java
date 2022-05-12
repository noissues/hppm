package com.hppm.controller;

import com.hppm.model.dto.NotationDTO;
import com.hppm.model.dto.PageDTO;
import com.hppm.service.StorageService;
import com.hppm.service.NotationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class NotationController {

    @Autowired
    NotationService notationService;
    
    @Autowired
    StorageService storageService;

    @PostMapping("/notation")
    @ApiOperation("Add a notation")
    public ResponseEntity<Map<String, Object>> addNotation(@RequestBody NotationDTO notation) {
        Map<String, Object> response = new HashMap<>();
        try {
            notationService.addNotation(notation);
            response.put("message", "Success!");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Save notation error", e);
            String message = "Error occurred while moving file " + "[" + e.getMessage() + "]";
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @DeleteMapping("/notation/{id}")
    @ApiOperation("Delete a notation")
    public ResponseEntity<Map<String, Object>> deleteNotation(@PathVariable("id") long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            notationService.deleteNotation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            log.error("Delete notation error", e);
            String message = "Error occurred while deleting file " + "[" + e.getMessage() + "]";
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
    

    @PutMapping("/notation")
    @ApiOperation("Update a notation")
    public ResponseEntity<Map<String, Object>> updateNotation(@RequestBody NotationDTO notation) {
        Map<String, Object> response = new HashMap<>();
        try {
            notationService.updateNotation(notation);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            log.error("Update notation error", e);
            String message = "Error occurred while updating file " + "[" + e.getMessage() + "]";
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * 分页查询曲谱
     * @param notationCategory 曲谱分类
     * @param searchParam 模糊搜索参数
     * @param page 第几页
     * @param size 每页记录数
     * @return PageDTO<NotationDTO>
     */
    @GetMapping("/notations")
    @ApiOperation("Query notations by page")
    public ResponseEntity<PageDTO<NotationDTO>> queryNotationListPage(
        @RequestParam(required = false) String notationCategory,
        @RequestParam(required = false) String searchParam,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {

        PageDTO<NotationDTO> pageDTO = notationService.queryNotationListPage(notationCategory, searchParam, page, size);

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }


    @GetMapping("/notation/{id}")
    public ResponseEntity<Map<String, Object>> getNotationDetail(@PathVariable("id") long id) {

        NotationDTO notation = notationService.getNotationDetail(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("notation", notation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
