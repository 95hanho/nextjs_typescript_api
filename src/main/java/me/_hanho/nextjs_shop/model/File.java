package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private int file_id;
    private String file_name;
    private String store_name;
    private String file_extension;
    private String file_path;
    private Date created_at;
    private String copyright;
    private String copyright_url;
}
