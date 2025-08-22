<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dev.syntax.model.Example" %>
<%@ page import="dev.syntax.model.Problem" %>
<%
    Problem problem = (Problem) request.getAttribute("problem");
%>
<%
    List<Example> examples = (List<Example>) request.getAttribute("examples");
%>
<html>
<head>
    <title>
        <%
            if (problem != null) {
                out.print(problem.getNum() + "번: " + problem.getTitle());
            } else {
                out.print("문제 상세");
            }
        %>
    </title>
</head>
<body style="margin: 50px;">

<%
    if (problem != null) {
%>
		<span style="padding:6px 12px; color:white; background-color: #4CAF50;"><%= problem.getNum() %>번</span>
		<span>
    		<a href="<%= request.getContextPath() %>/posts?problemId=<%= problem.getId() %>" style="text-decoration:none; padding:6px 12px; color: black;">질문 게시판</a>
		</span>
        <h1 style="margin-top: 40px;"><%= problem.getTitle() %></h1>
        <h3>문제</h3> 
        <p><%= problem.getText() %></p>
        <h3>입력</h3>
        <p><%= problem.getInput() %></p>
        <h3>출력</h3>
        <p><%= problem.getOutput() %></p>
<%	for (int i = 0; i < examples.size(); i++) {
		int num = i + 1;
%>
		<div style="display: flex; gap: 20px; margin-bottom: 10px;">
			<div>
			        <h3>예제입력 <%= num %></h3>
			        <p><%= examples.get(i).getInput() %></p>
			</div>
			<div>
			        <h3>예제출력 <%= num %></h3>
			        <p><%= examples.get(i).getOutput() %></p>
			</div>
        </div>
<%	
}
%>
<%
    } else {
%>
        <p>해당 문제를 찾을 수 없습니다.</p>
<%
    }
%>

</body>
</html>
