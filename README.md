## whale - 음악 스트리밍 & 커뮤니티

음악스트리밍과 음악 중심의 소통을 하는 커뮤니티를 만들고 싶었습니다. 게시판과 피드에서 음악을 태그하여 공유할 수 있습니다. Spotify API로 음악 스트리밍과 음악 정보 데이터를 이용할 수 있습니다. 백엔드는 Java, Spring을 사용하고 프론트엔드는 jsp를 사용합니다. DB는 Oracle을 사용하여 커뮤니티 데이터와 음원이 실행될 때 음원의 택스트 정보들을 자체 DB에 따로 저장합니다.

[프로젝트 목표]
* 웹개발 반복 숙달: 프로젝트를 주로 게시판 형태로 제작하여 웹개발의 기초를 반복 숙달함 
* 팀프로젝트 경험: 팀원의 코드가 서로에게 영향을 주며 팀프로젝트에서 설계 및 소통 경험

[프로젝트 개요]
* 기간: 2024.10.14 ~ 2024.11.21
* 팀원: 백엔드 5명
* 사용기술: Java, Spring, jsp, Oracle, MyBatis
* 담당: 관리자 페이지 (프론트엔드 95%, 백엔드 100%)

## 담당 기능
프로젝트의 유저 이용현황 확인, 신고처리 및 유저, 게시물을 관리할 수 있는 기능 제작.

* 관리자 페이지는 Spring Interceptor를 활용하여 /admin 경로 접근 시 세션에 저장된 유저 권한 정보를 기반으로 접근을 제한하였습니다. 유저가 주소창을 이용해 강제로 접근을 시도할 경우, 오류 페이지로 리다이렉트 되도록 
처리하였습니다.
* 신고처리로 유저 제재 시 별도의 유저 상태 로그, 게시물 삭제 로그가 남습니다. 신고가 접수되지 않더라도 유저를 정지할 수 있고, 게시물을 삭제할 수 있습니다. 이때는 관리자의 이름으로 신고가 자동 등록되어 처리 과정을 일관되게 유지합니다.
* Union을 활용한 쿼리를 통해 분리된 게시판과 피드 게시물을 하나의 통합된 리스트로 조회할 수 있도록 구현하였습니다. 댓글 또한 게시판과 피드를 하나의 통합된 리스트로 확인할 수 있도록 설계하여 데이터 조회의 편의성을 높였습니다.

* 로그인 시 나타나는 whale 메인 페이지
![1번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/whale%EB%A9%94%EC%9D%B8.png)


* 관리자 메인 페이지. 헤드와 사이드 메뉴가 고정되고 흰색 부분의 내용물만 변경되며 화면전환이 이루어짐.
![2번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EA%B4%80%EB%A6%AC%EC%9E%90%20%EB%A9%94%EC%9D%B8.png)


* 계정 목록, 게시판 목록, 신고 목록은 모두 게시판 형태.
![3번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EA%B2%8C%EC%8B%9C%EA%B8%80%EB%AA%A9%EB%A1%9D.png)


* 유저정보 일부를 관리자가 수정할 수 있음. 또한 신고가 없더라도 제재 가능하며, 제재 시 관리자 이름으로 자동 신고처리되어 일관성을 유지시킴.
![4번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EC%9C%A0%EC%A0%80%EC%88%98%EC%A0%95.png)


* 공지사항은 알림탭 공지, 게시판 공지 2가지로 제작. 알림 공지는 사이트 전체 공지를 보낼 목적으로 제작하여 댓글을 다는 느낌으로 제작. 게시판공지는 특정커뮤니티 또는 전체 커뮤니티에 작성가능. 전체 커뮤니티에 작성할 시 커뮤니티 개수 만큼 글이 작성됨. 전체 공지사항 삭제 및 수정시 제목과 작성 날짜가 같은 공지사항은 일괄 처리하여 여러개의 글이 작성되지만 하나의 글을 다루는 것처럼 설계함.(여러개의 글을 작성하는게 아닌 전체 탭 선택시 하나의 게시글로 일괄 적용 되게 하는 방법도 있었지만 게시판 담당자와 상의 후 전체탭 생성시 수정사항이 많이 발생하여 각 커뮤니티에 글을 생성되게 설계함)
![5번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EA%B3%B5%EC%A7%80%EC%95%8C%EB%A6%BC.png)
![6번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EA%B3%B5%EC%A7%80%EC%9E%91%EC%84%B1.png)


