<h2>👉🏻 Diagrams<h2/>
<img width="461" alt="image" src="https://github.com/jiinse0/spring_Lv3/assets/130745679/6cbf63ee-e32f-46c2-b566-241b5b5ed6f7">

<br>
<br>

<h2>👉🏻 Postman<h2/>
https://documenter.getpostman.com/view/27971774/2s946e9taT

<br>
<br>

<h2>👉🏻 API 명세<h2/>

<img width="822" alt="스크린샷 2023-07-13 235316" src="https://github.com/jiinse0/spring_Lv3/assets/130745679/7086fdea-25cb-43d2-b9bb-3584f042e24e">

<img width="821" alt="스크린샷 2023-07-13 235327" src="https://github.com/jiinse0/spring_Lv3/assets/130745679/5b06cdd1-131b-4cde-8080-011e28ee186b">

<img width="821" alt="스크린샷 2023-07-13 235336" src="https://github.com/jiinse0/spring_Lv3/assets/130745679/6a9b4904-6467-4b4d-bd6b-c46be1e77046">

<br>
<br>

<h2>👉🏻 추가 요구사항<h2/>
  
1. 회원 가입 API
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자`로 구성되어야 한다.
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글, 댓글 수정 / 삭제 가능

2. 댓글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
    - 선택한 게시글의 DB 저장 유무를 확인하기
    - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
3. 댓글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
4. 댓글 삭제 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
5. 예외 처리
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기
