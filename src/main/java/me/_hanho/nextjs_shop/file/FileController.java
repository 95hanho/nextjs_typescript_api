package me._hanho.nextjs_shop.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${spring.servlet.multipart.location}")
    private String uploadDir;
	
	@Autowired
	private FileService fileService;
	
	// 상품이미지 업로드
	@PostMapping("/product")
	public ResponseEntity<Map<String, Object>> productImageUpload(@RequestParam("files") List<MultipartFile> files,
			@RequestParam("beforeFileIdList") List<String> beforeFileIdList, @RequestParam("productId") String productId) {
		logger.info("fileUpload");
		Map<String, Object> result = new HashMap<String, Object>();

		if(beforeFileIdList.size() > 0) {
//			fileService.deleteExistingFile(beforeFileIdList);
		}
		
		files.forEach(f -> fileService.fileUpload(f, "product"));
		
//		fileService.fileUpload(files, "");
		
		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 파일업로드
	@PostMapping
	public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam(value="file") MultipartFile file,
			@RequestAttribute("id") String id) {
		logger.info("fileUpload");
		Map<String, Object> result = new HashMap<String, Object>();

		fileService.fileUpload(file, id);
		
		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
