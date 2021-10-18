# Spring Boot
> 스프링 프레임워크 기반 프로젝트를 어려운 설정이나, WAS에 대한 설정 없이 바로 개발에 들어갈 수 있도록 만든 프레임워크

## 🛸구조
### src/main/resources/static
> 정적 파일들을 담는 패키지
- legacy의 resources 폴더안의 파일들은 boot에서 static 폴더에 넣는다.
<hr/>

### src/main/resources/templates
> template 파일들을 담는 패킺
- legacy의 view 폴더안의 파일들은 boot에서 templates 폴더에 넣는다.
- jsp는 class 파일로 변환되기 때문에, templates에 넣을 수 없다.
<hr/>

###  application.properties 
> 스프링 설정파일
- legacy에서 spring 혹은 dispatch 폴더에 작성했던 스프링 설정파일들은 boot에서 application.properties 에 작성한다.


## 🛰프로젝트 생성
<img src="https://user-images.githubusercontent.com/47289479/137704604-9695d8fa-3a13-4b45-819c-6acfd060293f.png" width=400>

### packaging
#### jar 
- jar는 최종 산출물 안에 was가 포함되어 있다.
	- 실행하면 바로 웹서버로 사용할 수 있다.
-  jsp를 사용할 수가 없다.
	- jsp를 사용하려면 최종 산출물이 war이어야한다.
	- jar는 클래스만 있으면 된다
<hr/> 

#### war
war는 클래스+web application 구조를 가지고 있어야한다.

## 🚀Add starters
<img src="https://user-images.githubusercontent.com/47289479/137704609-1679ac7c-8bcf-49e0-b49f-5964613c8c6c.png" width=500>