* 신고는 게시물을 신고하는 형태로 진행됨. 하나의 게시글에 여러개의 신고가 들어왔을 때 하나의 신고만 처리되도 일괄 적용시켜 반복작업을 줄임.
![7번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%EC%8B%A0%EA%B3%A0%EC%83%81%EC%84%B8.png)


* 웹 사이트 현황 파악을 위해 DB에 생성되는 데이터에는 시간필드를 생성할 것을 팀원들에게 요구하여 활용함.
![8번](https://github.com/hankookin123/other-resources/blob/main/whale-admin-img/%ED%86%B5%EA%B3%84.png)


## 시스템 구성

* Spring MVC의 구조를 기반으로 Controller, Service, DAO 계층을 분리하여 설계하였습니다. MyBatis를 활용해 동적 쿼리를 적용하여, 조건에 따라 쿼리를 유연하게 생성하도록 설계하였습니다.
* 레이아웃 JSP 파일을 사용해 페이지 공통 레이아웃을 구성하였습니다. Controller에서 요청 경로에 따라 모델에 JSP 경로를 전달하고, 뷰에서 jsp:include 태그를 사용해 페이지 전환을 처리하였습니다.


## 데이터 구축

* 팀원이 생성한 게시물, 유저, 신고 등의 테이블에서 관리자가 처리한 내용을 기록하는 형식으로 테이블을 추가하였습니다.
* 유저 제재 시, 신고가 없는 경우에도 자동으로 신고 기록을 생성하여 처리 과정을 일관되게 유지하도록 설계하였습니다.
* 중복신고, 전체 커뮤니티 공지글과 같이 생성된 글은 여러개지만 처리는 같은 데이터에 대해서는 일괄처리하게 설계하여 불필요한 중복작업을 줄였습니다.

### 관리자 페이지 추가 테이블
* ADMIN_MEMO, FEED_DEL_LOG, FEED_DEL_LOG, POST_DEL_LOG, USER_STATUS_LOG, NOTICE_TABLE, REPORT_RESULT. 테이블 명을 보면 대부분 삭제나 처리내용을 기록하는 용도의 테이블이 추가 되었습니다.


## 회고

* ### 협업이 만든 더 나은 결과

 관리자 페이지 이동 방법은 로그인 시 세션에 저장된 유저 정보에 따라 관리자 페이지 이동 버튼을 활성, 비활성 하는 것으로 정했습니다. 페이지 구현 이후 팀원이 로그인 없이 주소를 이용해서 강제로 관리자 페이지에 진입하는 문제를 발견하였습니다. 문제를 해결하기 위해 Controller에 /admin 경로를 매핑하고, 이 경로를 Spring Interceptor를 활용하여 유저 정보에 따라 접근을 제한시켜 잘못된 접근을 차단할 수 있었습니다. 이 문제는 팀원이 점검하지 않았다면 발견하지 못했을 수 있었습니다. 이 경험을 통해 협업의 중요성을 다시 한번 깨달았고, 함께 검토하고 개선하는 과정이 프로젝트의 완성도를 높이는 데 얼마나 중요한지 알게 되었습니다.


* ### 협업이 만든 더 나은 결과

신고로 유저를 정지했을 때, 정지 기간이 지나면 자동으로 해제되도록 구현하는 방법을 고민하였습니다. Spring의 @Scheduled 애노테이션을 사용해 서버에서 관리하는 방법과 DB 스케줄러를 활용하는 방법을 비교한 결과, 서버에서 상태 정보를 다루는 것은 잘못된 방향이라는 판단으로 DB 중심 설계를 선택하였습니다. 신고 처리 테이블에 정지 종료 날짜 필드를 추가하고, 1시간마다 해당 필드를 체크하는 DB 스케줄러로 정지를 자동 해제하도록 구현했습니다. 이 과정에서 서버와 DB 간 역할 분담을 명확히 하며 안정성과 확장성을 확보할 수 있었습니다. 다양한 구현 방법을 비교하고 최적의 해결책을 찾아가는 경험은 시스템 설계에 대한 이해를 한층 더 높이는 계기가 되었습니다.
