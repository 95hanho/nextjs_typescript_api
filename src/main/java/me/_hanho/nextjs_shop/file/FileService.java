package me._hanho.nextjs_shop.file;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

@Service
public class FileService {

	@Value("${spring.servlet.multipart.location}")
    private String uploadDir;
	
	@Autowired
	private FileMapper fileMapper;
	
	
	
	public String getOriginalFile(String id) {
		return fileMapper.getOriginalFile(id);
	}
	

	public String getStoredFile(String id) {
		return fileMapper.getStoredFile(id);
	}

	
	@Transactional
	public void fileUpload(MultipartFile file, String id) {
		// 파일명 설정
		String originalFileName = file.getOriginalFilename();
		String storeFileName = System.currentTimeMillis() + "_" + originalFileName;
		
		String beforeFileName = getStoredFile(id);
		if(beforeFileName != null) {
			deleteFile(beforeFileName);
			fileMapper.fileUpdate(originalFileName, storeFileName, id);
		} else {
			fileMapper.fileInsert(originalFileName, storeFileName, id);
		}
		saveFile(file, storeFileName);
	}
	
	public boolean saveFile(MultipartFile file, String fileName) throws IOException {
		// 파일 저장
		try {
			// 클래스패스에서 "downloads" 폴더 경로 가져오기
//	        ClassPathResource resource = new ClassPathResource("downloads/");
//	        File downloadsDir = resource.getFile();
	        
			String filePath = uploadDir + "/" + fileName;
	        System.out.println("save downloadsDir : " + filePath);

	        // 저장 경로 생성
	        File dest = new File(filePath);
			
			file.transferTo(dest);
		} catch (IllegalStateException | java.io.IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteFile(String fileName) throws IOException {
		// 클래스패스에서 "downloads" 폴더 경로 가져오기
//	        ClassPathResource resource = new ClassPathResource("downloads/");
//	        File downloadsDir = resource.getFile();
		
		String filePath = uploadDir + "/" + fileName;
        System.out.println("delete downloadsDir : " + filePath);
        
        File file = new File(filePath);
        
        // 파일 존재 여부 확인
        if (file.exists()) {
            // 파일 삭제
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("파일 삭제 성공: " + fileName);
            } else {
                System.out.println("파일 삭제 실패: " + fileName);
            }
            return deleted;
        } else {
            System.out.println("파일이 존재하지 않습니다: " + fileName);
            return false;
        }
       
	}


	
}
