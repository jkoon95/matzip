

const storeNo = 2; //원래는 매장상세보기에서 mode.addattribute로 [[${store.storeNo}]]를 넘겨줌


$.ajax({
  url: "/reserve/closedDays",
  type: "post",
  data: {storeNo:storeNo}, //매개변수로 건내주는 data는 반드시 객체{key : value} 형태로!!!
  dataType : "json",
  success: function (data1) {

    $.ajax({
      url: "/reserve/tempClosedDays",
      type: "post",
      data: {storeNo:storeNo},
      dataType : "json",
      success: function (data2){

        //비활성화할거 배열(순서때매 이거 먼저 선언해야함)
        var closedDays = []; //정기휴무일(요일인데 숫자로)
        var tempClosedDays = []; //임시휴무일(날짜)
        for(let i=0; i<data1.length; i++){
          closedDays.push(data1[i]);
        };
        for(let i=0; i<data2.length; i++){
          tempClosedDays.push(data2[i]);
        };

        $('#datepicker').datepicker({
          //데이트 포맷
          dateFormat: "yy-mm-dd",
          //한글로 표시
          preveText: "이전 달",
          nextText: "다음 달",
          monthNames: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
          dayNames: ["일","월","화","수","목","금","토"],
          dayNamesShort: ["일","월","화","수","목","금","토"],
          dayNamesMin: ["일","월","화","수","목","금","토"],
          //선택 가능 기간 : 최소 오늘부터, 최대 2주 후까지
          minDate: "0",
          maxDate: "+2W",
          //특정일 선택 비활성화하기
          beforeShowDay: disableSomeDays,
          //날짜 선택시 작동할 함수(input태그가 아니라 div태그에 datepicker 넣었기 때문에)
          onSelect: select
        });

        function disableSomeDays(date){
          var month = date.getMonth();
          var dates = date.getDate();
          var year = date.getFullYear();
          var day = date.getDay();

          //정기휴무일(요일)
          for(let i=0; i<closedDays.length; i++){
            if (day == closedDays[i]){
              return [false];
            }
          }

          //임시휴무일(날짜)
          //배열(disabledDays)의 값과 일치하는 날짜는 false를 리턴
          for (let i = 0; i < tempClosedDays.length; i++) {
            if(month < 9){
              if($.inArray(year + '-0' + (month+1) + '-' + dates, tempClosedDays) != -1){
                return [false];
              }
            } else{
              if($.inArray(year + '-' + (month+1) + '-' + dates, tempClosedDays) != -1){
                return [false];
              }
            }
          }
          return [true];
        };

        //날짜 선택시 작동할 함수
        function select(dateText){
          $("#reserveDate").val(dateText);
        };


      },
      error: function(){

      }
    });

  },
  error : function(){
    console.log("에러");
  }
})
