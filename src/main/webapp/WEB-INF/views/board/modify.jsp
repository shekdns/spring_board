<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@include file="../include/header.jsp"%>
		
		<div class="container-fluid">
			<div class="card shadow mb-4">
				<div class="card-header">Modify</div>
				<div class="card-body">
					<div class="sbp-preview">
						<div class="sbp-preview-content">
							      <form role="form" action="/board/modify" method="post" enctype="multipart/form-data">
				     				
				     				<!--
				     					3週目
										まだファイルアップロード機能を追加していませんが、基本的にファイル1、2、3にhidden値を越えて修正可能に設定
										もしFileに値があれば、元のデータをhidden値に送信
								     	修正または削除する場合、新しいfile データをhidden値に送信
								     	
								     	5週目追加
								        controllerでパラメータを収集する時、upload fileはupload File名でserverに移る
								       	しかし、BoardVOではfile_1、file_2、file_3の名前でsetterをしなければならない。
								       	そのため、file_1、file_2、file_3をhiddenに渡してcontrollerでfileがuploadできなかった場合でも、
								       	パラメータ収集ができるよう(値はnullになる)ためにhiddenに値を渡す。
								     	
								    -->
					     			<c:forEach var="i" begin="1" end="3">
										<c:set var="t" value="file_${i}" />
										<c:choose>
											<c:when test="${not empty board[t]}">
									            <input type="hidden" name="file_${i}" value="${board[t]}">
									        </c:when>
									        <c:otherwise>
												<input type="hidden" name="file_${i}" value="">		
									        </c:otherwise>
										</c:choose>
									</c:forEach>
				     				   
							        <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
							        <input type='hidden' name='amount' value='<c:out value="${cri.amount }"/>'>
								    <input type='hidden' name='type' value='<c:out value="${cri.type }"/>'>
									<input type='hidden' name='keyword' value='<c:out value="${cri.keyword }"/>'>
									<input type='hidden' name='bno' value='<c:out value="${board.bno }"/>'>
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							 
							
							<div class="form-group">
							  <label>Title</label> 
							  <input class="form-control" name='title' 
							    value='<c:out value="${board.title }"/>' >
							</div>
							
							<div class="form-group">
							  <label>Text area</label>
							  <textarea class="form-control" rows="3" name='content' ><c:out value="${board.content}"/></textarea>
							</div>
							
							<div class="form-group">
							  <label>Writer</label> 
							  <input class="form-control" name='writer'
							    value='<c:out value="${board.writer}"/>' readonly="readonly">            
							</div>
							
							<c:forEach var="i" begin="1" end="3">
								<c:set var="t" value="file_${i}" />
								<div class="form-group">
								<label>이미지${i}</label>
									<c:if test="${not empty board[t]}">
										<a href="/resources/upload/${board[t]}" target="_blank"><img src="/resources/upload/${board[t]}" id="thumb_${i}"></a>
										<script>
								        	document.getElementById('thumb_${i}').src="/resources/upload/" + getThumbFileName('${board[t]}');
										</script>
									</c:if>
									<input type="file" class="form-control" name='uploadFile'>
								</div>
							</c:forEach>
													
							    <sec:authentication property="principal" var="pinfo"/>
								<sec:authorize access="isAuthenticated()">
									<c:if test="${pinfo.username eq board.writer}">
										<button type="submit" data-oper='modify' class="btn btn-primary">Modify</button>
										<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
									</c:if>
								</sec:authorize>
							  	<button type="submit" data-oper='list' class="btn btn-info">List</button>
							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

<%@include file="../include/footer.jsp"%>
<script type="text/javascript">
$(document).ready(function() {


	  var formObj = $("form");

	  $('button').on("click", function(e){
	    
	    e.preventDefault(); 
	    
	    var operation = $(this).data("oper");
	    
	    console.log(operation);
	    //削除 
	    if(operation === 'remove'){
	      formObj.attr("action", "/board/remove");
	      
	    }else if(operation === 'list'){
			//リストページ回り
	      formObj.attr("action", "/board/list").attr("method","get");

	      var pageNumTag = $("input[name='pageNum']").clone();
	      var amountTag = $("input[name='amount']").clone();
	      var keywordTag = $("input[name='keyword']").clone();
	      var typeTag = $("input[name='type']").clone();      
	      
	      formObj.empty();
	      
	      formObj.append(pageNumTag);
	      formObj.append(amountTag);
	      formObj.append(keywordTag);
	      formObj.append(typeTag);
	    }
	    //修正
	    formObj.submit();
	  });

});

//サムネイルファイル名を取得する関数
function getThumbFileName(fullFilePath) {
	var arrString = fullFilePath.split("/");
	console.log(arrString);
	arrString.splice(-1, 1, "s_" + arrString[arrString.length - 1]);
	return arrString.join("/");
}

</script>