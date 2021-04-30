<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@include file="../include/header.jsp"%>


		<!-- 詳細読みページ -->
		<div class="container-fluid">
			<div class="card shadow mb-4">
				<div class="card-header">Board Read</div>
				<div class="card-body">
					<div class="sbp-preview">
						<div class="sbp-preview-content">
							  <div class="form-group">
						          <label>Bno</label> <input class="form-control" name='bno'
						            value='<c:out value="${board.bno }"/>' readonly="readonly">
						        </div>
						
						        <div class="form-group">
						          <label>Title</label> <input class="form-control" name='title'
						            value='<c:out value="${board.title }"/>' readonly="readonly">
						        </div>
						
						        <div class="form-group">
						          <label>Text area</label>
						          <textarea class="form-control" rows="3" name='content'
						            readonly="readonly"><c:out value="${board.content}" /></textarea>
						        </div>
						
						        <div class="form-group">
						          <label>Writer</label> <input class="form-control" name='writer'
						            value='<c:out value="${board.writer }"/>' readonly="readonly">
						        </div>  
						        <!-- アップロードimageやファイルを出力-->
						        <c:forEach var="i" begin="1" end="3">
									<c:set var="t" value="file_${i}" />
										<c:if test="${not empty board[t]}">
											<div class="form-group">
									          <label>이미지${i}</label>
												<a href="/resources/upload/${board[t]}" target="_blank">
												<img src="/resources/upload/${board[t]}" id="thumb_${i}"></a>
									        </div>
									        <script>
									        document.getElementById('thumb_${i}').src="/resources/upload/" + getThumbFileName('${board[t]}');
											</script>
								        </c:if>
								</c:forEach>
						      
							<sec:authentication property="principal" var="pinfo"/>
							<sec:authorize access="isAuthenticated()">
								<c:if test="${pinfo.username eq board.writer}">
									<button data-oper='modify' class="btn btn-primary">Modify</button>
								</c:if>
							</sec:authorize>
							<button data-oper='list' class="btn btn-info">List</button>

							<form id='operForm' action="/board/modify" method="get">
							  <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
							  <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>
							  <input type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
							  <input type='hidden' name='keyword' value='<c:out value="${cri.keyword}"/>'>
							  <input type='hidden' name='type' value='<c:out value="${cri.type}"/>'>  
							</form>
								
						</div>
					
					</div>
	
				</div>
				
			</div>
		</div>
		
		<!-- コメント領域-->
		
		<div class='container-fluid'>
			  <div class="card shadow mb-4">	      
			      <div class="card-header">
			        <i class="fa fa-comments fa-fw"></i> Reply
			        <sec:authorize access="isAuthenticated()">
				        <button id='addReplyBtn' class='btn btn-primary btn-xs pull-right'>New Reply</button>
			        </sec:authorize>
			      </div>      
			      <!-- /.panel-heading -->
			      <div class="card-body">        
			      
			      	<!-- コメントリストの出力部分 -->
			        <ul class="chat">
			
			        </ul>
			        <!-- ./ end ul -->
			      </div>
			      <!-- /.panel .chat-panel -->
			
				<div class="panel-footer"></div>
			
			
				
			  </div>
			  <!-- ./ end row -->
			</div>



	<!-- コメント Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
		                <label>Reply</label> 
		                <input class="form-control" name='reply' value='New Reply!!!!'>
					</div>      
					<div class="form-group">
						<label>Replyer</label> 
						<input class="form-control" name='replyer' value='replyer'>
					</div>
					<div class="form-group">
						<label>Reply Date</label> 
						<input class="form-control" name='replyDate' value='2018-01-01 13:13'>
					</div>
				</div>
				
				<div class="modal-footer">
					<button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
					<button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
					<button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
					<button id='modalCloseBtn' type="button" class="btn btn-default">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
        <!-- /.modal-dialog -->
	</div>
	<!-- /댓글 modal -->
      
<script type="text/javascript" src="/resources/js/reply.js"></script>
		
		
<!-- コメント領域 -->

<%@include file="../include/footer.jsp"%>

