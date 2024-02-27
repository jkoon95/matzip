$("#reserve-fluid").on("click",function(){
	$.ajax({
		url: "/reserve/cancelReserve",
		type: "post",
		data: {reserveNo : afterRvmList.reserveNo},
		dataType : "json",
		success: function (data){

		},
		error: function(){

		}
	})
})