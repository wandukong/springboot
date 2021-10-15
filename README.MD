

# Thymeleaf

> HTML, XML, JavaScript, CSS 등의 템플릿을 처리하는 서버측 Java 템플릿 엔진
서블릿으로 변환하지 않고 템플릿을 파싱해서 웹 페이지 생성
- Thymeleaf는 자바 코드를 사용할 수 없음
- Thymeleaf는 서버측에서 해석된다.
- 디자이너가 WAS 없이 HTML을 수정할 수 있기 때문에 편리
- HTML을 파싱해서 데이터 바인딩하여 웹 페이지 생성(서블릿으로 변환하지 않음)
- Thymeleaf가 해석하는 html 파일을 templete이라고 말한다.

## 🖼Thymeleaf 레이아웃
### th:fragment
> fragment file 안에서 세부조각으로 정의(세부 조각별로 외부로 삽입시킬 목적)
```html
<div th:fragment="name"></div>
```
<hr/>

### th:insert="~{/···/fragmentFile[::name]}"
> 적용 태그의 자식 내용으로 fragment를 삽입
- fragment 파일만 있을 경우 fragment의 전체 내용을 삽입
```html
<div th:insert="~/common/파일이름"></div>
<div th:insert="~/common/layout"></div>
```

- ::name이 있을 경우 frament 파일 안의 세부 조각만 삽입
```html
<th:block th:fragment="header">
	...
</th:block>  
```
```html
<div th:insert="~{/common/파일이름::name"></th:block>
<div th:insert="~{/common/layout::header"></th:block>
```
<hr />

### th:replace="~{/···/fragmentFile[::name]}"
> 적용 태그 대신해서 삽입
- **th:block**: html 태그가 필요 없을 경우 사용, thymeleaf 속성만 기술하기 위해 
```html
<th:block th:replace="~{/common/layout}"></th:block>
<th:block th:replace="~{/common/layout::header}"></th:block>
```
<hr />

### 매개 변수화된 th:fragment
#### 레이아웃 파일
```html
<th:block th:fragment="setContent(content)" >
	<th:block th:replace="${content}"/>
</th:block>
```
#### 레이아웃에 넣을 파일
>this::셀렉터
- **content**: fragment 중에서 content name을 가진 파일을 매개변수로 전달

```html
<th:block th:replace="~{common/layout::setContent(~{this::content})}">
	<th:block th:fragment="content">
		<div class="card m2">
			<div class="card-header">Home</div>
			<div class="card-body"></div>
		</div>
	</th:block>
</th:block>
```
- **#content**: id가 content인 fragment 전달
```html
<th:block th:replace="~{common/layout::setContent(~{this::#content})}">
	<div id="content" class="card m2">
		<div class="card-header">Home</div>
		<div class="card-body"></div>
	</div>
</th:block>
```

- **.content**: class가 content인 fragment 전달
## ✒텍스트 삽입
### 속성을 통한 삽입
- **th:text**: 속성값의 태그를 문자 참조로 변환해서 출력
- **th:utext**: 속성값의 태그를 그대로 출력
- **${}** : Variable Expressions으로 모델 속성값 출력
- **#dates** - Thymeleaf에서 제공하는 Utility Objects
```html
<div>bno: <span th:text="${board.bno}"></span></div>
<div>btitle: <span th:text="${board.btitle}"></span></div>
<div>bcontent: <span th:text="${board.bcontent}"></span></div>
<div>bcontent(unescaped): <span th:utext="${board.bcontent}"></span></div>
<div>mid: <span th:text="${board.mid}"></span></div>
<div>bdate: <span th:text="${#dates.format(board.bdate, 'yyyy.MM.dd')}"></span></div>
```
<hr />

### 인라인 삽입
- **[[...]]**: ...에 포함된 태그를 문자 참조로 변환해서 문자로 출력
- **[(...)]**: ...에 포함된 태그를 그대로 출력
```html
<div>bno: [[${board.bno}]]</div>
<div>btitle: <span>[[${board.btitle}]]</span></div>
<div>bcontent: <span>[[${board.bcontent}]]</span></div>
<div>bcontent(unescaped): <span>[(${board.bcontent})]</span></div>
<div>mid: [[${board.mid}]]</div>
<div>bdate: [[${#dates.format(board.bdate, 'yyyy.MM.dd')}]]</div>
```

