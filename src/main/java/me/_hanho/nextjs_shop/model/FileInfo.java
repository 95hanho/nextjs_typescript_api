package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private int file_id;
    private String file_name;
    private String store_name;
    private String file_extension;
    private String file_path;
    private Timestamp created_at;
    private String copyright;
    private String copyright_url;
}
