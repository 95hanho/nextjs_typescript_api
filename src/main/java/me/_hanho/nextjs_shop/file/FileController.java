package me._hanho.nextjs_shop.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	// 저장된 파일이름 가져오기
	@GetMapping("/myfile")
	public ResponseEntity<Map<String, Object>> getMyThing(@RequestAttribute("id") String id) {
		logger.info("getMyThing");
		Map<String, Object> result = new HashMap<String, Object>();

		String fileName = fileService.getOriginalFile(id);
		
		result.put("fileName", fileName);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 파일업로드
	@PostMapping
	public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam(value="file") MultipartFile file,
			@RequestAttribute("id") String id) {
		logger.info("fileUpload");
		Map<String, Object> result = new HashMap<String, Object>();

		fileService.fileUpload(file, id);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 파일다운로드
	@CrossOrigin(exposedHeaders = "Content-Disposition")
	@GetMapping
	public ResponseEntity<byte[]> fileDownload(@RequestAttribute("id") String id) throws IOException {
		logger.info("fileDownload");
		
		String storedFileName = fileService.getStoredFile(id);
		String originalFileName = fileService.getOriginalFile(id);
		
		System.out.println("Download fileName : " + originalFileName);
		
		// 파일을 클래스패스에서 로드합니다. (여기서는 resoures 디렉토리에 있는 파일)
		String filePath = uploadDir + "/" + storedFileName;
		File file = new File(filePath);
		
//		ClassPathResource resource = new ClassPathResource("downloads/" + storedFileName);
		
		byte[] down = null;
		
		try(InputStream inputStream = new FileInputStream(file)) {
			down = inputStream.readAllBytes();
		}
		
		String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString())
                .replace("+", "%20");
		
		return ResponseEntity
				.ok()
//				.contentType(MediaType.IMAGE_JPEG)
				.contentType(MediaType.IMAGE_JPEG)
//				.header("Content-Disposition", "attachment; filename=" + resource.getFilename())
				.header("Content-Disposition", "attachment; filename=" + encodedFileName)
				.body(down);
	}
}
