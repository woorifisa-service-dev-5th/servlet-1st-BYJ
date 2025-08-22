import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/posts")
public class PostController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset = UTF-8");
		final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost:3306/";
		final String DATABASE_NAME = "byj_db";
		final String USER = "root";
		final String PASSWORD = "1234";

		String problemIdParam = req.getParameter("problemId");


		String sql = "SELECT p.post_id, p.title, pr.problem_num, " +
		             "l.name AS name, u.user_name, " +
		             "p.comment_count, p.like_count, " +
		             "DATE_FORMAT(p.created_at, '%Y-%m-%d') AS created_date " +
		             "FROM post p " +
		             "JOIN user u ON p.user_id = u.user_id " +
		             "JOIN language l ON p.language_id = l.language_id " +
		             "JOIN problems pr ON p.problem_id = pr.problem_id ";

		if (problemIdParam != null) {
		    sql += "WHERE p.problem_id = ? ";   // id(PK)로 조건
		}

		sql += "ORDER BY p.created_at DESC";


		try {
			Class.forName(DRIVER_NAME);
			// DB 연결(커넥션 객체 생성)
			Connection conn = DriverManager.getConnection(DB_URL + DATABASE_NAME, USER, PASSWORD);
			PreparedStatement ps = conn.prepareStatement(sql);
			
			if (problemIdParam != null) {
			    ps.setLong(1, Long.parseLong(problemIdParam));
			}
			
			ResultSet rs = ps.executeQuery();
			PrintWriter out = resp.getWriter();
			{ // HTML head + CSS
				out.println("<!DOCTYPE html>");
				out.println("<html><head><meta charset='UTF-8'>");
				out.println("<title>게시글 목록</title>");
				out.println("<style>");
				out.println("body { font-family: Arial, sans-serif; font-size:14px; }");
				out.println(".post-table { width:100%; border-collapse:collapse; }");
				out.println(
						".post-table th { background:#f2f2f2; padding:8px; border-bottom:2px solid #ddd; text-align:left; }");
				out.println(".post-table td { padding:8px; border-bottom:1px solid #ddd; }");
				out.println(".post-table tr:hover { background:#f9f9f9; }");
				out.println(".post-table a { color:#0066cc; text-decoration:none; }");
				out.println(".post-table a:hover { text-decoration:underline; }");
				out.println("</style></head><body>");

				// Table
				out.println("<h1>게시글 목록</h1>");
				out.println("<table class='post-table'>");
				out.println("<thead><tr>" + "<th>ID</th><th>제목</th><th>문제ID</th><th>언어</th>"
						+ "<th>작성자</th><th>댓글</th><th>좋아요</th><th>작성일</th>" + "</tr></thead>");
				out.println("<tbody>");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				while (rs.next()) {
					long id = rs.getLong("post_id");
					String title = rs.getString("title");
					long problemId = rs.getLong("problem_num");
					String langName = rs.getString("name");
					String userName = rs.getString("user_name");
					int commentCount = rs.getInt("comment_count");
					int likeCount = rs.getInt("like_count");
					String createdAt = rs.getString("created_date");

					out.printf("<tr>" + "<td>%d</td>" + "<td><a href='%s/posts/%d'>%s</a></td>" + "<td>%d</td>"
							+ "<td>%s</td>" + "<td>%s</td>" + "<td>%d</td>" + "<td>%d</td>" + "<td>%s</td>" + "</tr>",
							id, req.getContextPath(), id, title, problemId, langName, userName, commentCount, likeCount,
							createdAt);
				}

				out.println("</tbody></table>");
				out.println("</body></html>");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}