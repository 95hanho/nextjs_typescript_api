package me._hanho.nextjs_shop.seller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetProductImageRequest {
	private Integer productId;
	private List<AddFileMeta> addFiles;
	private List<UpdateFile> updateFiles;
	private List<Integer> deleteImageIds;
}