package me._hanho.nextjs_shop.file.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRequest {
    private int fileId; // 파일 ID (업로드 후 반환되는 값)
    private String fileName;
    private String storeName;
    private String fileExtension;
    private String filePath;
    // private String copyright;
    // private String copyrightUrl;
}
