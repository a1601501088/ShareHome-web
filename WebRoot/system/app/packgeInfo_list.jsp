<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tag/pages.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/system/head.jsp" %>
	<script type="text/javascript" src="<c:url value='/js/MzTreeView10.js'/>"></script>
	<script type="text/javascript">
	
	//添加
  	function showInfoAdd(){
	    $("#tipInfo6").html("");
	    $("#tipInfo7").html("");
		$("#tipInfo8").html("");
 		$("#tipInfo9").html("");
 		$("#tipInfo10").html("");
  		$.dialog({  			
  			id: "forAddInfo",
  			title: "添加",
  			content: document.getElementById("forAddInfo"),
  			ok: function(){  
  					$("#tipInfo6").html("");
				    $("#tipInfo7").html("");
					$("#tipInfo8").html("");
			 		$("#tipInfo9").html("");
			 		$("#tipInfo10").html("");	  			 	
	  				var spName = $("#AspName").val();
					var spPhone = $("#AspPhone").val();
	  				var spConnect = $("#AspConnect").val();
					var status = $("#Astatus").val();
	  				var iptvSpId = $("#AiptvSpId").val();
						
					if(spName.length == 0){
						$("#tipInfo6").html("请输入SP名称");
						return false;
					}
					if(spPhone.length == 0){
						$("#tipInfo7").html("请输入类SP联系电话");
						return false;
					}
					if(spConnect == 0 ){
						$("#tipInfo8").html("请输入spConnect");
						return false;
					} 
					if(!valzs(status)){
						$("#tipInfo9").html("请输入整数");
						return false;
					}
					if(iptvSpId.length == 0){
						$("#tipInfo10").html("请输入iptvSpId");
						return false;
					}					
				 $.post("<c:url value='/system/backstage/packgeInfo_check.do' />",			
				 	{
				 		spName : spName			 		
				 	},				 					 
				 	function(data){
				 		if(data == null){
			 				$.dialog.list["forAddInfo"].close();
							document.addForm.action="packgeInfo_add.do";
	       					document.addForm.submit(); 
				 		}else{
				 			$("#tipInfo6").html("SP名称不能相同");				 		
				 		}
						 					
					},"json");		
					return false ;				
  			},
    		cancelVal: '&nbsp;关闭&nbsp;',
    		cancel: true 
  		});
	}
	
	
	
	//修改
  	function showInfoEdit(form){
		 $("#tipInfo1").html("");
		 $("#tipInfo2").html("");
		 $("#tipInfo3").html("");
		 $("#tipInfo4").html("");
		 $("#tipInfo5").html("");
  		var id = getCheckedValue(form);
  		if(isNotNull(id)){
  			$.dialog({
  				width:300,
	  			id: "forEditInfo",
	  			title: "修改",
	  			content: document.getElementById("forEditInfo"),
	  			ok: function(){
	  				     $("#tipInfo1").html("");
						 $("#tipInfo2").html("");
						 $("#tipInfo3").html("");
						 $("#tipInfo4").html("");
						 $("#tipInfo5").html("");
	  				var spName = $("#spName").val();
					var spPhone = $("#spPhone").val();
	  				var spConnect = $("#spConnect").val();
					var status = $("#status").val();
	  				var iptvSpId = $("#iptvSpId").val();

					if(spName.length == 0){
						$("#tipInfo1").html("请输入SP名称");
						return false;
					}
					if(spPhone.length == 0){
						$("#tipInfo2").html("请输入类SP联系电话");
						return false;
					}
					if(spConnect == 0 ){
						$("#tipInfo3").html("请输入spConnect");
						return false;
					} 
					if(!valzs(status)){
						$("#tipInfo4").html("请输入整数");
						return false;
					}
					if(iptvSpId.length == 0){
						$("#tipInfo5").html("请输入iptvSpId");
						return false;
					}
					
	  				$.dialog.list["forEditInfo"].close();
	  				document.editForm.action="packgeInfo_edit.do?spId="+id;
	        		document.editForm.submit();
	  			},
	  			init: function () {
			         $.post("<c:url value='/system/backstage/packgeInfo_view.do' />",{spId : id},function(data){
						if(data!=null){
							$("#spName").val(data.spName),
							$("#spPhone").val(data.spPhone);
							$("#spConnect").val(data.spConnect);
							$("#iptvSpId").val(data.iptvSpId);
							$("#status").val(data.status);						
						}					
					},"json");
			    },
			    
	    		cancelVal: '&nbsp;关闭&nbsp;',
	    		cancel: true 
	  		});
  		}
	}
	
	//删除
	function showInfoDelete(form){
		var id = getCheckedValue(form);
  		if(isNotNull(id)){
			$.dialog.confirm('警告：您确认删除吗？',function(){
				window.location.href="<c:url value='/system/backstage/packgeInfo_delete.do?spId="+ id +"'/>";
			});
		}	
	}
	
	function getStatus(status){	
			alert(status);	
		var length = $("#status option").length;
		var option  = $("#status option") ;
		for(var i = 0; i <length ;i++){
			var value = option.get(i).value ;
			if(value == status){
				status = option.get(i).text	;						
			}
		}						
		return status;
	}
	
	
	</script>
	</head>
  <body>
  <form action="<c:url value='packgeInfo_list.do'/>" name="form" id="form" method="post">
  <table width="100%" border="0" cellspacing="6" cellpadding="0" style="border-collapse: separate; border-spacing: 6px;">
		<tr valign="top">
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="6" class="blockTable">
				<tr>
					<td height="30" valign="middle" class="blockTd"><img src="<c:url value='/images/system/role.gif'/>" /> 列表</td>
				</tr>
				<tr>
					<td style="padding:0 8px 4px;">
						 <div style="float: left; white-space: nowrap;">
						<pg:button type="button" action="packgeInfo_add" onclick="showInfoAdd();" value="新 增" />
						<pg:button type="button" action="packgeInfo_edit" onclick="showInfoEdit(this.form);" value="修 改" />
						<pg:button type="button" action="packgeInfo_delete" onclick="showInfoDelete(this.form);" value="删 除" />
					</div>
					<div style="float: right; white-space: nowrap;">					  		
				  		<select  id="keyWord" name = "keyWord" >					  					  						  			            	
						  			<option value="packgeUrl" >专区u&nbsp;r&nbsp;l&nbsp;：</option>				  							  			
						  			<option value="packgeOrde" >专区排序：</option>				  							  		
						  			<option value="packgeName" >专区名称：</option>
						  			<option value="packgePrice" >包月价格：</option>
				  		</select>
							<script type="text/javascript">
								//从后台返回时，设置下拉框默认
									var option = $("#keyWord option");
									var length = option.length;
									for(var i = 0 ;i < length ;i++){								
										 if(option[i].value == "${keyWord}"){
											option.eq(i).attr("selected",true);											
										}  
									} 
							</script>
						
						<input name="msgName" type="text" value="${msgName}" />
            	  		<input type="button" class="inputButton" name="Submitbutton" id="Submitbutton" value="查询" onclick="form.submit();" />
            		</div>
				</td>
			</tr>
			<tr>
				<td style="padding-top:0px;padding-left:6px;padding-right:6px;padding-bottom:8px;">
				<table width="100%" cellpadding="2" cellspacing="0" class="dataTable" id="dg1">
				<tr class="dataTableHead">

					<td width="10%" onclick="getStatus()">专区ID</td>
					<td width="3%"><input type="checkbox" id="chkAll" name="chkAll" onclick="checkall(this.form);"/></td>
					<td width="20%"><b>专区类型</b></td>
					<td width="10%"><b>创建时间</b></td>
					<td width="10%"><b>专区url</b></td>
					<td width="10%"><b>专区名称</b></td>
					<td width="17%"><b>专区排序</b></td>
					<td width="10%"><b>专区使用期限</b></td>
					<td width="10%"><b>专区状态</b></td>
					<td width="10%"><b>专区包月价格</b></td>
				</tr>
				<c:if test="${!empty pagination.list}">
   				<c:forEach items="${pagination.list}" var="item">
				<tr style="background-color:#F9FBFC">
					<td>${item.packgeId}</td>					
					<td><input type="checkbox" id="id" name="id" value="${item.packgeId}" /></td>
					<td>${item.packgeType}</td>
				<%-- 	<td><fmt:formatDate value="${item.packgeCritme}" type="date" dateStyle="long"  /></td> --%>
					<td>${item.packgeCritme}</td>
					<td>${item.packgeUrl}</td>
					<td>${item.packgeName}</td>
					<td>${item.packgeOrde}</td>
					<td>${item.packgeTerm}</td>
					<td>${item.packgeStatus}</td>
					<td>${item.packgePrice}</td>
				</tr>
				 </c:forEach>
   		  		</c:if>
				<tr>
					<td colspan="12" align="center" id="dg1_PageBar">
					<pg:zPage baseurl="packgeInfo_list.do?msgName=${msgName}" pagination="${pagination}"/>
					</td>
				</tr>
				</table>
				</td>
			</tr>
		</table> 
      	</td>
      </tr>
    </table>
  </form>
  
  <!-- 修改信息弹出层 -->
  <div id="forEditInfo" style="display: none;">
  <form action="" method="post" id="editForm" name="editForm">
    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="4" >
	  <tr>
	    <td align="right">SP名称：</td>
	    <td align="left">
	      <input type="text" size="20" id="spName" name="spName" />&nbsp;<span style="color: red;" id="tipInfo1"></span></td>
	  </tr>
	  <tr>
	    <td align="right">SP联系电话：</td>
	    <td align="left">
	      <input type="text" size="20" id="spPhone" name="spPhone" />&nbsp;<span style="color: red;" id="tipInfo2"></span></td>
	  </tr>
	  <tr>
	    <td align="right">SP联系方式：</td>
	    <td align="left">
	      <input type="text" size="20" id="spConnect" name="spConnect" />&nbsp;<span style="color: red;" id="tipInfo3"></span></td>
	  </tr>
	  <tr>
	    <td align="right">状态：</td>
	    <td align="left">
	      <select id="status" title="状态"  name="status" >
	      	<c:forEach  items="${enumStatus}" var="item" >	      	
	      		<option value="${item.enumKey}"  >${item.enumValue}</option>
	      	</c:forEach>	
	      </select>
	      &nbsp;<span style="color: red;" id="tipInfo4"></span></td>
	  </tr>
	  <tr>
	    <td align="right">IPTV_SP_ID：</td>
	    <td align="left">
	      <input type="text" size="20" id="iptvSpId" name="iptvSpId" />&nbsp;<span style="color: red;" id="tipInfo5"></span></td>
	  </tr>
	  </table>
	</form>
	</div>
	
	<!-- 添加信息弹出层 -->
  <div id="forAddInfo" style="display: none;">
  <form action="" method="post" id="addForm" name="addForm">
    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="4" >
	  <tr>
	    <td align="right">SP名称：</td>
	    <td align="left">
	      <input type="text" size="20" id="AspName" name="spName" />&nbsp;<span style="color: red;" id="tipInfo6"></span></td>
	  </tr>
	  <tr>
	    <td align="right">SP联系电话：</td>
	    <td align="left">
	      <input type="text" size="20" id="AspPhone" name="spPhone" />&nbsp;<span style="color: red;" id="tipInfo7"></span></td>
	  </tr>
	  <tr>
	    <td align="right">spConnect：</td>
	    <td align="left">
	      <input type="text" size="20" id="AspConnect" name="spConnect" />&nbsp;<span style="color: red;" id="tipInfo8"></span></td>
	  </tr>
	  <tr>
	    <td align="right">IPTV_SP_ID：</td>
	    <td align="left">
	      <input type="text" size="20"  name="iptvSpId" id="AiptvSpId" />&nbsp;<span style="color: red;" id="tipInfo10"></span></td>
	  </tr>
	  </table>
	</form>
	</div>
	
  </body>
</html>

