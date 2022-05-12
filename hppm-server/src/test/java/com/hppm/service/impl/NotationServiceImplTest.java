// package com.hppm.service.impl;
//
// import java.io.IOException;
// import java.util.Arrays;
//
// import com.github.pagehelper.PageInfo;
// import com.hppm.model.dto.NotationDTO;
// import com.hppm.service.StorageService;
// import com.hppm.service.NotationService;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;
//
// @RunWith(SpringRunner.class)
// @SpringBootTest
// public class NotationServiceImplTest {
//
//     @Autowired
//     StorageService storageService;
//    
//     @Value("${hppm.file.tempFile}")
//     private String tempFilePath;
//    
//     @Autowired
//     NotationService notationService;
//    
//     // @Test
//     // public void saveNotation() throws IOException {
//     //     // 模拟 Upload
//     //     NotationDTO notationDTO = NotationDTO.builder()
//     //         .notationName("测试曲谱")
//     //         .author("测试作者")
//     //         .notationCategoryCode("G")
//     //         .playCategoryCode("TC")
//     //         .songName("测试歌曲")
//     //         .artist("测试艺人")
//     //         .notationUrls(
//     //             Arrays.asList(
//     //                 "http://127.0.0.1:8081/file/e583349a-dd71-4949-9d7e-8af7e817dd27.png",
//     //                 "http://127.0.0.1:8081/file/ae3a664c-c647-4ee1-acda-8ad26cef7aa3.png")
//     //         ).build();
//     //    
//     //     notationService.saveNotation(notationDTO);
//     // }
//
//     // @Test
//     // public void queryNotationListPage() {
//     //     PageInfo<NotationDTO> notationDTOPageInfo = notationService.queryNotationListPage(null, "测试曲谱", 1, 5);
//     //     System.out.println(Arrays.toString(notationDTOPageInfo.getList().toArray()));
//     // }
//
//     // @Test
//     // public void getNotationDetail() {
//     //     NotationDTO notationDetail = notationService.getNotationDetail(2);
//     //     System.out.println(notationDetail.toString());
//     // }
// }