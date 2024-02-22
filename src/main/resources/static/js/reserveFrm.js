//비활성화할 날짜들 배열(순서때매 이거 먼저 선언해야함)
//var closedDays = [db에서 closedDay 가져오기. 일(=0) ~ 토(6)];
var disabledDays = ["2024-02-23", "2024-02-25"];

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
  beforeShowDay: disableSomeday,
  //날짜 선택시 작동할 함수(input태그가 아니라 div태그에 datepicker 넣었기 때문에)
  onSelect: select
});

//날짜를 나타내기 전에(beforeShowDay) 실행할 함수
function disableSomeday(date){
  //var day = date.getDay();
  //for(let i=0; i<closedDays.length; i++){
    //return [day != closedDays.get(i)];
  //}
  var month = date.getMonth();
  var dates = date.getDate();
  var year = date.getFullYear();
  //배열(disabledDays)의 값과 일치하는 날짜는 false를 리턴
  for (let i = 0; i < disabledDays.length; i++) {
    if(month < 9){
      if($.inArray(year + '-0' + (month+1) + '-' + dates, disabledDays) != -1){
        return [false];
      }
    } else{
      if($.inArray(year + '-' + (month+1) + '-' + dates, disabledDays) != -1){
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