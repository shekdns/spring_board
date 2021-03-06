<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@include file="../include/header.jsp"%>
                <div class="container-fluid">

                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                           	<div class="panel-heading">
								Board List Page
							<button id='regBtn' type="button" class="btn btn-xs pull-right">Register New Board</button>
							</div>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                            	<table class="table table-striped table-bordered table-hover">                               
                                    <thead>
                                        <tr>
                                            	<th>번호</th>
												<th>제목</th>
												<th>작성자</th>
												<th>작성일</th>
												<th>수정일</th>
                                        </tr>
                                    </thead>
                               
                                    <tbody>
                                       <c:forEach items="${list}" var="board">
										<tr>
											<td><c:out value="${board.bno}" /></td>
											<td><a class='move' href='<c:out value="${board.bno}"/>'><c:out value="${board.title}" />
												<b>[<c:out value="${board.replyCnt}" />]</b></a> 
											</td>
											<td><c:out value="${board.writer}" /></td>
											<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}" /></td>
											<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate}" /></td>
										</tr>
										</c:forEach>
	                                        
                                    </tbody>
                                </table>
                                
                                	<!-- 検索領域スタート -->
								<div class='row'>
									<div class="col-lg-12">
				
										<form id='searchForm' action="/board/list" method='get'>
											<select name='type'>
												<option value=""
													<c:out value="${pageMaker.cri.type == null?'selected':''}"/>>--</option>
												<option value="T"
													<c:out value="${pageMaker.cri.type eq 'T'?'selected':''}"/>>제목</option>
												<option value="C"
													<c:out value="${pageMaker.cri.type eq 'C'?'selected':''}"/>>내용</option>
												<option value="W"
													<c:out value="${pageMaker.cri.type eq 'W'?'selected':''}"/>>작성자</option>
												<option value="TC"
													<c:out value="${pageMaker.cri.type eq 'TC'?'selected':''}"/>>제목
													or 내용</option>
												<option value="TW"
													<c:out value="${pageMaker.cri.type eq 'TW'?'selected':''}"/>>제목
													or 작성자</option>
												<option value="TWC"
													<c:out value="${pageMaker.cri.type eq 'TWC'?'selected':''}"/>>제목
													or 내용 or 작성자</option>
											</select>
											<input type='text' name='keyword' value='<c:out value="${pageMaker.cri.keyword}"/>' />
											<input type='hidden' name='pageNum'	value='<c:out value="${pageMaker.cri.pageNum}"/>' />
											<input type='hidden' name='amount' value='<c:out value="${pageMaker.cri.amount}"/>' />
											<button class='btn btn-primary btn-icon-split'>Search</button>
										</form>
									</div>
								</div>
									<!-- 検索領域エンド -->
                                
				               <!--  Paginationスタート-->
								<div class='pull-right'>
									<ul class="pagination">
				
										<c:if test="${pageMaker.prev}">
											<li class="paginate_button page-item previous"><a href="${pageMaker.startPage -1}" class="page-link">Previous</a></li>
										</c:if>
				
										<c:forEach var="num" begin="${pageMaker.startPage}"	end="${pageMaker.endPage}">
											<li class="paginate_button page-item  ${pageMaker.cri.pageNum == num ? "active":""} ">
												<a href="${num}" class="page-link">${num}</a>
											</li>
										</c:forEach>
				
										<c:if test="${pageMaker.next}">
											<li class="paginate_button page-item next"><a href="${pageMaker.endPage +1 }" class="page-link">Next</a></li>
										</c:if>
				
									</ul>
								</div>
								<!--  Paginationエンド -->
				
								<!-- PagingFormスタート-->
								<form id='actionForm' action="/board/list" method='get'>
								<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
								<input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
								<input type='hidden' name='type' value='<c:out value="${ pageMaker.cri.type }"/>'>
								<input type='hidden' name='keyword'	value='<c:out value="${ pageMaker.cri.keyword }"/>'>
								</form>
								<!-- PagingFormエンド -->
				
								<!-- Modal追加 -->
								<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
									aria-labelledby="myModalLabel" aria-hidden="true">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"
													aria-hidden="true"></button>
												<h4 class="modal-title" id="myModalLabel"></h4>
											</div>
											<div class="modal-body">처리가 완료되었습니다.</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">Close</button>
												<button type="button" class="btn btn-primary" data-dismiss="modal">Save
													changes</button>
											</div>
										</div>
									
									</div>
									
								</div>
								<!-- Modal -->
                                
                                
                            </div>
                        </div>
                    </div>

                </div>
 
<%@include file="../include/footer.jsp"%> 
<script>
		
$(document).ready(function() {
	var result = '<c:out value="${result}"/>';
	
	checkModal(result);
	history.replaceState({}, null, null);

	function checkModal(result) {
		// alert(history.state);
		// 文章登録後のリスト画面でmodelウィンドウが表示された後、更新してもmodelウィンドウが表示されないように
		// result値チェック
		// result値はcontrollerから越えてきた1回性の値でページを読み返すとnullになる。
		// history.stateは照会でbackしたときにhistoryオブジェクトの現在の情報を読んで値があれば、
		// modalウィンドウを出さない。
		if (result === '' || history.state) {
			return;
		}

		if (parseInt(result) > 0) {
			$(".modal-body").html("게시글 " + parseInt(result)	+ " 번이 등록되었습니다.");
		}

		$("#myModal").modal("show");
	}
	
	$("#regBtn").on("click", function() {
		self.location = "/board/register";
	});

	
	var actionForm = $("#actionForm");

	// ページ番号クリックイベント
	$(".paginate_button a").on("click", function(e) {
		e.preventDefault();
		actionForm.find("input[name='pageNum']").val($(this).attr("href"));
		actionForm.submit();
	});
	
	// 詳細を見るをクリックするイベント
	$(".move").on("click",function(e) {
		e.preventDefault();
		actionForm.append("<input type='hidden' name='bno' value='" + $(this).attr("href")	+ "'>");
		actionForm.attr("action", "/board/get");
		actionForm.submit();
	});
	
	// 検索ボタンクリックイベント
	var searchForm = $("#searchForm");
	$("#searchForm button").on("click",	function(e) {
		if (!searchForm.find("option:selected").val()) {
			alert("검색종류를 선택하세요");
			return false;
		}

		if (!searchForm.find("input[name='keyword']").val()) {
			alert("키워드를 입력하세요");
			return false;
		}
		
		searchForm.find("input[name='pageNum']").val("1");
		e.preventDefault();
		searchForm.submit();
	});
});




	
</script>