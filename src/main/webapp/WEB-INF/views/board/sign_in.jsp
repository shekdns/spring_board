<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>회원가입</title>

    <!-- Custom fonts for this template-->
    <link href="/resources/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="/resources/css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body class="bg-gradient-primary">

    <div class="container">

        <div class="card o-hidden border-0 shadow-lg my-5">
            <div class="card-body p-0">
                <!-- Nested Row within Card Body -->
                <div class="row">
                    <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
                    <div class="col-lg-7">
                        <div class="p-5">
                            <div class="text-center">
                                <h1 class="h4 text-gray-900 mb-4">Create an Account!</h1>
                            </div>
                            <form class="user" method='post' action="/board/sign_in">
                           		 <input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}" />
                                <div class="form-group row">
                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                        <input type="text" class="form-control form-control-user" id="id"
                                            placeholder="id" name="userid">
                           				<div class="check_font" id="id_check"></div>
                                    </div>
                          
                                    <div class="col-sm-6">
                                        <input type="text" class="form-control form-control-user" id="name"
                                            placeholder="nickname" name="userName">
                                        <div class="check_font" id="name_check"></div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                        <input type="password" class="form-control form-control-user"
                                            id="pass" placeholder="Password" name="userpw">
                                         <div class=check_font id="pw1_check"></div>
                                    </div>
                                    <div class="col-sm-6">
                                        <input type="password" class="form-control form-control-user"
                                            id="pass2" placeholder="Repeat Password">
                                          <div class=check_font id="pw2_check"></div>
                                    </div>
                                </div>
                                        <div class="form-group">
                                    <input type="email" class="form-control form-control-user" id="email"
                                        placeholder="Email Address" name="email">
                                          <div class=check_font id="email_check"></div>
                                </div>
                                
                                
                                <button type="submit" class="btn btn-primary btn-user btn-block" id="register_btn"
									disabled>Register Account</button>
                               
                                <hr>
                                <a href="index.html" class="btn btn-google btn-user btn-block">
                                    <i class="fab fa-google fa-fw"></i> Register with Google
                                </a>
                                <a href="index.html" class="btn btn-facebook btn-user btn-block">
                                    <i class="fab fa-facebook-f fa-fw"></i> Register with Facebook
                                </a>
                            </form>
                            <hr>
                            <div class="text-center">
                                <a class="small" href="forgot-password.html">Forgot Password?</a>
                            </div>
                            <div class="text-center">
                                <a class="small" href="sign_up">Already have an account? Login!</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="/resources/vendor/jquery/jquery.min.js"></script>
    <script src="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="/resources/vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="/resources/js/sb-admin-2.min.js"></script>
    
    <script>
	

	//ID正規式小文字と数字の4~12桁
	var idJ = /^[a-z0-9]{4,12}$/;
	//パスワード正規式数字or英文4~12桁
	var pwJ = /^[A-Za-z0-9]{4,12}$/; 
	//名前 正規式 ハングルまたは英文
	var nameJ = /^[가-힣a-zA-Z]+$/;
	//Eメール検査正規式有効な正規式
	var mailJ = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

	//ID有効性検査(1=重複0!=重複)
	var id_isFalse = 0;
	var name_isFalse = 0;
	var email_isFalse = 0;
	var pass_isFalse = 0;


	$("#id").blur(function() {
		var user_id = $('#id').val();
		
		if(user_id == ""){
			
			$('#id_check').text('아이디를 입력해주세요 :)');
			$('#id_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);	
			id_isFalse = 0;
		}else{
			
		$.ajax({
			url : '${pageContext.request.contextPath}/board/sign_in/idCheck?id='+user_id,
			type : 'get',
			success : function(data) {
				console.log("1 = 중복o / 0 = 중복x : " + data);							
				console.log("아이디 : " + user_id);	
				console.log("id_false : " + id_isFalse);		
				if (data == 1) {
					// 1:IDが重複する文句
					
					$("#id_check").text("사용중인 아이디입니다 :p");
					$("#id_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					id_isFalse = 0;		
				} else if(!idJ.test(user_id)){
				   //上の値が 0
					$("#id_check").text("아이디는 소문자와 숫자 4~12 자리만 가능 합니다 :p");
					$("#id_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					id_isFalse = 0;		

				} else {
					$("#id_check").text("사용가능한 아이디입니다 :p");
					$("#id_check").css("color", "green");
					id_isFalse = 1;
					signUp();
				}
			}, error : function() {
						console.log("실패");
				}
			});
		}
		

	});

	//名前有効性
	$("#name").blur(function() {
			
		var name = $('#name').val();
	
		if(name == ""){
			$('#name_check').text('이름을 입력해주세요 :)');
			$('#name_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);	
			name_isFalse = 0;
		}else{
		isFalse = 0;
		$.ajax({
			url : '${pageContext.request.contextPath}/board/sign_in/nameCheck?name='+name,
			type : 'get',
			success : function(data) {
				console.log("1 = 중복o / 0 = 중복x : " + data);
				console.log("이름 : " + name);									
				console.log("name_false : " + name_isFalse);
				if (data == 1) {
					//1:名前の重複する文句
					$("#name_check").text("사용중인 이름입니다 :p");
					$("#name_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					name_isFalse = 0;
				}else if(!nameJ.test(name)){
				   //上の値が 0 
					$("#name_check").text("이름은 한글 또는 영문으로 해주세요 :p");
					$("#name_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					name_isFalse = 0;		
				} 
				else {
					$("#name_check").text("사용가능한 이름입니다 :p");
					$("#name_check").css("color", "green");
					name_isFalse = 1;
					signUp();
				}
			}, error : function() {
						console.log("실패");
				}
			});
		}

	});

	//Eメール有効性
	$("#email").blur(function() {
		var email = $('#email').val();
		
		if(email == ""){
			
			$('#email_check').text('이메일을 입력해주세요 :)');
			$('#email_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);	
			email_isFalse = 0;
		}else{
			
		$.ajax({
			url : '${pageContext.request.contextPath}/board/sign_in/emailCheck?email='+email,
			type : 'get',
			success : function(data) {
				console.log("1 = 중복o / 0 = 중복x : " + data);							
				console.log("이메일 : " + email);
				console.log("email_false : " + email_isFalse);				
				if (data == 1) {
					//1:Eメール重複する文句
					
					$("#email_check").text("사용중인 이메일입니다 :p");
					$("#email_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					email_isFalse = 0;
				}else if(!mailJ.test(email)){
				   //上の値が 0 
					$("#email_check").text("유효한 이메일을 입력해주세요 :p");
					$("#email_check").css("color", "red");
					$("#register_btn").attr("disabled", true);
					email_isFalse = 0;		

				}  
				else {
					
					$("#email_check").text("사용가능한 이메일입니다 :p");
					$("#email_check").css("color", "green");
				
					email_isFalse = 1;
					signUp();
				}
			}, error : function() {
						console.log("실패");
				}
			});
		}
	

	});
	//パスワード有効性検査
	$("#pass").blur(function(){
		var pass = $('#pass').val();
		
		if(pass == ""){
		
			$('#pw1_check').text('비밀번호를 입력해주세요 :)');
			$('#pw1_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);
		
		}else if(!pwJ.test(pass)){
				$("#pw1_check").text("숫자 or 문자로만 4~12 자리 입력 ");
				$("#pw1_check").css("color", "red");
				$("#register_btn").attr("disabled", true);

		}else{

				$("#pw1_check").text("사용가능한 비밀번호 입니다 :p");
			    $("#pw1_check").css("color", "green");
				signUp();

		}
		
	});
	//パスワード再確認有効性検査
	$("#pass2").blur(function(){
		var pass2 = $('#pass2').val();
		var pass_check = $('#pass').val();
		
		if(pass_check == ""){
			$('#pw2_check').text('비밀번호 입력란에 먼저 입력해주세요');
			$('#pw2_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);
		}else if(pass2 != pass_check){
			$('#pw2_check').text('비밀번호가 다릅니다.');
			$('#pw2_check').css('color', 'red');
			$("#register_btn").attr("disabled", true);
		}else{
			$("#pw2_check").text("재확인 되었습니다.");
		    $("#pw2_check").css("color", "green");
			signUp();
		}
		

	});

	//入力欄最終確認function
	function signUp(){
		var check_id = $('#id_check').html();
		var check_pwd = $('#pw1_check').html();
		var check_pwd2 = $('#pw2_check').html();
   		var check_name = $('#name_check').html();
   		var check_email = $('#email_check').html();
   		
		if(check_id == '사용가능한 아이디입니다 :p' && check_pwd == '사용가능한 비밀번호 입니다 :p'
		&& check_name == '사용가능한 이름입니다 :p' && check_email == '사용가능한 이메일입니다 :p' && check_pwd2 == '재확인 되었습니다.'){
			$("#register_btn").attr("disabled", false);
		}

	}

	//登録ボタン
	$("#register_btn").on("click", function(e){
		 alert("회원가입이 정상적으로 처리 되었습니다.");
		 $("form").submit();
	});

	
	</script>

</body>

</html>