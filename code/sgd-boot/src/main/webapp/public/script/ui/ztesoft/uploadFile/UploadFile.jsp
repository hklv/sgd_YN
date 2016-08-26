<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<%String Language = "en_US"; %>

<html>
	<head>
		<title>UploadFile</title>
		<script type="text/javascript">

		function onclickBtnOK(){

			$("#frameFile")[0].contentWindow.uploadFile("280px", GetUrlParameter("userPath"));		
		}
		
		function afterDoPostBack(){
			var obj = $("#frameFile")[0].contentWindow.getUploadResultObj();
			var retObj = {};
			retObj.FileName = obj.labelFileNameNew;
			retObj.FileId = obj.labelFileId;
			retObj.FilePath = obj.labelFilePath;
			if(GetUrlParameter("userPath")){
			
				retObj.idVal = GetUrlParameter("userPath") + obj.labelFileNameNew;
				retObj.textVal = GetUrlParameter("userPath") + obj.labelFileNameNew;
			}else{
				retObj.idVal = obj.labelFileNameNew;
				retObj.textVal = obj.labelFileNameNew;
			}

			closeModalWindow(retObj);
		}
		function onclickBtnCancel(){
			closeModalWindow();
		}
		
	</script>
	</head>
	<body>
		<div class="layout_detail" style='margin-left: 5px;'>
			<table border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td class="img1"></td>
					<td class="bkg"></td>
					<td class="img2"></td>
				</tr>
				<tr>
					<td class="bkg"></td>
					<td class="bkgPadding">
						<table border=0  cellpadding=0 cellspacing=0 style='height:60px'>
							<tr>
								<th id="th11"></th>
								<th id="th12"></th>
								<th id="th13"></th>
								<th id="th14"></th>
								<th id="th15"></th>
							</tr>
							<tr>
								<td class="fieldNameCol1st" style="padding-right:10px"><%=getString("UPLOAD_LAB_FILE") %></td>
								<td colspan="4"style="margin-left:15px">
			   						<IFRAME ID="frameFile" FRAMEBORDER="0" height="30px" width="100%" SCROLLING="NO" SRC="public/script/ui/ztesoft/uploadFile/up.jsp?widthPx=width:300px"></IFRAME>
			   					</td>
							</tr>								
						</table>
					</td>
					<td class="bkg"></td>
				</tr>
				<tr>
					<td class="img3"></td>
					<td class="bkg"></td>
					<td class="img4"></td>
				</tr>
			</table>			
		</div>
		<div class="layout_detail" style='width:100%'>
			<table border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td class="img1"></td>
					<td class="bkg"></td>
					<td class="img2"></td>
				</tr>
				<tr>
					<td class="bkg"></td>
					<td class="bkgPadding" style='padding-right:0px'>
						<table border=0  cellpadding=0 cellspacing=0>
							<tr>
								<th id="th11"></th>
								<th id="th12"></th>
								<th id="th13"></th>
								<th id="th14"></th>
								<th id="th15"></th>
							</tr>
							<tr>								
								<td style="padding-right:84px" colspan="5" align="right">
									<input type="button" id="btnOK" class="button" onclick="onclickBtnOK();" value="<%=getString("OK") %>">
									<input type="button" id="btnCancel" class="button" onclick="onclickBtnCancel();" value="<%=getString("Cancel") %>">									
								</td>	
							</tr>															
						</table>
					</td>
					<td class="bkg"></td>
				</tr>
				<tr>
					<td class="img3"></td>
					<td class="bkg"></td>
					<td class="img4"></td>
				</tr>
			</table>			
		</div>																																								
	</body>
</html>