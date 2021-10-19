# Spring Security
> Spring에서 어플리케이션의 인증과 권한 부여 및 보안 기능 등을 담당하는 프레임워크이다.

-   **Authentication**  : 인증, 자신이 누구라고 주장하는 사람을 확인하는 절차
-   **Authorizatiom**  : 인가, 권한 부여, 특정한 일을 하려고 할 때 그것을 허용하는 행위.

## ➕Add Starters
<img src="https://user-images.githubusercontent.com/47289479/137822574-ab891b91-bdbe-4d7c-a179-217d972a4567.png" width=500>

## 🔓Security 설정을 위한 Class 생성

- 추상 클래스 WebSecurityConfigurerAdaper를 상속 받아서 구현한다
- 어노테이션 @EnableWebSecurity을 붙인다.
```java
@EnableWebSecurity
public class webSecurityConfig extends WebSecurityConfigurerAdapter{
	...
}
```
- 3가지의 configure 메소드를 오버라이딩 한다.
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	// 로그인 설정
	// 로그아웃 설정
	// URL 권한 설정
	// CSRF 설정
}

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// 데이터베이스에서 가져올 사용자 정보 설정
	// 패스워드 인코딩 방법 설정
}

@Override
public void configure(WebSecurity web) throws Exception {
	// 권한 계층 관계 설정
}
```

## 1️⃣configure(HttpSecurity http)

```java
@Override
protected void configure(HttpSecurity http) throws Exception {

	// 로그인 설정
	http.formLogin().loginPage("/security/loginForm") 	// default: /login(GET)
			.usernameParameter("mid") 					// default: username
			.passwordParameter("mpassword") 			// default: password
			.loginProcessingUrl("/login") 				// default: /login(POST) - form POST
			.defaultSuccessUrl("/security/content")		// default: /
			.failureUrl("/security/loginError"); 		// default: /login?error

	// 로그아웃 설정
	http.logout()
		.logoutUrl("/logout")	 						// default: /logout
		.logoutSuccessUrl("/security/content");			// default: /
	
	// URL 권한 설정
	http.authorizeRequests()
		.antMatchers("/security/admin/**").hasAuthority("ROLE_ADMIN")
		.antMatchers("/security/manager/**").hasAuthority("ROLE_ADMIN")
		.antMatchers("/security/user/**").authenticated()
		.antMatchers("/**").permitAll();
	
	// 403(권한x) 에러가 발생할 경우 이동할 경로 설정
	http.exceptionHandling().accessDeniedPage("/security/accessDenied");
	
	// CSRF 설정
	http.csrf().disable();
}
```




## 2️⃣configure(AuthenticationManagerBuilder auth)
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// 데이터베이스에서 가져올 사용자 정보 설정
	// 패스워드 인코딩 방법 설정
	auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT mid, mpassword, menabled FROM member WHERE mid=?")
			.authoritiesByUsernameQuery("SELECT mid, mrole FROM member WHERE mid=?")
			.passwordEncoder(passwordEncoder); // default : DelegatingPasswordEncoder 
}
```
<hr/>

### 패스워드 인코더 관리 빈으로 생성
> 회원가입을 할 때등 여러 곳에서 패스워드 인코더를 사용하기 때문에  관리 빈으로 등록해야 한다.
```java
@Resource
private PasswordEncoder passwordEncoder;

@Bean // @Bean : 메소드가 return 하는 객체를 관리 객체로 만들어준다.
public PasswordEncoder passwordEncoder() {
	// password column 저장 형식 : {암호화알고리즘}암호화된패스워드
	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();  
		
	// bcrypt 알고리즘 사용 	// password column 저장 형식 : 암호화된패스워드
	// PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 	
	
	return passwordEncoder;
}
```
## 3️⃣configure(WebSecurity web)
```java
@Override
public void configure(WebSecurity web) throws Exception {
	log.info("configure(WebSecurity web) 실행");
	// 권한 계층 관계 설정
	DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
	handler.setRoleHierarchy(roleHierarchyImpl);
	web.expressionHandler(handler);
	
	// 인증이 필요 없는 경로 설정
	web.ignoring()
		.antMatchers("/bootstrap-4.6.0-dist/**")
		.antMatchers("/css/**")
		.antMatchers("/favicon.ico")
		.antMatchers("/images/**")
		.antMatchers("/jquery/**");
}
```
<hr />

### 권한 계층 관리 빈으로 생성
> HttpSecurity에서 관리 계층을 참조하기 위해 관리 빈으로 등록해야 한다.
```java
@Resource
private RoleHierarchyImpl roleHierarchyImpl;

@Bean
public RoleHierarchyImpl roleHierarchyImpl() {
	RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
	roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
	return roleHierarchyImpl;
}
```


## 🥂Thymeleafe 와 Spring Security 연동 

> [thymeleaf-extras-springsecurit](https://github.com/thymeleaf/thymeleaf-extras-springsecurity)
> [Spring Security 권한 관련 표현식](https://docs.spring.io/spring-security/site/docs/5.3.4.RELEASE/reference/html5/#el-access)

### dependency 추가
```xml
<dependency>
	<groupId>org.thymeleaf.extras</groupId>
	<artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```


### 네임스페이스 선언
```html
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```

### 인증 정보 조회
```html
<div sec:authorize="!isAuthenticated()">  				// 로그인이 되어 있지 않은 경우
<div sec:authorize="hasRole('ROLE_ADMIN')"> 			// 관리자인 경우
<div>User: [[${#authentication.name}]]</div> 			// user id 출력
<div>Roles: [[${#authentication.authorities}]]</div>	// user role 출력
```

## 🤴Custom 인증 관리자

### User Class 상속한 클래스 생성
```java
public class CustomUserDetails extends User{
	private String memail; // 추가적으로 불러올 데이터

	public CustomUserDetails(
			String username, 
			String password, 
			Boolean enabled, 
			Collection<? extends GrantedAuthority> authorities, 
			String memail) {
		super(username, password, enabled, true, true, true, authorities);
		this.memail = memail;
	}
	

	public String getMemail() {
		return memail;
	}
}
```

### UserDetails 객체를 생성하는 Service 생성
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Resource
	private MemberDao memberDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberDao.selectByMid(username);
		if(member==null){
			throw new UsernameNotFoundException(username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(member.getMrole()));
		CustomUserDetails userDetails = new CustomUserDetails(
				member.getMid(), 
				member.getMpassword(), 
				member.isMenabled(), 
				authorities, 
				member.getMemail()
		);
		
		return userDetails;
	}
}
```

### configure(AuthenticationManagerBuilder auth)에 적용 
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	provider.setUserDetailsService(customUserDetailsService);
	provider.setPasswordEncoder(passwordEncoder);
	auth.authenticationProvider(provider);
}
```

### 인증 정보 조회
```html
<div>User: [[${#authentication.name}]]</div>
<div>User Email: [[${#authentication.principal.memail}]]</div>
<div>User Role: [[${#authentication.authorities}]]</div>
```