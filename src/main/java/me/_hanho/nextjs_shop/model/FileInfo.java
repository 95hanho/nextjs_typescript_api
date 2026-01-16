package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private int fileId;
    private String fileName;
    private String storeName;
    private String fileExtension;
    private String filePath;
    private Timestamp createdAt;
    private String copyright;
    private String copyrightUrl;
    private boolean isDeleted;
    private Timestamp deletedAt;
}
