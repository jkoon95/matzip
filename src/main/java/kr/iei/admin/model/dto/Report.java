package kr.iei.admin.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report {
	private int report_no;
	private int member_no;
	private String report_reason;
	private String report_target;
	private int report_type;
	
	private String member_id;
	private int store_no;
	private String store_name;
}
