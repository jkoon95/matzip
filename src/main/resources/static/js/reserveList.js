$("#yes-cancel").on("click",function(){
  const cancel = $(this);
	$.ajax({
		url: "/reserve/cancelReserve",
		type: "post",
		data: {reserveNo : afterRvmList.reserveNo},
		dataType : "json",
		success: function (result){
      if(result>0){
        cancel.parent().parent().parent().css("box-shadow","0 0 0 3px #f04c4c");
        setTimeout(function(){
          location.replace("/reserve/reserveList");
        }, 2000);
      }else{
        const div = $("<div>");
        div.text("취소 실패")
        cancel.parent().append(div);
      }
		},
		error: function(){
      const div = $("<div>");
      div.text("취소 실패")
      cancel.parent().append(div);
		}
	})
})