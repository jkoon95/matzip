/**
 * 
 */

//검색한 주소의 정보를 insertAddress 함수로 넘겨준다.
function searchAddressToCoordinate(address) {
    naver.maps.Service.geocode({
        query: address
    }, function(status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
          return null;
        }
        if (response.v2.meta.totalCount === 0) {
          return null;
        }
        const htmlAddresses = [],
            item = response.v2.addresses[0],
            point = new naver.maps.Point(item.x, item.y);
        if (item.roadAddress) {
            htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
        }
        if (item.jibunAddress) {
            htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
        }
        if (item.englishAddress) {
            htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
        }
        insertAddress(item.roadAddress, item.x, item.y);
    });
}

//검색정보를 테이블로 작성해주고, 지도에 마커를 찍어준다.
function insertAddress(address, latitude, longitude) {
  const mapList = "";
  $('#mapList').append(mapList);	

  const map = new naver.maps.Map('map', {
      center: new naver.maps.LatLng(longitude, latitude),
      zoom: 16
  });
    const marker = new naver.maps.Marker({
        map: map,
        position: new naver.maps.LatLng(longitude, latitude),
    });
    // 마커에 정보창 표시
    let contentString = [
    "<div class='iw_inner'>",
    "	<h3>KH정보교육원</h3>",
    "	<p>서울시 영등포구 선유2로 57 이레빌딩 19F 강의장</p>",
    "</div>"
  ].join("");
  
  let infoWindow = new naver.maps.InfoWindow();
  
  // marker에 클릭이벤트 추가
  naver.maps.Event.addListener(marker,"click",function(e){
    // infoWindow객체에 우리가 만들어놓은 태그문자열을 세팅
    infoWindow = new naver.maps.InfoWindow({
      content : contentString
    });
    infoWindow.open(map,marker);
  });
}

//지도를 그려주는 함수
function selectMapList() {
  const map = new naver.maps.Map('map', {
      center: new naver.maps.LatLng(37.3595704, 127.105399),
      zoom: 10,
      zoomControl : true,
    zoomControlOptions : {
      position : naver.maps.Position.TOP_RIGHT,
      style : naver.maps.ZoomControlStyle.SMALL
    }
  });
}

// 지도를 이동하게 해주는 함수
function moveMap(len, lat) {
  const mapOptions = {
        center: new naver.maps.LatLng(len, lat),
        zoom: 16,
        mapTypeControl: true
    };
    const map = new naver.maps.Map('map', mapOptions);
    const marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(len, lat),
        map: map
    });
}