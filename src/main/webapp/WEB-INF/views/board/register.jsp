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
						<!--まだファイルアップロード機能が追加されていませんが、基本的にファイル1、2、3に非表示値を越えて文章登録を可能に設定  -->	
						  <form role="form" action="/board/register" method="post" enctype="multipart/form-data">
					           <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				
								 <input type="hidden" name="file_1" value="">
						         <input type="hidden" name="file_2" value="">
						         <input type="hidden" name="file_3" value="">
				
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
					          
					           <div class="form-group">
						            <label>이미지 등록 1</label> <input type="file" class="form-control" name='uploadFile'>
						        </div>
						          
						        <div class="form-group">
						            <label>이미지 등록 2</label> <input type="file" class="form-control" name='uploadFile'>
						        </div>
						          
						        <div class="form-group">
						            <label>이미지 등록 3</label> <input type="file" class="form-control" name='uploadFile'>
						        </div>
					          
					          
					          <button type="submit" class="btn btn-default">Submit
					            Button</button>
					          <button type="reset" class="btn btn-default">Reset Button</button>
					        </form>
												
						</div>
					</div>
				</div>
			</div>
		</div>






<%@include file="../include/footer.jsp"%> 