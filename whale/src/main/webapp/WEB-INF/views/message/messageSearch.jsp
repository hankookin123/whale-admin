<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" href="../static/css/message/messageHome.css">

<script type="text/javascript"
	src="../static/js/message/massageSearch.js"></script>
<ul id="tabs">
	<li class="tab now" data-tab="msg">메시지</li>
	<li class="tab" data-tab="friend">친구</li>
	<li class="tab" data-tab="user">전체유저</li>
</ul>
<div id="result"><jsp:include page="messageTable.jsp" /></div>
