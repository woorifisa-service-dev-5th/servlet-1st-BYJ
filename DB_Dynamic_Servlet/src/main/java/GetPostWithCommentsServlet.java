import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/posts/*")
public class GetPostWithCommentsServlet extends HttpServlet {

    final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost:3306/";
    final String DATABASE_NAME = "byj_db";
    final String USER = "root";
    final String PASSWORD = "1234";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.getWriter().println("게시글 ID가 없습니다.");
            return;
        }
        long postId = Long.parseLong(pathInfo.substring(1)); // "/{id}" → 숫자 변환

        String getPostQuery = "SELECT like_count, title, content, user_id, created_at "
                + "FROM POST WHERE post_id = ?";

        String getCommentQuery = "SELECT c.comment_id, c.content, c.created_at, u.user_name "
                + "FROM `COMMENT` c JOIN `USER` u ON c.user_id = u.user_id "
                + "WHERE c.post_id = ? ORDER BY c.created_at DESC";

        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, USER, PASSWORD)) {

            // 📌 게시글 출력
            try (PreparedStatement pstmt = connection.prepareStatement(getPostQuery)) {
                pstmt.setLong(1, postId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        resp.getWriter().println("<h2>게시글 상세</h2>");
                        resp.getWriter().println("<p><b>제목: " + rs.getString("title") + "</b></p>");
                        resp.getWriter().println("<p>내용: " + rs.getString("content") + "</p>");
                        resp.getWriter().println("<p>좋아요 수: " + rs.getInt("like_count") + "</p>");
                        resp.getWriter().println("<p>작성일: " + rs.getTimestamp("created_at") + "</p>");
                        resp.getWriter().println("<hr/>");
                    } else {
                        resp.getWriter().println("<h3>해당 게시글이 존재하지 않습니다.</h3>");
                        return;
                    }
                }
            }

            // 📌 댓글 출력
            try (PreparedStatement pstmt = connection.prepareStatement(getCommentQuery)) {
                pstmt.setLong(1, postId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    resp.getWriter().println("<h3>댓글 목록</h3>");
                    while (rs.next()) {
                        resp.getWriter().println("<p><b>" + rs.getString("user_name") + "</b>: "
                                + rs.getString("content") + " (" + rs.getTimestamp("created_at") + ")</p>");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h3>게시글/댓글 조회 실패: " + e.getMessage() + "</h3>");
        }
    }
}