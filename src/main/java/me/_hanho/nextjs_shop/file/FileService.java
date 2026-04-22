package me._hanho.nextjs_shop.file;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.file.dto.FileUploadRequest;
import me._hanho.nextjs_shop.model.FileInfo;

@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${spring.servlet.multipart.location}")
    private String uploadDir;
	
	private final FileMapper fileMapper;

	public FileInfo getStoredFile(String id) {
		return fileMapper.getFile(id);
	}
	
	@Transactional
	public FileUploadRequest fileUploadImage(MultipartFile file) {
		// 파일명 설정
		String fileName = file.getOriginalFilename();
		if (fileName == null || fileName.isBlank()) {
			throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
		}
		String storeFileName = createStoreFileName(fileName);
		String fileExtension = extractExt(fileName).toLowerCase();
		String fileNameWithoutExt = removeExt(originalFileName);
		String filePath = uploadDir + "/" + storeFileName;

		// 이미지 파일인지 검증(파일 확장자 검증)
		if (!fileExtension.matches("jpg|jpeg|png|gif|webp")) {
			throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
		}
		// 파일크기 제한
		if (file.getSize() > 5 * 1024 * 1024) {
			throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED);
		}
		// MIME 타입 체크
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
		}

		// 파일 저장
		saveFile(file, filePath);
		// DB 저장
		FileUploadRequest request = FileUploadRequest.builder()
				.fileName(fileNameWithoutExt)
				.storeName(storeFileName)
				.fileExtension(fileExtension)
				.filePath(filePath)
				.build();

		fileMapper.insertFile(request);

		return request;
	}

	/* ------------------------------------------------------------------------------- */

	// 확장자 추출 함수
	private String extractExt(String originalFileName) {
		int pos = originalFileName.lastIndexOf(".");
		if (pos == -1 || pos == originalFileName.length() - 1) {
			throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
		}
		return originalFileName.substring(pos + 1);
	}
	// 저장 파일이름 만들기
	public String createStoreFileName(String originalFileName) {
		String uuid = UUID.randomUUID().toString();
		String ext = extractExt(originalFileName).toLowerCase();
		String storeFileName = uuid + "." + ext;
		return storeFileName;
	}
	// 파일 저장
	public void saveFile(MultipartFile file, String filePath) {
		try {
			// 클래스패스에서 "downloads" 폴더 경로 가져오기
//	        ClassPathResource resource = new ClassPathResource("downloads/");
//	        File downloadsDir = resource.getFile();
	        
	        System.out.println("save downloadsDir : " + filePath);

	        // 저장 경로 생성
	        File dest = new File(filePath);
			File parent = dest.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			file.transferTo(dest);
		} catch (IllegalStateException | java.io.IOException e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}
	// 파일 삭제
	public boolean deleteFile(String storeName) throws IOException {
		// 클래스패스에서 "downloads" 폴더 경로 가져오기
//	        ClassPathResource resource = new ClassPathResource("downloads/");
//	        File downloadsDir = resource.getFile();
		
		String filePath = uploadDir + "/" + storeName;
        System.out.println("delete downloadsDir : " + filePath);
        
        File file = new File(filePath);
        
        // 파일 존재 여부 확인
        if (file.exists()) {
            // 파일 삭제
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("파일 삭제 성공: " + storeName);
            } else {
                System.out.println("파일 삭제 실패: " + storeName);
            }
            return deleted;
        } else {
            System.out.println("파일이 존재하지 않습니다: " + storeName);
            return false;
        }
       
	}


	
}
