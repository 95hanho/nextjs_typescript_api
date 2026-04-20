package me._hanho.nextjs_shop.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetReviewImageRequest {
	private Integer reviewId;
	private List<AddReviewFileMeta> addFiles;
	private List<UpdateReviewFile> updateFiles;
	private List<Integer> deleteImageIds;
}