<!-- 댓글 js -->
<script type="text/javascript">
$(document).ready(function () {

	//掲示物番号
	var bnoValue = '<c:out value="${board.bno}"/>';
	//出力部分
	var replyUL = $(".chat");

	//showListはページ番号をパラメータとして受け、パラメータがなければ開始は1ページ
	showList(1);
	
	// コメントリストを出力する関数
	function showList(page){
		
		// console.log("show list " + page);
	    
		replyService.getList({bno:bnoValue, page: page|| 1 }, function(replyCnt, list) {
	      
		    // console.log("replyCnt: "+ replyCnt );
		    // console.log("list: " + list);
		    // console.log(list);
	    	
			if(page == -1){
				pageNum = Math.ceil(replyCnt/10.0);
				showList(pageNum);
				return;
			}
 			
			var str="";
	     
			if(list == null || list.length == 0){
				replyUL.html("");
				return;
			}
	     	//コメントリストを出力
			for (var i = 0, len = list.length || 0; i < len; i++) {
				str +="<li class='left clearfix' data-rno='"+list[i].rno+"'>";
				str +="  <div><div class='header'><strong class='primary-font'>["
					+ list[i].rno+"] "+list[i].replyer+"</strong>"; 
				str +="    <small class='pull-right text-muted'>"
					+ replyService.displayTime(list[i].replyDate)+"</small></div>";
				str +="    <p>"+list[i].reply+"</p></div></li>";
			}
	     
			replyUL.html(str);
	     
			showReplyPage(replyCnt);

	 
		});//end function
	     
	}//end showList

	// コメントページング処理
	var pageNum = 1;  //スタートページ
    var replyPageFooter = $(".panel-footer"); //ページングボタン部分
    
    function showReplyPage(replyCnt){
      
      var endNum = Math.ceil(pageNum / 10.0) * 10;   // エンドページ
      var startNum = endNum - 9;  // スタートページの10ページまで表示して、それ以上nextに進み、11からスタート
      
      var prev = startNum != 1; //その前のページングリストに戻る
      var next = false; //次ページの基本値false

      //終番号計算
      if(endNum * 10 >= replyCnt){
        endNum = Math.ceil(replyCnt/10.0);
      }
      
      if(endNum * 10 < replyCnt){
        next = true;
      }
      
      var str = "<ul class='pagination pull-right2'>";
      
      if(prev){
        str+= "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>Previous</a></li>";
      }
      
      for(var i = startNum ; i <= endNum; i++){
        
        var active = pageNum == i? "active":"";
        
        str+= "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
      }
      
      if(next){
        str+= "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>Next</a></li>";
      }
      
      str += "</ul></div>";
      
      console.log(str);
      
      replyPageFooter.html(str);
    }
     
    replyPageFooter.on("click","li a", function(e){
       e.preventDefault();
       console.log("page click");
       
       var targetPageNum = $(this).attr("href");
       
       console.log("targetPageNum: " + targetPageNum);
       
       pageNum = targetPageNum;
       
       showList(pageNum);
     });
 	// コメントページング処理終了
 	
    
	/* コメントモジュールの動作部分*/
	var modal = $(".modal");
    var modalInputReply = modal.find("input[name='reply']");
    var modalInputReplyer = modal.find("input[name='replyer']");
    var modalInputReplyDate = modal.find("input[name='replyDate']");
    var modalModBtn = $("#modalModBtn");
    var modalRemoveBtn = $("#modalRemoveBtn");
    var modalRegisterBtn = $("#modalRegisterBtn");

 	//コメント認証部分を追加
	var replyer = null;

    //ログインしている状態および作成者は、現在接続中のユーザー
    <sec:authorize access="isAuthenticated()">
    	var replyer = '<sec:authentication property="principal.username"/>';   
	</sec:authorize>

 	//トークン値リターンajaxセキュリティ
	const csrfHeaderName ="${_csrf.headerName}"; 
	const csrfTokenValue="${_csrf.token}";
    
    $("#modalCloseBtn").on("click", function(e){
    	modal.modal('hide');
    });
    
    $("#addReplyBtn").on("click", function(e){
		modal.find("input").val("");
		modal.find("input[name='replyer']").val(replyer);
		modalInputReplyDate.closest("div").hide();
		modal.find("button[id !='modalCloseBtn']").hide();
		
		modalRegisterBtn.show();
		$(".modal").modal("show");
    });

 	// Ajax Spring Security Header これをしてこそ ajaxにpostを送るときにデータ送信が可能
    $(document).ajaxSend(function(e, xhr, options) { 
		xhr.setRequestHeader(csrfHeaderName, csrfTokenValue); 
	});
	
    // コメント登録
	modalRegisterBtn.on("click",function(e){
      
		var reply = {
			reply: modalInputReply.val(), //コメントの内容。
            replyer:modalInputReplyer.val(), // コメント作成者。
            bno:bnoValue //現在の文番号
		};
      	//add追加
		replyService.add(reply, function(result){
        
        alert(result);
        
        modal.find("input").val("");
        modal.modal("hide");
        
        showList(1);
      });
      
    });
    
	//コメント照会クリックイベント処理
    $(".chat").on("click", "li", function(e){
      
		var rno = $(this).data("rno");
		console.log(rno);
		
		replyService.get(rno, function(reply){
		   //モーダルウィンドウにそれぞれの情報を表示
			modalInputReply.val(reply.reply);
			modalInputReplyer.val(reply.replyer);
			modalInputReplyDate.val(replyService.displayTime( reply.replyDate)).attr("readonly","readonly");
			modal.data("rno", reply.rno);
			
			modal.find("button[id !='modalCloseBtn']").hide();
			modalModBtn.show();
			modalRemoveBtn.show();
			
			$(".modal").modal("show");
		});
	});
	
 	// コメント修正イベント。
	modalModBtn.on("click", function(e){
		
		var originalReplyer = modalInputReplyer.val();
		
		var reply = {
				rno:modal.data("rno"), 
				reply: modalInputReply.val(),
				replyer: originalReplyer
				};
	  	//ログインしなければ不可能 既にログインしていなければ掲示板にアクセス不可能
		if(!replyer){
			alert("로그인후 수정이 가능합니다.");
			modal.modal("hide");
			return;
		}

		console.log("Original Replyer: " + originalReplyer);
		//作成者が異なる場合
		if(replyer  != originalReplyer){
			alert("자신이 작성한 댓글만 수정이 가능합니다.");
			modal.modal("hide");
			return;
		}
		  
		replyService.update(reply, function(result){
			alert(result);
			modal.modal("hide");
			showList(pageNum);
		});
	});

	// コメント削除部分。 
	modalRemoveBtn.on("click", function (e){
	  	  
		var rno = modal.data("rno");

		console.log("RNO: " + rno);
		console.log("REPLYER: " + replyer);
		//ログインしなければ不可能 既にログインしていなければ掲示板にアクセス不可能
		if(!replyer){
			alert("로그인후 삭제가 가능합니다.");
			modal.modal("hide");
			return;
		}
	   	  
		var originalReplyer = modalInputReplyer.val();
	   	  
		console.log("Original Replyer: " + originalReplyer);
		//作成者が異なる場合
		if(replyer !== originalReplyer){
	   		  
			alert("자신이 작성한 댓글만 삭제가 가능합니다.");
			modal.modal("hide");
			return;
		}
	   	  
		replyService.remove(rno, originalReplyer, function(result){
	   	        
			alert(result);
			modal.modal("hide");
			showList(pageNum);
		});
	});
	
	
});

</script>
<!-- コメントjs終わり。 -->
<script type="text/javascript">
$(document).ready(function() {
  var operForm = $("#operForm"); 
  
  $("button[data-oper='modify']").on("click", function(e){
    
    operForm.attr("action","/board/modify").submit();
    
  });
  
    
  $("button[data-oper='list']").on("click", function(e){
    
    operForm.find("#bno").remove();
    operForm.attr("action","/board/list")
    operForm.submit();
    
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