## 🎲JavaScript 데이터 삽입
>  **[(...)]** 을 이용
- **th:inline:"javascript"** 
	- 삽입되는 데이터가 문자열일 경우 자동으로 큰 따옴표 붙여준다.
	- 자바 배열을 javacript 배열로 자동으로 변환한다.
	- 자바 객체를 javacript 객체로 자동으로 변환한다.
	- 자바에서 작성된 JsonObject는 JSON.parse()를 사용해서 변환해야 한다.
```html
<script th:inline="javascript">
	//문자열
	let name = [[${name}]];
	console.log("name: ", name);
	
	// 배열
	let hobby = [[${hobby}]];
	console.log("hobby: ", hobby);
	
	// 객체
	let board = [[${board}]];
	console.log("board: ", board);
	
	// json객체
	let jsonBoard = JSON.parse([[${jsonBoard}]]);
	console.log("jsonBoard: ", jsonBoard);

	// Backtick 바인딩 기호와 차이
	var name = [[${name}]]; 	// ServerSide에서 데이터 바인딩
	var message = `Your's Name is ${name}`; // ClientSide에서 데이터 바인딩
	console.log("message: " + message);

</script>
```

## 💡표준 표현식 문법

### 표준 표현식 종류
```
Variable Expressions: ${...}
Selection Variable Expressions: *{...}
Message Expressions: #{...}
Link URL Expressions: @{...}
Fragment Expressions: ~{...}
```
<hr />

### 표준 표현식 작성 위치
- 유효성 검사할 때 data-th-text 사용해야한다.
```
th:*="표현식"
data-th-*="표현식"
<tag> … 표현식 …  </tag>
```
<hr/>

### 세션 객체 불러오기 
- session에 저장된 객체를 불러오려면, session. 을 붙여야한다.
```html
<div>Current user : [[${session.sessionMid}]]</div>
<div th:text="'Current user : ' + ${session.sessionMid}"></div>
```
<hr/>

### th:if 
- display 효과
	- 조건을 만족하면 보여주고, 만족하지 않으면 보여주지 않는다.
```html
<div>
	<a th:if="${session.sessionMid == null}" href="variableExpressions" class="btn btn-info btn-sm">로그인</a>
	<a th:if="${session.sessionMid != null}"href="variableExpressions" class="btn btn-info btn-sm">로그아웃</a>
</div>
```
<hr/>

### Variable Expressions
> th:text
```html
<div th:text="${'Current user : '+ session.sessionMid}"></div>
<div th:text="'Current user : ' + ${session.sessionMid}"></div>
<div th:text="|Current user : ${session.sessionMid}|"></div>
<div>Current user : [[${session.sessionMid}]]</div>
```
<hr/>

### Selection Variable Expressions
>  th:object 
- **\*** : 어떤 객체의 속성값
```html
<th:block th:object="${board}">
	<div>bno: [[*{bno}]]</div>
	<div>btitle: [[*{btitle}]]</div>
	<div>bcontent: [[*{bcontent}]]</div>
	<div>bcontent(unescaped): [(*{bcontent})]</div>
	<div>mid: [[*{mid}]]</div>
	<div>bdate: [[*{#dates.format(bdate, 'yyyy.MM.dd')}]]</div>
</th:block>
```
<hr/>

### Message Variable Expressions
```html
<h6>[[#{welcome}]]</h6>
<button class="btn btn-sm btn-info">[[#{btn.join}]]</button>
```
<hr/>

### Link URL Expressions
> @{...}
- contextPath를 자동으로 앞에 붙여 주어, 절대 경로를 만들어 준다.
```html
<link rel="stylesheet" th:href="@{/bootstrap-4.6.0-dist/css/bootstrap.min.css}"/>
<script th:src="@{/jquery/jquery-3.5.1.min.js}"></script>
<a th:href="@{/}" class="btn btn-success btn-sm">로그인</a>
```