<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@include file="../include/header.jsp"%>
		
		<div class="container-fluid">
			<div class="card shadow mb-4">
				<div class="card-header">Register</div>
				<div class="card-body">
					<div class="sbp-preview">
						<div class="sbp-preview-content">	
						  <form role="form" action="/board/register" method="post" enctype="multipart/form-data" name="register">
					           <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				
		<!-- 						 <input type="hidden" name="file_1" value="">
						         <input type="hidden" name="file_2" value="">
						         <input type="hidden" name="file_3" value=""> -->
				
					          <div class="form-group">
					            <label>Title</label> <input class="form-control" name='title'>
					          </div>
					
					          <div class="form-group">
					            <label>Text area</label>
					            <textarea class="form-control" rows="3" name='content'></textarea>
					          </div>
					
					          <div class="form-group">
					            <label>Writer</label> <input class="form-control" name='writer' 
					                value='<sec:authentication property="principal.member.userid"/>' readonly="readonly">
					          </div>
					       
					          
					          <input type="button" value="Submit" class="btn btn-primary" onclick="check_onclick()">
					          <button type="reset" class="btn btn-danger">Reset Button</button>
					      
					        </form>	
					       											
						</div>
					</div>
				</div>
			</div>
		</div>
		
		     <!-- 파일 업로드 부분 -->
        <div class="row">
        	 <div class="col-md-12">
        		<div class="card card-success">
	            	  <div class="card-header">
	              		  <h3 class="card-title">파일 업로드</h3>
	             	 </div>
	             	 <div class="card-body">
	             	 	<!-- <input type="file" name="uploadFile" multiple> -->
	             	 	  <div class="form-group uploadDiv">
		             	 	<label class="input-file-button" for="input-file">
							  업로드
							</label>
							<input type="file"  id="input-file" name="uploadFile" multiple style="display:none"/>
	             	 	  </div>
	             	 	  
	             	 	  <div class="uploadResult">
	             	 	  	<ul>
	             	 	  	
	             	 	  	
	             	 	  	</ul>
	      
	             	 	  </div>
	             	 	
	             	 </div>
             	 </div>
        	
        	 </div>
        </div>



<%@include file="../include/footer.jsp"%> 
<script>
	//form NULL値 チェック
	function check_onclick(){
		var check_form = document.register;
		var formObj = $("form[role='form']");

		if(check_form.title.value == ""){
			alert("제목을 입력해 주세요.");
			return register.title.focus();
		}else if(check_form.content.value == ""){
			alert("컨텐츠를 입력해 주세요.");
			return register.content.focus();	
		}else{

			
			/* $("form").submit(); */
			var str = "";

			$(".uploadResult ul li").each(function(i, obj){

				var jobj = $(obj);

				console.dir(jobj);
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
				
			});

			formObj.append(str).submit();
			alert("생성되었습니다."); 
			//check_form.submit();
		}

	}


	  $(document).ready(function(){

		  var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		  var maxSize = 5242880; // 5MB

			//파일 체크
			function checkExtension(fileName, fileSize){

				if(fileSize >= maxSize){
					alert("파일 사이즈 초과");
					return false;
				}

				if(regex.test(fileName)){
					alert("해당 종류의 파일은 업로드 할 수 없습니다.");
					return false;
				}
				return true;
			
			}
			//파일 체크
			
			//showUploadResult
			function showUploadResult(uploadResultArr){
				
				if(!uploadResultArr || uploadResultArr.length == 0){
					return;
				}

				var uploadUL = $(".uploadResult ul");

				var str = "";

				$(uploadResultArr).each(
					function(i, obj){
						
						if(obj.image){
							var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);

							str += "<li data-path='"+obj.uploadPath+"'";
							str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'>"
							str +="<div>";
							str += "<span> " + obj.fileName + "</span>";
							str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
							str += "<img src='/display?fileName="+fileCallPath+"' width='150px' height='150px'>";
							str += "</div>";
							str += "</li>";
						}else{
							var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
							var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");

							str += "<li data-path='"+obj.uploadPath+"'";
							str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'"
							str +=" ><div>";
							str += "<span> " + obj.fileName + "</span>";
							str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
							str += "<img src='/resources/img/attach.jpg' width='150px' height='150px'></a>";
							str += "</div>"; 
							str += "</li>";
						}

					
					});

					uploadUL.append(str);
			}
			
			//showUploadResult

			$("input[type='file']").change(function(e){

				var header = $("meta[name='_csrf_header']").attr('content');
				var token = $("meta[name='_csrf']").attr('content');
		
				var formData = new FormData();

				var inputFile = $("input[name='uploadFile']");

				var files = inputFile[0].files;

				for(var i = 0; i < files.length; i++){
					
					if(!checkExtension(files[i].name, files[i].size)){
						return false;
					}
					formData.append("uploadFile", files[i]);
				}
				
				$.ajax({
					url : '/uploadAjaxAction',
					processData : false,
					contentType : false,
					data : formData,
					type : 'POST',
					dataType : 'json',
					beforeSend: function(xhr){
	        	 	        xhr.setRequestHeader(header, token);
	        	 	},
					success : function(result){
						console.log(result);
						showUploadResult(result);
					}

				});



			});
	  });
	  
	  $(".uploadResult").on("click", "button", function(e){

			var header = $("meta[name='_csrf_header']").attr('content');
			var token = $("meta[name='_csrf']").attr('content');
			
			console.log("delete file");

			var targetFile = $(this).data("file");
			var type = $(this).data("type");

			var targetLi = $(this).closest("li");

			$.ajax({
				url: '/deleteFile',
				data : {fileName: targetFile,
						type: type
					   },
				dataType : 'text',
				type : 'POST',
				beforeSend: function(xhr){
    	 	        xhr.setRequestHeader(header, token);
    	        },
				success : function(result){
						alert(result);
						targetLi.remove();
				}

			});

	  });

</script>