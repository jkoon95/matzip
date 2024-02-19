package kr.or.iei.store.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreReview {
	private int reviewNo;
	private String reviewWriter;
	private String reviewPhoto;
	private int reviewStar;
	private String reviewContent;
	private String regDate;
	private int storeNo;
	private int reviewRef;
	
	public String getReviewContentBr() {
		return reviewContent.replaceAll("\r\n", "<br>");
	}
}
