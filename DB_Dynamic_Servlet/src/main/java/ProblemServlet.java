import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.syntax.model.Example;
import dev.syntax.model.Problem;

@WebServlet("/problems/*")
public class ProblemServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost:3306/";
		final String DATABASE_NAME = "byj_db";
		final String USER = "root";
		final String PASSWORD = "1234";
		
		try {
			Class.forName(DRIVER_NAME);
			// DB 연결(커넥션 객체 생성)
			Connection con = DriverManager.getConnection(DB_URL + DATABASE_NAME, USER, PASSWORD);
            String pathInfo = request.getPathInfo(); // "/1"
            long problemId = Long.parseLong(pathInfo.substring(1));
	            PreparedStatement ps = con.prepareStatement("SELECT * FROM problems WHERE problem_id = ?");
	            PreparedStatement exps = con.prepareStatement("SELECT * From example WHERE problem_id = ?");
	            ps.setLong(1, problemId);
	            exps.setLong(1, problemId);
	            ResultSet rs = ps.executeQuery();
	            ResultSet exrs = exps.executeQuery();

	            if (rs.next()) {
	                Problem p = new Problem();
	                p.setId(rs.getLong("problem_id"));
	                p.setNum(rs.getInt("problem_num"));
	                p.setTitle(rs.getString("problem_title"));
	                p.setText(rs.getString("problem_text"));
	                p.setInput(rs.getString("problem_input"));
	                p.setOutput(rs.getString("problem_output"));
	            
	                
	                // JSP로 전달
	                request.setAttribute("problem", p);
	            }
	            
	            List<Example> exList = new ArrayList<>();
	            
	            while (exrs.next()) {
	            	Example e = new Example();
	                e.setId(exrs.getLong("example_id"));
	            	e.setProblemId(exrs.getLong("problem_id"));
	                e.setInput(exrs.getString("example_input"));
	                e.setOutput(exrs.getString("example_output"));
	                exList.add(e);
	            }
	            	request.setAttribute("examples", exList);

	            request.getRequestDispatcher("/WEB-INF/problem.jsp").forward(request, response);
	            
	            exrs.close();
	            rs.close();
	            exps.close();
	            ps.close();
	            con.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }
			
//			CREATE TABLE problems (
//    				problem_id BIGINT AUTO_INCREMENT PRIMARY KEY,    -- 문제 고유 ID
//				    problem_num INT NOT NULL,                     -- 문제 번호
//				    problem_title VARCHAR(255) NOT NULL,          -- 문제 제목
//				    problem_text VARCHAR(255) NOT NULL,                   -- 문제 설명
//				    problem_input VARCHAR(255),                           -- 입력 예시
//				    problem_output VARCHAR(255)                          -- 출력 예시
//				) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
//			
//			INSERT INTO problems (problem_num, problem_title, problem_text, problem_input, problem_output)
//			VALUES 
//			(1000, 'A+B', '두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오.', '첫째 줄에 A와 B가 주어진다. (0 < A, B < 10)', '첫째 줄에 A+B를 출력한다.');
			
	
//	CREATE TABLE example (
//		    example_id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 문제 고유 ID
//		    problem_id BIGINT,                -- 문제 설명
//		    example_input VARCHAR(255),                           -- 입력 예시
//		    example_output VARCHAR(255)                           -- 출력 예시
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
	
//	INSERT INTO example(problem_id, example_input, example_output) VALUES (1, '1 2', '3');
//	INSERT INTO example(problem_id, example_input, example_output) VALUES (1, '2 2', '4');
}
